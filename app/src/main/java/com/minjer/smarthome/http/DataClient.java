package com.minjer.smarthome.http;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.minjer.smarthome.utils.DialogUtil;
import com.minjer.smarthome.utils.JsonUtil;
import com.minjer.smarthome.utils.ParamUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataClient {

    private static final String TAG = "DataClient";
    private static final String BASE_URL = "http://116.205.244.2:12277";


    // 数据与云端服务器进行同步 等待测试
    public static void syncData(Context context) {
        String url = BASE_URL + "/download";

        String userId = ParamUtil.getString(context,ParamUtil.LOGIN_ID,null);
        JsonObject params = new JsonObject();
        params.addProperty("user_id", userId);

        try {
            String res = HttpClient.doJsonPost(url, params, null);
            if (res == null) {
                DialogUtil.showToastShort(null, "网络错误");
                return;
            }
            Log.d(TAG, "Response: " + res);
            Map<String, Object> resMap = JsonUtil.parseToMap(res);
            Log.d(TAG, "Response Map: " + resMap);

            // 写入本地数据库
            if (resMap.get("city") != null) ParamUtil.saveString(context, ParamUtil.CITY, Objects.requireNonNull(resMap.get("city")).toString());
            if (resMap.get("cityCode") != null) ParamUtil.saveString(context, ParamUtil.CITY_CODE, Objects.requireNonNull(resMap.get("cityCode")).toString());
            if (resMap.get("province") != null) ParamUtil.saveString(context, ParamUtil.PROVINCE, Objects.requireNonNull(resMap.get("province")).toString());
            if (resMap.get("gateway_code") != null) ParamUtil.saveString(context, ParamUtil.GATEWAT_CODE, Objects.requireNonNull(resMap.get("gateway_code")).toString());
            if (resMap.get("name") != null) ParamUtil.saveString(context, ParamUtil.USER_NAME, Objects.requireNonNull(resMap.get("name")).toString());
            if (resMap.get("email") != null) ParamUtil.saveString(context, ParamUtil.E_MAIL, Objects.requireNonNull(resMap.get("email")).toString());
            if (resMap.get("desc") != null) ParamUtil.saveString(context, ParamUtil.USER_DESC, Objects.requireNonNull(resMap.get("desc")).toString());



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // TODO 上传数据到云端服务器

    public static void uploadData(Context context) {
        String url = BASE_URL + "/upload";

        JsonObject params = new JsonObject();
        params.addProperty("user_id", ParamUtil.getString(context,ParamUtil.LOGIN_ID,null));
        params.addProperty("city", ParamUtil.getString(context,ParamUtil.CITY,""));
        params.addProperty("city_code", ParamUtil.getString(context,ParamUtil.CITY_CODE,""));
        params.addProperty("province", ParamUtil.getString(context,ParamUtil.PROVINCE,""));
        params.addProperty("gateway_code", ParamUtil.getString(context,ParamUtil.GATEWAT_CODE,""));
        params.addProperty("name", ParamUtil.getString(context,ParamUtil.USER_NAME,""));
        params.addProperty("email", ParamUtil.getString(context,ParamUtil.E_MAIL,""));
        params.addProperty("desc", ParamUtil.getString(context,ParamUtil.USER_DESC,""));

        try {
            String res = HttpClient.doJsonPost(url, params, null);
            if (res == null) {
                DialogUtil.showToastShort(null, "网络错误");
                return;
            }
            Log.d(TAG, "Response: " + res);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // 判断用户账号密码是否正确
    public static Boolean loginCheck(String inputUserId, String inputPassword) {
        JsonObject params = new JsonObject();
        params.addProperty("login_id", inputUserId);
        params.addProperty("login_pwd", inputPassword);
        try {
            String res = HttpClient.doJsonPost(BASE_URL + "/login", params, null);
            if (res == null) {
                DialogUtil.showToastShort(null, "网络错误");
                return false;
            }
            Log.d(TAG, "Response: " + res);

            Map<String, Object> resMap = JsonUtil.parseToMap(res);
            Log.d(TAG, "Response Map: " + resMap);
            String isValid = Objects.requireNonNull(resMap.get("isValid")).toString().substring(0, 1);

            return isValid.equals("1");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
