package ru.concerteza.postgrejson;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Functions to access JSON fields. Written on top of <a href="http://code.google.com/p/google-gson/">google-gson</a>
 *
 * @author alexey
 * Date: 5/26/12
 */
public class JsonUtils {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Timestamp.class, new TimestampDeserializer())
            .create();
    private static final Type MAP_TYPE = new TypeToken<HashMap<String, Object>>() {}.getType();

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as String or <code>null</code> if key doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static String jsonMapGetString(String json, String key) {
        if(null == json || null == key) return null;
        Map<String, Object> map = GSON.fromJson(json, MAP_TYPE);
        Object res = map.get(key);
        return null != res ? res.toString() : null;
    }

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as Boolean or <code>null</code> if key doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static Boolean jsonMapGetBoolean(String json, String key) {
        if(null == json || null == key) return null;
        Map<String, Object> map = GSON.fromJson(json, MAP_TYPE);
        Object res = map.get(key);
        if(null == res) return null;
        if(Boolean.class.isAssignableFrom(res.getClass())) return (Boolean) res;
        return Boolean.valueOf(res.toString());
    }

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as Integer or <code>null</code> if key doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static Integer jsonMapGetInteger(String json, String key) {
        if(null == json || null == key) return null;
        Map<String, Object> map = GSON.fromJson(json, MAP_TYPE);
        Object res = map.get(key);
        if(null == res) return null;
        if(Double.class.isAssignableFrom(res.getClass())) {
            Double doub = (Double) res;
            return doub.intValue();
        }
        return Integer.valueOf(res.toString());
    }

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as Long or <code>null</code> if key doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static Long jsonMapGetLong(String json, String key) {
        if(null == json || null == key) return null;
        Map<String, Object> map = GSON.fromJson(json, MAP_TYPE);
        Object res = map.get(key);
        if(null == res) return null;
        if(Double.class.isAssignableFrom(res.getClass())) {
            Double doub = (Double) res;
            return doub.longValue();
        }
        return Long.valueOf(res.toString());
    }

    /**
     * @param json JSON object as string
     * @param key  object mapping key
     * @return value for provided key as BigDecimal or <code>null</code> if key doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static BigDecimal jsonMapGetBigDecimal(String json, String key) {
        if(null == json || null == key) return null;
        Map<String, Object> map = GSON.fromJson(json, MAP_TYPE);
        Object res = map.get(key);
        if(null == res) return null;
        if(BigDecimal.class.isAssignableFrom(res.getClass())) return (BigDecimal) res;
        return new BigDecimal(res.toString());
    }

    /**
     * @param json JSON object as string
     * @param key  object mapping key
     * @return value for provided key as Timestamp or <code>null</code> if key doesn't exist,
     * <b>value must be in</b> <code>yyyy-MM-dd HH:mm:ss</code> <b>format</b>
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static Timestamp jsonMapGetTimestamp(String json, String key) {
        if(null == json || null == key) return null;
        Map<String, Object> map = GSON.fromJson(json, MAP_TYPE);
        Object res = map.get(key);
        if(null == res) return null;
        return Timestamp.valueOf(res.toString());
    }

    /**
     * @param json array as string
     * @return String array
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static String[] jsonArrayToStringArray(String json) {
        if(null == json) return null;
        return GSON.fromJson(json, String[].class);
    }

    /**
     * @param json array as string
     * @return Boolean array
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static Boolean[] jsonArrayToBooleanArray(String json) {
        if(null == json) return null;
        return GSON.fromJson(json, Boolean[].class);
    }

    /**
     * @param json array as string
     * @return Integer array
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static Integer[] jsonArrayToIntegerArray(String json) {
        if(null == json) return null;
        return GSON.fromJson(json, Integer[].class);
    }

    /**
     * @param json array as string
     * @return Long array
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static Long[] jsonArrayToLongArray(String json) {
        if(null == json) return null;
        return GSON.fromJson(json, Long[].class);
    }

    /**
     * @param json array as string
     * @return BigDecimal array
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static BigDecimal[] jsonArrayToBigDecimalArray(String json) {
        if(null == json) return null;
        return GSON.fromJson(json, BigDecimal[].class);
    }

    /**
     * @param json array as string
     * @return Timestamp array, <b>values must be in</b> <code>yyyy-MM-dd HH:mm:ss</code> <b>format</b>
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static Timestamp[] jsonArrayToTimestampArray(String json) {
        if(null == json) return null;
        return GSON.fromJson(json, Timestamp[].class);
    }

    private static class TimestampDeserializer implements JsonDeserializer<Timestamp> {
        @Override
        public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            return Timestamp.valueOf(json.getAsString());
        }
    }

}
