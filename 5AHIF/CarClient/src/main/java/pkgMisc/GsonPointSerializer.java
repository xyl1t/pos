package pkgMisc;

import com.google.gson.*;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import java.lang.reflect.Type;

public class GsonPointSerializer implements JsonSerializer<Point>, JsonDeserializer<Point> {
    @Override
    public JsonElement serialize(Point point, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jo = new JsonObject();

        jo.addProperty("type", "Point");

        JsonArray ja = new JsonArray();
        ja.add(point.getPosition().getValues().get(0));
        ja.add(point.getPosition().getValues().get(1));
        jo.add("coordinates", ja);
        return jo;
    }

    @Override
    public Point deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonElement je = jsonElement.getAsJsonObject().get("coordinates");
        double lng = je.getAsJsonArray().get(0).getAsDouble();
        double lat = je.getAsJsonArray().get(1).getAsDouble();

        return new Point(new Position(lng, lat));
    }
}