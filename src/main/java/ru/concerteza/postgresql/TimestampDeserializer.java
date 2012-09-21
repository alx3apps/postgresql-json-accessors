package ru.concerteza.postgresql;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.sql.Timestamp;

/**
 * User: alexey
 * Date: 9/19/12
 */
class TimestampDeserializer implements JsonDeserializer<Timestamp> {
    @Override
    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return Timestamp.valueOf(json.getAsString());
    }
}
