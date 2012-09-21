package ru.concerteza.postgresql;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static ru.concerteza.postgresql.JsonArraysUtils.*;
import static ru.concerteza.postgresql.JsonUtils.*;

/**
 * Functions to access JSON fields. Written on top of <a href="http://code.google.com/p/google-gson/">google-gson</a>
 *
 * @author alexey
 * Date: 5/26/12
 */
public class JsonAccessors {
    static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Timestamp.class, new TimestampDeserializer())
            .create();
    static final Type MAP_TYPE = new TypeToken<LinkedHashMap<String, JsonElement>>() {}.getType();
    static final Type LIST_TYPE = new TypeToken<ArrayList<JsonElement>>() {}.getType();

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return object for provided key as String or <code>null</code> if key doesn't exist
     * @throws RuntimeException if target value is not JSON object
     */
    public static String getObject(String json, String key) {
        JsonElement el = getElement(json, key);
        if(null == el || el.isJsonNull()) return null;
        return objectToString(el, json);
    }

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as String or <code>null</code> if key doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if target value is not JSON string
     */
    public static String getString(String json, String key) {
        JsonElement el = getElement(json, key);
        if(null == el || el.isJsonNull()) return null;
        return elementAsString(el, json);
    }

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as Boolean or <code>null</code> if key doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if target value is not JSON boolean
     */
    public static Boolean getBoolean(String json, String key) {
        JsonElement el = getElement(json, key);
        if(null == el || el.isJsonNull()) return null;
        return elementAsBoolean(el, json);
    }

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as Integer or <code>null</code> if key doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if target value is not JSON number
     * @throws NumberFormatException if target number is not integer
     */
    public static Integer getInteger(String json, String key) {
        JsonElement el = getElement(json, key);
        if(null == el || el.isJsonNull()) return null;
        return elementAsInteger(el, json);
    }

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as Long or <code>null</code> if key doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if target value is not JSON number
     * @throws NumberFormatException if target number is not long
     */
    public static Long getLong(String json, String key) {
        JsonElement el = getElement(json, key);
        if(null == el || el.isJsonNull()) return null;
        return elementAsLong(el, json);
    }

    /**
     * @param json JSON object as string
     * @param key  object mapping key
     * @return value for provided key as BigDecimal or <code>null</code> if key doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if target value is not JSON number
     */
    public static BigDecimal getBigDecimal(String json, String key) {
        JsonElement el = getElement(json, key);
        if(null == el || el.isJsonNull()) return null;
        return elementAsBigDecimal(el, json);
    }

    /**
     * @param json JSON object as string
     * @param key  object mapping key
     * @return value for provided key as Timestamp or <code>null</code> if key doesn't exist,
     * <b>value must be in</b> <code>yyyy-MM-dd HH:mm:ss</code> <b>format</b>
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if target value is not JSON string or if target string has wrong format
     */
    public static Timestamp getTimestamp(String json, String key) {
        String str = getString(json, key);
        if(null == str) return null;
        return stringAsTimestamp(str, json);
    }

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as string array,
     * each string containing string representation of JSON object or null
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if array element is not JSON object
     */
    public static String[] getObjectArray(String json, String key) {
        JsonArray ja = getArray(json, key);
        if(null == ja) return null;
        return arrayAsObjectArray(ja.iterator(), ja.size(), json);
    }

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as string array,
     * each string containing string representation of JSON array or null
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if array element is not JSON object
     */
    public static String[] getMultiArray(String json, String key) {
        JsonArray ja = getArray(json, key);
        if(null == ja) return null;
        return arrayAsMultiArray(ja.iterator(), ja.size(), json);
    }

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as string array
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if array element is not JSON String
     */
    public static String[] getStringArray(String json, String key) {
        JsonArray ja = getArray(json, key);
        if(null == ja) return null;
        return arrayAsStringArray(ja.iterator(), ja.size(), json);
    }

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as boolean array
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if array element is not JSON boolean
     */
    public static Boolean[] getBooleanArray(String json, String key) {
        JsonArray ja = getArray(json, key);
        if(null == ja) return null;
        return arrayAsBooleanArray(ja.iterator(), ja.size(), json);
    }

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as integer array
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if array element is not JSON number
     * @throws NumberFormatException if target number is not integer
     */
    public static Integer[] getIntegerArray(String json, String key) {
        JsonArray ja = getArray(json, key);
        if(null == ja) return null;
        return arrayAsIntegerArray(ja.iterator(), ja.size(), json);
    }


    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as long array
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if array element is not JSON number
     * @throws NumberFormatException if target number is not long
     */
    public static Long[] getLongArray(String json, String key) {
        JsonArray ja = getArray(json, key);
        if(null == ja) return null;
        return arrayAsLongArray(ja.iterator(), ja.size(), json);
    }

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as big decimal array
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if array element is not JSON number
     */
    public static BigDecimal[] getBigDecimalArray(String json, String key) {
        JsonArray ja = getArray(json, key);
        if(null == ja) return null;
        return arrayAsBigDecimalArray(ja.iterator(), ja.size(), json);
    }

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as big decimal array
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if array element is not JSON string or string is not timestamp
     */
    public static Timestamp[] getTimestampArray(String json, String key) {
        JsonArray ja = getArray(json, key);
        if(null == ja) return null;
        return arrayAsTimestampArray(ja.iterator(), ja.size(), json);
    }

    /**
     * @param json JSON array
     * @return string array,each string containing
     *  string representation of JSON object or null
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if array element is not JSON object
     */
    public static String[] toObjectArray(String json) {
        if(null == json) return null;
        List<JsonElement> list = GSON.fromJson(json, LIST_TYPE);
        return arrayAsObjectArray(list.iterator(), list.size(), json);
    }

    /**
     * @param json JSON array
     * @return string array,each string containing
     *  string representation of JSON array or null
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if array element is not JSON array
     */
    public static String[] toMultiArray(String json) {
        if(null == json) return null;
        List<JsonElement> list = GSON.fromJson(json, LIST_TYPE);
        return arrayAsMultiArray(list.iterator(), list.size(), json);
    }

    /**
     * @param json JSON array
     * @return string array
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if array element is not JSON string
     */
    public static String[] toStringArray(String json) {
        if(null == json) return null;
        List<JsonElement> list = GSON.fromJson(json, LIST_TYPE);
        return arrayAsStringArray(list.iterator(), list.size(), json);
    }

    /**
     * @param json JSON array
     * @return boolean array
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if array element is not JSON boolean
     */
    public static Boolean[] toBooleanArray(String json) {
        if(null == json) return null;
        List<JsonElement> list = GSON.fromJson(json, LIST_TYPE);
        return arrayAsBooleanArray(list.iterator(), list.size(), json);
    }

    /**
     * @param json JSON array
     * @return integer array
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if array element is not JSON number
     * @throws NumberFormatException if target number is not integer
     */
    public static Integer[] toIntegerArray(String json) {
        if(null == json) return null;
        List<JsonElement> list = GSON.fromJson(json, LIST_TYPE);
        return arrayAsIntegerArray(list.iterator(), list.size(), json);
    }

    /**
     * @param json JSON array
     * @return long array
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if array element is not JSON number
     * @throws NumberFormatException if target number is not long
     */
    public static Long[] toLongArray(String json) {
        if(null == json) return null;
        List<JsonElement> list = GSON.fromJson(json, LIST_TYPE);
        return arrayAsLongArray(list.iterator(), list.size(), json);
    }

    /**
     * @param json JSON array
     * @return big decimal array
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if array element is not JSON number
     */
    public static BigDecimal[] toBigDecimalArray(String json) {
        if(null == json) return null;
        List<JsonElement> list = GSON.fromJson(json, LIST_TYPE);
        return arrayAsBigDecimalArray(list.iterator(), list.size(), json);
    }

    /**
     * @param json JSON array
     * @return timestamp array
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     * @throws RuntimeException if array element is not JSON string or string is not timestamp
     */
    public static Timestamp[] toTimestampArray(String json) {
        if(null == json) return null;
        List<JsonElement> list = GSON.fromJson(json, LIST_TYPE);
        return arrayAsTimestampArray(list.iterator(), list.size(), json);
    }
}
