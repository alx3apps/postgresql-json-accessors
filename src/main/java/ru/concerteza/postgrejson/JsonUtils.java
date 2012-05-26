package ru.concerteza.postgrejson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Functions to access JSON object and array fields. Written on top of <a href="http://code.google.com/p/google-gson/">google-gson</a>
 *
 * @author alexey
 * Date: 5/26/12
 */
public class JsonUtils {
    private static final Gson GSON = new Gson();
    private static final Type MAP_TYPE = new TypeToken<HashMap<String, Object>>() {}.getType();

    /**
     * @param json JSON array as string
     * @param index array index
     * @return value for provided index as String or <code>null</code> if index doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static String jsonArrayGetString(String json, int index) {
        Object[] arr = GSON.fromJson(json, Object[].class);
        if(arr.length <= index) return null;
        if(null == arr[index]) return null;
        return arr[index].toString();
    }

    /**
     * @param json JSON array as string
     * @param index array index
     * @return value for provided index as Boolean or <code>null</code> if index doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static Boolean jsonArrayGetBoolean(String json, int index) {
        Object[] arr = GSON.fromJson(json, Object[].class);
        if(arr.length <= index) return null;
        if(null == arr[index]) return null;
        if(Boolean.class.isAssignableFrom(arr[index].getClass())) return (Boolean) arr[index];
        return Boolean.valueOf(arr[index].toString());
    }

    /**
     * @param json JSON array as string
     * @param index array index
     * @return value for provided index as Long or <code>null</code> if index doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static Long jsonArrayGetLong(String json, int index) {
        Object[] arr = GSON.fromJson(json, Object[].class);
        if(arr.length <= index) return null;
        if(null == arr[index]) return null;
        if(Double.class.isAssignableFrom(arr[index].getClass())) {
            Double doub = (Double) arr[index];
            return doub.longValue();
        }
        return Long.valueOf(arr[index].toString());
    }

    /**
     * @param json JSON array as string
     * @param index array index
     * @return value for provided index as Integer or <code>null</code> if index doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static Integer jsonArrayGetInteger(String json, int index) {
        Object[] arr = GSON.fromJson(json, Object[].class);
        if(arr.length <= index) return null;
        if(null == arr[index]) return null;
        if(Double.class.isAssignableFrom(arr[index].getClass())) {
            Double doub = (Double) arr[index];
            return doub.intValue();
        }
        return Integer.valueOf(arr[index].toString());
    }

    /**
     * @param json JSON array as string
     * @param index array index
     * @return value for provided index as Double or <code>null</code> if index doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static Double jsonArrayGetDouble(String json, int index) {
        Object[] arr = GSON.fromJson(json, Object[].class);
        if(arr.length <= index) return null;
        if(null == arr[index]) return null;
        if(Double.class.isAssignableFrom(arr[index].getClass())) return (Double) arr[index];
        return Double.valueOf(arr[index].toString());
    }

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as String or <code>null</code> if key doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static String jsonMapGetString(String json, String key) {
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
        Map<String, Object> map = GSON.fromJson(json, MAP_TYPE);
        Object res = map.get(key);
        if(null == res) return null;
        if(Boolean.class.isAssignableFrom(res.getClass())) return (Boolean) res;
        return Boolean.valueOf(res.toString());
    }

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as Double or <code>null</code> if key doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static Double jsonMapGetDouble(String json, String key) {
        Map<String, Object> map = GSON.fromJson(json, MAP_TYPE);
        Object res = map.get(key);
        if(null == res) return null;
        if(Double.class.isAssignableFrom(res.getClass())) return (Double) res;
        return Double.valueOf(res.toString());
    }

    /**
     * @param json JSON object as string
     * @param key object mapping key
     * @return value for provided key as Long or <code>null</code> if key doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static Long jsonMapGetLong(String json, String key) {
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
     * @param key object mapping key
     * @return value for provided key as Integer or <code>null</code> if key doesn't exist
     * @throws com.google.gson.JsonSyntaxException on invalid JSON
     */
    public static Integer jsonMapGetInteger(String json, String key) {
        Map<String, Object> map = GSON.fromJson(json, MAP_TYPE);
        Object res = map.get(key);
        if(null == res) return null;
        if(Double.class.isAssignableFrom(res.getClass())) {
            Double doub = (Double) res;
            return doub.intValue();
        }
        return Integer.valueOf(res.toString());
    }
}
