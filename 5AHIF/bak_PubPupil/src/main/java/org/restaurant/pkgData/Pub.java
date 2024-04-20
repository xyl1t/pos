package org.restaurant.pkgData;

import org.bson.types.ObjectId;

public class Pub {
    final static private int UNDEFINED = -99;
    private static final int MAXLEN_OF_DESC = 10;
    private ObjectId _id;
    private String name, location;
    private int founded; //year
    private String description;

    public Pub(String name, String location, int founded, String description) {
        this.name = name;
        this.location = location;
        this.founded = founded;
        this.description = description;
    }

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId id) {
        this._id = id;
    }


    @Override
    public String toString() {
        int length = Math.min(description.length(), MAXLEN_OF_DESC);
        return name + ", " + location + ", " + founded + ", " + description.substring(0, length) + "...";
    }

    public String toLongString() {
        return "Pub{id=" + _id + ", name=" + name + ", location=" + location + ", founded=" + founded + ", description=" + description + '}';
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getFounded() {
        return founded;
    }

    public String getDescription() {
        return description;
    }
}
