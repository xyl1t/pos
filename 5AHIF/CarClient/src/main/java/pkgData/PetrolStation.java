package pkgData;

import com.mongodb.client.model.geojson.Point;
import org.bson.types.ObjectId;

public class PetrolStation {
    private ObjectId _id;
    private String stationName;
    private Point position;

    public PetrolStation(String stationName, Point position) {
        this.stationName = stationName;
        this.position = position;
    }

    @Override
    public String toString() {
        return "PetrolStation{" +
                "_id=" + _id +
                ", stationName='" + stationName + '\'' +
                ", position=" + position +
                '}';
    }

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}
