package com.goropai.elements;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement
@XmlType(propOrder = {"version", "generator", "copyright", "attribution", "license", "bounds", "nodes", "ways", "relations"}, name = "osm")
public class Osm {

    private String version;
    private String generator;
    private String copyright;
    private String attribution;
    private String license;
    private Bounds bounds;
    private List<Node> nodes;
    private List<Way> ways;
    private List<Relation> relations;

    public String getVersion() {
        return version;
    }

    @XmlAttribute
    public void setVersion(String version) {
        this.version = version;
    }

    public String getGenerator() {
        return generator;
    }

    @XmlAttribute
    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getCopyright() {
        return copyright;
    }

    @XmlAttribute
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getAttribution() {
        return attribution;
    }

    @XmlAttribute
    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public String getLicense() {
        return license;
    }

    @XmlAttribute
    public void setLicense(String license) {
        this.license = license;
    }

    public Bounds getBounds() {
        return bounds;
    }

    @XmlElement
    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    @XmlElement(name = "node")
    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Way> getWays() {
        return ways;
    }

    @XmlElement(name = "way")
    public void setWays(List<Way> ways) {
        this.ways = ways;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    @XmlElement(name = "relation")
    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    @Override
    public String toString() {
        return "";
    }
}
