package com.minjer.smarthome.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class ParamUtil {
    private static final String PREFS_NAME = "config";
    public static final String E_MAIL = "email";
    public static final String USER_NAME = "name";
    public static final String USER_DESC = "desc";
    public static final String GATEWAT_CODE = "gateway_code";
    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String CITY_CODE = "cityCode";

    public static final String LOGIN_ID = "login_id";
    public static final String LOGIN_PWD = "login_pwd";

    public static final String WEATHER_RESRESH_TIME = "weather_refresh_time";

    public static final String WEATHER_TEMP = "weather_temp";
    public static final String WEATHER_TYPR = "weather_type";

    public static final String MESSAGE_LIST = "message_list";

    public static final String DEVICE_LIST = "device_list";

    public static final String ACTION_PACK_LIST = "action_pack_list";

    public static final String PACK_CREAT_TEMP_LIST = "pack_creat_temp_list";

    public static final String ON_TIME_LIST = "on_time_list";

    // 获取SharedPreferences实例的方法
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // 保存字符串到SharedPreferences
    public static void saveString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    // 从SharedPreferences获取字符串
    public static String getString(Context context, String key, String defaultValue) {
        return getSharedPreferences(context).getString(key, defaultValue);
    }

    // 保存整型到SharedPreferences
    public static void saveInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    // 从SharedPreferences获取整型
    public static int getInt(Context context, String key, int defaultValue) {
        return getSharedPreferences(context).getInt(key, defaultValue);
    }

    // 保存布尔型到SharedPreferences
    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    // 从SharedPreferences获取布尔型
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getSharedPreferences(context).getBoolean(key, defaultValue);
    }

    // 保存浮点型到SharedPreferences
    public static void saveFloat(Context context, String key, float value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    // 从SharedPreferences获取浮点型
    public static float getFloat(Context context, String key, float defaultValue) {
        return getSharedPreferences(context).getFloat(key, defaultValue);
    }

    // 保存长整型到SharedPreferences
    public static void saveLong(Context context, String key, long value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    // 从SharedPreferences获取长整型
    public static long getLong(Context context, String key, long defaultValue) {
        return getSharedPreferences(context).getLong(key, defaultValue);
    }

    // 删除某个键值对
    public static void remove(Context context, String key) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(key);
        editor.apply();
    }

    // 清除所有数据
    public static void clear(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }

    // 获取所有键值对并转换为JSON字符串
    public static String getAll2Json(Context context) {
        Map<String, ?> all = getSharedPreferences(context).getAll();
        return JsonUtil.toJson(all);
    }

    // 获得所有键值对
    public static Map<String, ?> getAll(Context context) {
        return getSharedPreferences(context).getAll();
    }
}
