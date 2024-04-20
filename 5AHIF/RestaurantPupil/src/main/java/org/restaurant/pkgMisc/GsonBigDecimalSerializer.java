package org.restaurant.pkgMisc;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;

public class GsonBigDecimalSerializer implements JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonElement je = jsonElement.getAsJsonObject().get("$numberDecimal");
        return new BigDecimal(je == null ? "0" : je.getAsString());
    }
}
