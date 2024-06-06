package com.minjer.smarthome.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    private static final Gson GSON = new Gson();

    /**
     * 将 JSON 字符串解析为 Map
     *
     * @param json JSON 字符串
     * @return 解析后的 Map
     */
    public static Map<String, Object> parseToMap(String json) {
        JsonObject jsonObject = GSON.fromJson(json, JsonObject.class);
        return convertJsonToMap(jsonObject);
    }

    /**
     * 将 JSON 对象解析为 Map
     *
     * @param jsonObject JSON 对象
     * @return 解析后的 Map
     */
    public static Map<String, Object> convertJsonToMap(JsonObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (value.isJsonObject()) {
                map.put(key, convertJsonToMap(value.getAsJsonObject()));
            } else if (value.isJsonArray()) {
                map.put(key, GSON.fromJson(value, Object[].class));
            } else {
                map.put(key, GSON.fromJson(value, Object.class));
            }
        }
        return map;
    }

    /**
     * 将 JSON 字符串解析为指定的 Java 对象
     *
     * @param json  JSON 字符串
     * @param clazz 目标 Java 对象的类型
     * @param <T>   目标 Java 对象的类型
     * @return 解析后的 Java 对象
     */
    public static <T> T parseToObject(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    /**
     * 将 JSON 字符串解析为指定的 Java 对象
     *
     * @param json  JSON 字符串
     * @param type  目标 Java 对象的类型
     * @param <T>   目标 Java 对象的类型
     * @return 解析后的 Java 对象
     */
    public static <T> T parseToObject(String json, Type type) {
        return GSON.fromJson(json, type);
    }

    /**
     * 将 Java 对象转换为 JSON 字符串
     *
     * @param object Java 对象
     * @return JSON 字符串
     */
    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

}