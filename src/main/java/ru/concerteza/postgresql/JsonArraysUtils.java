package ru.concerteza.postgresql;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Iterator;

import static ru.concerteza.postgresql.JsonUtils.*;

/**
 * User: alexey
 * Date: 9/19/12
 */
class JsonArraysUtils {
    static JsonArray getArray(String json, String key) {
        JsonElement el = getElement(json, key);
        if(null == el || el.isJsonNull()) return null;
        if(!el.isJsonArray())throw new RuntimeException("Target value: '" + el +
                "' is not a JSON array, input json: '" + json + "'");
        return el.getAsJsonArray();
    }

    static String[] arrayAsObjectArray(Iterator<JsonElement> iter, int size, String jsonForLogging) {
        String[] res = new String[size];
        for(int i = 0; i < size; i++) {
            JsonElement el = iter.next();
            if(el.isJsonNull()) continue;
            res[i] = objectToString(el, jsonForLogging);
        }
        return res;
    }

    static String[] arrayAsMultiArray(Iterator<JsonElement> iter, int size, String jsonForLogging) {
        String[] res = new String[size];
        for(int i = 0; i < size; i++) {
            JsonElement el = iter.next();
            if(el.isJsonNull()) continue;
            res[i] = arrayToString(el, jsonForLogging);
        }
        return res;
    }

    static String[] arrayAsStringArray(Iterator<JsonElement> iter, int size, String jsonForLogging) {
        String[] res = new String[size];
        for(int i = 0; i < size; i++) {
            JsonElement el = iter.next();
            if(el.isJsonNull()) continue;
            res[i] = elementAsString(el, jsonForLogging);
        }
        return res;
    }

    static Boolean[] arrayAsBooleanArray(Iterator<JsonElement> iter, int size, String jsonForLogging) {
        Boolean[] res = new Boolean[size];
        for(int i = 0; i < size; i++) {
            JsonElement el = iter.next();
            if(el.isJsonNull()) continue;
            res[i] = elementAsBoolean(el, jsonForLogging);
        }
        return res;
    }

    static Integer[] arrayAsIntegerArray(Iterator<JsonElement> iter, int size, String jsonForLogging) {
        Integer[] res = new Integer[size];
        for(int i = 0; i < size; i++) {
            JsonElement el = iter.next();
            if(el.isJsonNull()) continue;
            res[i] = elementAsInteger(el, jsonForLogging);
        }
        return res;
    }

    static Long[] arrayAsLongArray(Iterator<JsonElement> iter, int size, String jsonForLogging) {
        Long[] res = new Long[size];
        for(int i = 0; i < size; i++) {
            JsonElement el = iter.next();
            if(el.isJsonNull()) continue;
            res[i] = elementAsLong(el, jsonForLogging);
        }
        return res;
    }

    static BigDecimal[] arrayAsBigDecimalArray(Iterator<JsonElement> iter, int size, String jsonForLogging) {
        BigDecimal[] res = new BigDecimal[size];
        for(int i = 0; i < size; i++) {
            JsonElement el = iter.next();
            if(el.isJsonNull()) continue;
            res[i] = elementAsBigDecimal(el, jsonForLogging);
        }
        return res;
    }

    static Timestamp[] arrayAsTimestampArray(Iterator<JsonElement> iter, int size, String jsonForLogging) {
        Timestamp[] res = new Timestamp[size];
        for(int i = 0; i < size; i++) {
            JsonElement je = iter.next();
            if(je.isJsonNull()) continue;
            String str = elementAsString(je, jsonForLogging);
            res[i] = stringAsTimestamp(str, jsonForLogging);
        }
        return res;
    }
}