package pkgMisc;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class GsonLocalDateTimeSerializer implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    @Override
    public JsonElement serialize(LocalDateTime dateTime, Type type, JsonSerializationContext jsc) {
        long milliseconds = dateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();

        JsonObject jo = new JsonObject();
        jo.addProperty("$date", milliseconds);
        return jo;
    }

    @Override
    public LocalDateTime deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        long milliseconds = je.getAsJsonObject().get("$date").getAsLong();
        return LocalDateTime.ofEpochSecond(milliseconds / 1000, 0, ZoneOffset.UTC);
    }
}
