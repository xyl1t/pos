package pkgData;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import java.time.LocalDateTime;

public class GeoPosition {
    private Point location;
    private LocalDateTime datetime;

    public GeoPosition(double first, double second, LocalDateTime datetime) {
        this.location = new Point(new Position(first, second));
        this.datetime = datetime;

    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }
}