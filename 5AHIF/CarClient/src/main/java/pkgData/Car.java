package pkgData;

import org.bson.types.ObjectId;

import java.util.ArrayList;

public class Car {
    private ObjectId _id;
    private String carName;
    private ArrayList<GeoPosition> positions;

    public Car(String carName, ArrayList<GeoPosition> positions) {
        this.carName = carName;
        this.positions = positions;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public ObjectId getId() {
        return _id;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public ArrayList<GeoPosition> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<GeoPosition> positions) {
        this.positions = positions;
    }

    public void addPosition(GeoPosition pos) {
        this.positions.add(pos);
    }
}
