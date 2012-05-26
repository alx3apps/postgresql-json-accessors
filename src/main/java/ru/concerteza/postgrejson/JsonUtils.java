package ru.concerteza.postgrejson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * User: alexey
 * Date: 5/26/12
 */
public class JsonUtils {
    private static final Gson GSON = new Gson();
    public static final Type MAP_TYPE = new TypeToken<HashMap<String, Object>>() {}.getType();

    public static String jsonArrayGetString(String json, int index) {
        Object[] arr = GSON.fromJson(json, Object[].class);
        if(arr.length <= index) return null;
        if(null == arr[index]) return null;
        return arr[index].toString();
    }

    public static Boolean jsonArrayGetBoolean(String json, int index) {
        Object[] arr = GSON.fromJson(json, Object[].class);
        if(arr.length <= index) return null;
        if(null == arr[index]) return null;
        if(Boolean.class.isAssignableFrom(arr[index].getClass())) return (Boolean) arr[index];
        return Boolean.valueOf(arr[index].toString());
    }

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

    public static Double jsonArrayGetDouble(String json, int index) {
        Object[] arr = GSON.fromJson(json, Object[].class);
        if(arr.length <= index) return null;
        if(null == arr[index]) return null;
        if(Double.class.isAssignableFrom(arr[index].getClass())) return (Double) arr[index];
        return Double.valueOf(arr[index].toString());
    }

    public static String jsonMapGetString(String json, String key) {
        Map<String, Object> map = GSON.fromJson(json, MAP_TYPE);
        Object res = map.get(key);
        return null != res ? res.toString() : null;
    }

    public static Boolean jsonMapGetBoolean(String json, String key) {
        Map<String, Object> map = GSON.fromJson(json, MAP_TYPE);
        Object res = map.get(key);
        if(null == res) return null;
        if(Boolean.class.isAssignableFrom(res.getClass())) return (Boolean) res;
        return Boolean.valueOf(res.toString());
    }

    public static Double jsonMapGetDouble(String json, String key) {
        Map<String, Object> map = GSON.fromJson(json, MAP_TYPE);
        Object res = map.get(key);
        if(null == res) return null;
        if(Double.class.isAssignableFrom(res.getClass())) return (Double) res;
        return Double.valueOf(res.toString());
    }

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
