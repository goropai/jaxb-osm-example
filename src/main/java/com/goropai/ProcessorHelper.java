package com.goropai;

import com.goropai.elements.*;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ProcessorHelper {

    private static final double MAX_DIFF = 0.5d;

    private static ProcessorHelper processorHelper;

    private ProcessorHelper() {
    }

    public static ProcessorHelper getInstance() {
        if (processorHelper == null) {
            processorHelper = new ProcessorHelper();
        }
        return processorHelper;
    }

    private double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return earthRadius * c;
    }

    private double distFromLib(double lat1, double lng1, double lat2, double lng2) {
        GeodeticCalculator geoCalc = new GeodeticCalculator();
        Ellipsoid reference = Ellipsoid.WGS84;
        GlobalCoordinates pointOne = new GlobalCoordinates(lat1, lng1);
        GlobalCoordinates pointTwo = new GlobalCoordinates(lat2, lng2);
        GeodeticCurve geodeticCurve = geoCalc.calculateGeodeticCurve(reference, pointOne, pointTwo);
        return geodeticCurve.getEllipsoidalDistance();
    }

    private double squareFrom(List<Node> nodes, String role) {
        double area = 0d;
        if (nodes.size() > 2) {
            nodes.add(nodes.get(0));
            for (int i = 0; i < nodes.size() - 1; i++) {
                Node node1 = nodes.get(i);
                Node node2 = nodes.get(++i);
                area += convertToRadians(node2.getLon() - node1.getLon()) * (2 + Math.sin(convertToRadians(node1.getLat()))
                        + Math.sin(convertToRadians(node2.getLat())));
            }
            area = Math.abs(area * 6378137 * 6378137 / 2);
            if (role.equals(Way.ROLE_INNER)) {
                area = area * (-1);
            }
        }
        return area;
    }

    private double convertToRadians(double input) {
        return input * Math.PI / 180;
    }

    public File constructUrlAndGetFile() throws IOException, URISyntaxException {
        Scanner scanner = new Scanner(System.in);
        File file = new File("C:\\test.xml");
        System.out.println("Type \\`u\\` to read data from URL or \\`f\\` for file: ");
        if (scanner.next().trim().equalsIgnoreCase("u")) {
            while (true) {
                System.out.println("Enter left top latitude: ");
                double minlat = scanner.nextDouble();
                System.out.println("Enter left top longitude: ");
                double minlon = scanner.nextDouble();
                System.out.println("Enter right bottom latitude: ");
                double maxlat = scanner.nextDouble();
                System.out.println("Enter right bottom longitude: ");
                double maxlon = scanner.nextDouble();
                if (((Math.abs(minlon - maxlon)) > MAX_DIFF) || (Math.abs(minlat - maxlat)) > MAX_DIFF) {
                    System.out.println("You've entered wrong data. Type \\`exit\\` for exit or try again: ");
                    if (scanner.next().trim().equalsIgnoreCase("exit")) {
                        break;
                    }
                } else {
                    URL url = new URL("https://api.openstreetmap.org/api/0.6/map?bbox="
                            + minlat + "," + minlon + "," + maxlat + "," + maxlon);
                    try (BufferedInputStream in = new BufferedInputStream(url.openStream());
                         FileOutputStream out = new FileOutputStream(file)) {
                        final byte data[] = new byte[1024];
                        int count;
                        while ((count = in.read(data, 0, 1024)) != -1) {
                            out.write(data, 0, count);
                        }
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        } else {
            System.out.println("Enter *.osm/*.pbf file path: ");
            file =  new File(scanner.next().trim());
        }
        return file;
    }

    public Osm unmarshallAndGetOsm(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Osm.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (Osm) unmarshaller.unmarshal(file);
    }

    public void getLengthsSortedByLength(Osm osm) {
        Map<Integer, String> wayNames = new HashMap<>();
        Map<Integer, Double> wayLengths = new HashMap<>();

        List<Node> nodes = osm.getNodes();
        List<Way> ways = osm.getWays();

        for (Way way : ways) {
            List<Nd> nds = way.getNds();
            List<Integer> ndIds = nds.stream().map(Nd::getRef).collect(Collectors.toList());
            List<Node> wayNodes = nodes.stream()
                    .filter(x -> (ndIds.contains(x.getId())))
                    .collect(Collectors.toList());
            if (wayNodes.get(0) != wayNodes.get(wayNodes.size() - 1)) {
                Node firstNode = wayNodes.get(0);
                double wayLength = 0d;
                for (int i = 1; i < wayNodes.size(); i++) {
                    Node secondNode = wayNodes.get(i);
                    wayLength += distFrom(firstNode.getLat(), firstNode.getLon(), secondNode.getLat(), secondNode.getLon());
                    firstNode = secondNode;
                }
                int wayId = way.getId();
                String wayName = "";
                if (way.getTags() != null) {
                    for (Tag tag : way.getTags()) {
                        if (tag.getK().equals("name")) {
                            wayName = tag.getV();
                        }
                    }
                }
                wayNames.put(wayId, wayName.equals("") ? "wayId=" + wayId : wayName);
                wayLengths.put(wayId, wayLength);
            }
        }

        wayLengths.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(x -> System.out.println(wayNames.get(x.getKey()) + " - " + x.getValue() + " meters"));
    }

    public void getSquaresSortedBySize(Osm osm) {
        Map<Integer, Double> relationSquares = new HashMap<>();

        List<Node> nodes = osm.getNodes();
        List<Way> ways = osm.getWays();
        List<Relation> relations = osm.getRelations();

        for (Relation relation : relations) {
            for (Tag tag : relation.getTags()) {
                if (tag.getV().equals("multipolygon")) {
                    List<Member> members = relation.getMembers();
                    List<Integer> memberIds = members.stream().map(Member::getRef).collect(Collectors.toList());
                    Map<Integer, String> memberRoles = members.stream().collect(Collectors.toMap(Member::getRef, Member::getRole));
                    List<Way> relationWays = ways.stream()
                            .filter(x -> (memberIds.contains(x.getId())))
                            .collect(Collectors.toList());
                    double relationSquare = 0d;
                    for (Way way : relationWays) {
                        List<Nd> nds = way.getNds();
                        List<Integer> ndIds = nds.stream().map(Nd::getRef).collect(Collectors.toList());
                        List<Node> wayNodes = nodes.stream()
                                .filter(x -> (ndIds.contains(x.getId())))
                                .collect(Collectors.toList());
                        String role = memberRoles.get(way.getId());
                        relationSquare += squareFrom(wayNodes, role);
                    }
                    relationSquares.put(relation.getId(), relationSquare);
                }
            }
        }

        relationSquares.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(x -> System.out.println(x.getKey() + " - " + x.getValue() + " meters sq."));
    }
}
