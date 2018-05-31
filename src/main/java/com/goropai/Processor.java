package com.goropai;

import com.goropai.elements.Osm;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;


public class Processor {

    private static ProcessorHelper processorHelper;

    public Processor() {
        processorHelper = ProcessorHelper.getInstance();
    }

    public static void main(String[] args) {
        Processor processor = new Processor();

        //todo read bbox and compose URL, get XML as file
        File xmlFile = null;
        try {
            xmlFile = processorHelper.constructUrlAndGetFile();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        //todo unmarshall the XML and get root element
        Osm osm = null;
        try {
            osm = processorHelper.unmarshallAndGetOsm(xmlFile);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        //todo get lengths
        processorHelper.getLengthsSortedByLength(osm);

        //todo get squares
        processorHelper.getSquaresSortedBySize(osm);

    }
}
