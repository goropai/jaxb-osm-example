package com.goropai;

import com.goropai.elements.*;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Test2 {

    public static void main(String[] args) throws MalformedURLException {
        File file = new File("C:\\test.xml");
        URL url = new URL("https://api.openstreetmap.org/api/0.6/map?bbox=11.54,48.14,11.543,48.145");
        try (BufferedInputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream out = new FileOutputStream(file)) {
            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                out.write(data, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(file.getTotalSpace());
    }
}
