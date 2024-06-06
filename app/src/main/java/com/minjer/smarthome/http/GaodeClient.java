package com.minjer.smarthome.http;

import android.util.Log;

import java.io.IOException;

public class GaodeClient {
    private static final String TAG = "GaodeClient";

    private static final String GET_CITY_URL = "https://restapi.amap.com/v3/ip";

    private static final String KEY = "7f79c6e69eee1e7427f7cdb38031ad0b";


    public static String getCity() {
        String url = GET_CITY_URL + "?key=" + KEY;
        try {
            String response = HttpClient.doGet(url);
            Log.d(TAG, "Response: " + response);
            return response;
        } catch (IOException e) {
            Log.e(TAG, "Failed to get city", e);
            return null;
        }
    }
}
