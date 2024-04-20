package org.restaurant.pkgData;

import com.mongodb.client.model.geojson.Point;
import org.bson.types.ObjectId;

public class Location {
    private ObjectId _id;
    private String name;
    private Point position;

    public Location(String name, Point position) {
        this.name = name;
        this.position = position;
    }

    public ObjectId getId() {
        return _id;
    }

    @Override
    public String toString() {
        return name + ", long/lat: " + position.getPosition().getValues().get(0) + " / " + position.getPosition().getValues().get(1);
    }

    public String getName() {
        return name;
    }

    public Point getPosition() {
        return position;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setId(ObjectId id) {
        this._id = id;
    }
}
