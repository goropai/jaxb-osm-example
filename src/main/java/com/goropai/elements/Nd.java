package com.goropai.elements;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"ref"}, name = "nd")
public class Nd {
    private int ref;

    public int getRef() {
        return ref;
    }

    @XmlAttribute
    public void setRef(int ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        return "";
    }
}
