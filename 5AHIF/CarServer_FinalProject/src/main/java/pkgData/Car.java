package pkgData;

import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.Comparator;

public class Car {
    private ObjectId _id;
    private String carName;
    private ArrayList<GeoPosition> positions;

    public Car(String carName, ArrayList<GeoPosition> positions) {
        this.carName = carName;
        this.positions = positions;
    }

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
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

    public GeoPosition getLastPosition() {
        // TODO: is this ok? to sort every time?
        ArrayList<GeoPosition> copy = new ArrayList<>(positions);
        copy.sort(Comparator.comparing(GeoPosition::getDatetime));
        return copy.get(copy.size() - 1);
    }
}
