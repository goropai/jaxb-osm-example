package com.goropai.elements;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"minlat", "minlon", "maxlat", "maxlon"}, name = "bounds")
public class Bounds {
    private double minlat;
    private double minlon;
    private double maxlat;
    private double maxlon;

    public double getMinlat() {
        return minlat;
    }

    @XmlAttribute
    public void setMinlat(double minlat) {
        this.minlat = minlat;
    }

    public double getMinlon() {
        return minlon;
    }

    @XmlAttribute
    public void setMinlon(double minlon) {
        this.minlon = minlon;
    }

    public double getMaxlat() {
        return maxlat;
    }

    @XmlAttribute
    public void setMaxlat(double maxlat) {
        this.maxlat = maxlat;
    }

    public double getMaxlon() {
        return maxlon;
    }

    @XmlAttribute
    public void setMaxlon(double maxlon) {
        this.maxlon = maxlon;
    }
}
