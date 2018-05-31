package com.goropai.elements;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"type", "ref", "role"}, name = "member")
public class Member {
    private String type;
    private int ref;
    private String role;

    public String getType() {
        return type;
    }

    @XmlAttribute
    public void setType(String type) {
        this.type = type;
    }

    public int getRef() {
        return ref;
    }

    @XmlAttribute
    public void setRef(int ref) {
        this.ref = ref;
    }

    public String getRole() {
        return role;
    }

    @XmlAttribute
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "";
    }
}
