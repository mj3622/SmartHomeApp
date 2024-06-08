package com.minjer.smarthome.http;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.minjer.smarthome.utils.ParamUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GaodeClient {
    private static final String TAG = "GaodeClient";

    private static final String GET_CITY_URL = "https://restapi.amap.com/v3/ip";

    private static final String GET_WEATHER_URL = "https://restapi.amap.com/v3/weather/weatherInfo";

    private static final String KEY = "7f79c6e69eee1e7427f7cdb38031ad0b";

    public static final String WEATHER = "weather";
    public static final String TEMPERATURE = "temperature";

    /**
     * 获取城市信息
     *
     * @return 城市信息 原始字符串
     */
    public static String getCity() throws IOException{
        String url = GET_CITY_URL + "?key=" + KEY;
        String response = HttpClient.doGet(url);
        Log.d(TAG, "Response: " + response);
        return response;
    }

    /**
     * 获取天气信息
     *
     * @param cityCode 城市
     * @return 天气信息(已处理过的Map对象)
     */
    public static Map<String, String> getWeather(String cityCode) {
        String url = GET_WEATHER_URL + "?key=" + KEY + "&city=" + cityCode;
        try {
            String response = HttpClient.doGet(url);

            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            JsonArray livesArray = jsonObject.getAsJsonArray("lives");
            JsonObject firstLiveObject = livesArray.get(0).getAsJsonObject();
            String weather = firstLiveObject.get("weather").getAsString();
            String temperature_float = firstLiveObject.get("temperature_float").getAsString();

            Log.d(TAG, "Response: " + response);

            Map<String, String> resMap = new HashMap<>();
            resMap.put(WEATHER, weather);
            resMap.put(TEMPERATURE, temperature_float);

            return resMap;
        } catch (IOException e) {
            Log.e(TAG, "Failed to get weather", e);
            return null;
        }
    }
}
