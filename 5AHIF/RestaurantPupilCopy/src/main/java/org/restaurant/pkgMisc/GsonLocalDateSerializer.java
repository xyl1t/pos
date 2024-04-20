package org.restaurant.pkgMisc;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class GsonLocalDateSerializer implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    @Override
    public JsonElement serialize(LocalDate date, Type type, JsonSerializationContext jsc) {
        JsonObject jo = new JsonObject();
        jo.addProperty("$date", date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
        return jo;
    }

    @Override
    public LocalDate deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        return Instant.ofEpochMilli(je.getAsJsonObject().get("$date").getAsLong()).atZone(ZoneOffset.UTC).toLocalDate();
    }
}