package org.restaurant.pkgMisc;

import com.google.gson.*;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;

public class GsonSerializerObjectId implements JsonSerializer<ObjectId>, JsonDeserializer<ObjectId> {

    @Override
    public JsonElement serialize(ObjectId objectId, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jo = new JsonObject();
        jo.addProperty("$oid", objectId.toHexString());
        return jo;
    }

    @Override
    public ObjectId deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new ObjectId(jsonElement.getAsJsonObject().get("$oid").getAsString());
    }
}
