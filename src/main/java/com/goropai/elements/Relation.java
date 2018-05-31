package com.goropai.elements;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlType(propOrder = {"id", "visible", "version", "changeset", "timestamp", "user", "uid", "members", "tags"}, name = "relation")
public class Relation {

    private int id;
    private String visible;
    private int version;
    private int changeset;
    private String timestamp;
    private String user;
    private int uid;
    private List<Member> members;
    private List<Tag> tags;

    public int getId() {
        return id;
    }

    @XmlAttribute
    public void setId(int id) {
        this.id = id;
    }

    public String getVisible() {
        return visible;
    }

    @XmlAttribute
    public void setVisible(String visible) {
        this.visible = visible;
    }

    public int getVersion() {
        return version;
    }

    @XmlAttribute
    public void setVersion(int version) {
        this.version = version;
    }

    public int getChangeset() {
        return changeset;
    }

    @XmlAttribute
    public void setChangeset(int changeset) {
        this.changeset = changeset;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @XmlAttribute
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    @XmlAttribute
    public void setUser(String user) {
        this.user = user;
    }

    public int getUid() {
        return uid;
    }

    @XmlAttribute
    public void setUid(int uid) {
        this.uid = uid;
    }

    public List<Member> getMembers() {
        return members;
    }

    @XmlElement(name = "member")
    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public List<Tag> getTags() {
        return tags;
    }

    @XmlElement(name = "tag")
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");
        if (!tags.isEmpty()) {
            for (Tag tag:tags) {
                stringBuilder.append(tag).append(" ");
            }
        }
        return "relation [ " + "id=" + id +" [ " + (tags.isEmpty()?"":(stringBuilder.toString()).trim()) + " ]]";
    }

    //todo method square
    public double getSquare() {
        return 1d;
    }
}
