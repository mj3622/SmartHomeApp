package com.minjer.smarthome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.minjer.smarthome.adapter.DeviceAdapter;
import com.minjer.smarthome.http.DataClient;
import com.minjer.smarthome.http.GaodeClient;
import com.minjer.smarthome.pojo.Device;
import com.minjer.smarthome.pojo.Message;
import com.minjer.smarthome.utils.DeviceUtil;
import com.minjer.smarthome.utils.DialogUtil;
import com.minjer.smarthome.utils.JsonUtil;
import com.minjer.smarthome.utils.MessageUtil;
import com.minjer.smarthome.utils.PageUtil;
import com.minjer.smarthome.utils.ParamUtil;
import com.minjer.smarthome.utils.TimeUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DeviceFragment extends Fragment {

    private ListView deviceListView;
    private LinearLayout emptyView;
    private List<Device> deviceList;
    private DeviceAdapter deviceAdapter;

    private final static int REQUEST_CODE_MODIFY_DEVICE = 1;

    private static final String TAG = "DeviceFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device, container, false);

        // 初始化天气数据
        initWeather(rootView);

        // 给消息按钮添加点击事件
        rootView.findViewById(R.id.system_info).setOnClickListener(v -> {
            // 跳转到消息页面
            Intent intent = new Intent(getContext(), MessageBoxActivity.class);
            startActivity(intent);
        });

        // 给刷新按钮添加点击事件
        rootView.findViewById(R.id.refresh_device_status).setOnClickListener(v -> {
            // 同步设备信息
            if (ParamUtil.getBoolean(getContext(), ParamUtil.UPDATE_DEVICE_STATUS, false)) {
                try {
                    DeviceUtil.syncDeviceStatus(getContext());
                    PageUtil.refreshPage(getActivity());
                } catch (Exception e) {
                    DialogUtil.showToastShort(getContext(), "网络连接失败");
                    Log.e(TAG, "Failed to sync device status", e);
                }
            } else {
                DialogUtil.showToastShort(getContext(), "未开启同步功能");
            }
        });

        // 初始化设备列表
        initDeviceList(rootView);

        // Inflate the layout for this fragment
        return rootView;
    }

    private void initDeviceList(View rootView) {
        deviceListView = rootView.findViewById(R.id.device_list);
        emptyView = rootView.findViewById(R.id.device_empty);


        deviceList = DeviceUtil.getDeviceList(getContext());
        deviceAdapter = new DeviceAdapter(getContext(), R.layout.item_device, deviceList);
        deviceListView.setAdapter(deviceAdapter);

        checkDeviceList();

        deviceListView.setOnItemClickListener((parent, view, position, id) -> {
            Device device = deviceList.get(position);

            if (ParamUtil.getBoolean(getContext(), ParamUtil.UPDATE_DEVICE_STATUS, false) && !Objects.equals(device.getStatus(), Device.STATUS_ONLINE)) {
                Log.d(TAG, "Click device: " + device.getName() + " 但是设备不在线");
                DialogUtil.showToastShort(getContext(), "未连接到设备");
            } else {

                Log.d(TAG, "Click device: " + device.getName());
                Intent intent = new Intent(getContext(), BaseDeviceActivity.class);
                String deviceJson = JsonUtil.toJson(device);
                intent.putExtra("device", deviceJson);
                startActivityForResult(intent, REQUEST_CODE_MODIFY_DEVICE);
            }

        });
    }

    private void checkDeviceList() {
        if (deviceList.isEmpty()) {
            deviceListView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            deviceListView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    // 初始化天气信息
    private void initWeather(View rootView) {
        TextView weatherInfo = rootView.findViewById(R.id.weather_info);
        ImageView weatherIcon = rootView.findViewById(R.id.weather_pic);

        TextView scene_hint = rootView.findViewById(R.id.scene_hint);
        scene_hint.setText(ParamUtil.getString(getContext(), ParamUtil.USER_NAME, "未知用户") + " 的家");


        // 获取城市信息
        String code = ParamUtil.getString(getContext(), ParamUtil.CITY, null);
        if (code == null) {
            String cityInfo = null;
            try {
                cityInfo = GaodeClient.getCity();
            } catch (IOException e) {
                Log.e(TAG, "Failed to get city info", e);
                DialogUtil.showToastShort(getContext(), "网络连接失败");
                return;
            }
            Map<String, Object> result = JsonUtil.parseToMap(cityInfo);
            String province = (String) result.get("province");
            String city = (String) result.get("city");
            String cityCode = (String) result.get("adcode");

            // 设置城市信息
            ParamUtil.saveString(getContext(), ParamUtil.PROVINCE, province);
            ParamUtil.saveString(getContext(), ParamUtil.CITY, city);
            ParamUtil.saveString(getContext(), ParamUtil.CITY_CODE, cityCode);
            code = cityCode;
        }

        Boolean needRefresh = TimeUtil.needResreshWeather(getContext());
        Log.d("DeviceFragment", "Need refresh: " + needRefresh);
        if (needRefresh) {
            // 获取天气信息
            Map<String, String> weather = GaodeClient.getWeather(code);
            String tempWeatherType = weather.get(GaodeClient.WEATHER);
            String tempTemperature = weather.get(GaodeClient.TEMPERATURE);

            // 保存刷新时间
            ParamUtil.saveString(getContext(), ParamUtil.WEATHER_RESRESH_TIME, TimeUtil.getNowTime());

            // 保存天气信息
            ParamUtil.saveString(getContext(), ParamUtil.WEATHER_TYPR, tempWeatherType);
            ParamUtil.saveString(getContext(), ParamUtil.WEATHER_TEMP, tempTemperature);
        }

        String weatherType = ParamUtil.getString(getContext(), ParamUtil.WEATHER_TYPR, "晴");
        String temperature = ParamUtil.getString(getContext(), ParamUtil.WEATHER_TEMP, "25");

        weatherInfo.setText(weatherType + " " + temperature + "℃");

        // 设置天气图标
        if (weatherType.contains("云")) {
            weatherIcon.setImageResource(R.drawable.qingzhuanduoyun);
        } else if (weatherType.contains("晴")) {
            weatherIcon.setImageResource(R.drawable.qingtian);
        } else if (weatherType.contains("雨")) {
            weatherIcon.setImageResource(R.drawable.dayu);
        } else if (weatherType.contains("雪")) {
            weatherIcon.setImageResource(R.drawable.daxue);
        } else if (weatherType.contains("雾")) {
            weatherIcon.setImageResource(R.drawable.youwu);
        } else if (weatherType.contains("霾")) {
            weatherIcon.setImageResource(R.drawable.shachenbao);
        } else if (weatherType.contains("沙") || weatherType.contains("尘") || weatherType.contains("风")) {
            weatherIcon.setImageResource(R.drawable.feng);
        } else {
            weatherIcon.setImageResource(R.drawable.yintian);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, requestCode + " " + resultCode);
        if (requestCode == REQUEST_CODE_MODIFY_DEVICE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "Update device list");
            PageUtil.refreshPage(getActivity());
        }
    }
}