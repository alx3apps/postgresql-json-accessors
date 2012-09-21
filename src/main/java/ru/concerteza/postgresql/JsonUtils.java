package ru.concerteza.postgresql;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

import static ru.concerteza.postgresql.JsonAccessors.*;

/**
 * User: alexey
 * Date: 9/19/12
 */
class JsonUtils {
    static String objectToString(JsonElement el, String jsonForLogging) {
        if(!el.isJsonObject()) throw new RuntimeException("Target value: '" + el +
                "' is not a JSON object, input json: '" + jsonForLogging + "'");
        return el.toString();
    }

    static String arrayToString(JsonElement el, String jsonForLogging) {
        if(!el.isJsonArray()) throw new RuntimeException("Target value: '" + el +
                "' is not a JSON array, input json: '" + jsonForLogging + "'");
        return el.toString();
    }

    static JsonPrimitive elementAsPrimitive(JsonElement el, String jsonForLogging) {
        if(!el.isJsonPrimitive()) throw new RuntimeException("Target value: '" + el +
                "' is not a JSON primitive, input json: '" + jsonForLogging + "'");
        return el.getAsJsonPrimitive();
    }

    static String elementAsString(JsonElement el, String jsonForLogging) {
        JsonPrimitive jp = elementAsPrimitive(el, jsonForLogging);
        if (!jp.isString()) throw new RuntimeException("Target value: '" + jp +
                "' is not a JSON string, input json: '" + jsonForLogging + "'");
        return jp.getAsString();
    }

    static boolean elementAsBoolean(JsonElement el, String jsonForLogging) {
        JsonPrimitive jp = elementAsPrimitive(el, jsonForLogging);
        if(!jp.isBoolean()) throw new RuntimeException("Target value: '" + jp +
                "' is not a JSON boolean, input json: '" + jsonForLogging + "'");
        return jp.getAsBoolean();
    }

    static int elementAsInteger(JsonElement el, String jsonForLogging) {
        JsonPrimitive jp = elementAsPrimitive(el, jsonForLogging);
        if(!jp.isNumber()) throw new RuntimeException("Target value: '" + jp +
                "' is not a JSON number, input json: '" + jsonForLogging + "'");
        return jp.getAsNumber().intValue();
    }

    static long elementAsLong(JsonElement el, String jsonForLogging) {
        JsonPrimitive jp = elementAsPrimitive(el, jsonForLogging);
        if(!jp.isNumber()) throw new RuntimeException("Target value: '" + jp +
                "' is not a JSON number, input json: '" + jsonForLogging + "'");
        return jp.getAsNumber().longValue();
    }

    static BigDecimal elementAsBigDecimal(JsonElement el, String jsonForLogging) {
        JsonPrimitive jp = elementAsPrimitive(el, jsonForLogging);
        if(!jp.isNumber()) throw new RuntimeException("Target value: '" + jp +
                "' is not a JSON number, input json: '" + jsonForLogging + "'");
        return jp.getAsBigDecimal();
    }

    static Timestamp stringAsTimestamp(String str, String jsonForLogging) {
        try {
            return Timestamp.valueOf(str);
        } catch(IllegalArgumentException e) {
            throw new RuntimeException("Target value: '" + str +
                    "' has wrong format for timestamp, must be 'yyyy-MM-dd HH:mm:ss', input json: '" + jsonForLogging + "'");
        }
    }

    static JsonElement getElement(String json, String key) {
        if(null == json || null == key) return null;
        Map<String, JsonElement> map = GSON.fromJson(json, MAP_TYPE);
        return map.get(key);
    }
}
