package com.minjer.smarthome;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.minjer.smarthome.http.DataClient;
import com.minjer.smarthome.http.GaodeClient;
import com.minjer.smarthome.pojo.Message;
import com.minjer.smarthome.utils.DialogUtil;
import com.minjer.smarthome.utils.JsonUtil;
import com.minjer.smarthome.utils.MessageUtil;
import com.minjer.smarthome.utils.ParamUtil;
import com.minjer.smarthome.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Map;

public class DeviceFragment extends Fragment {

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

        // TODO 测试用 暂时保留
         rootView.findViewById(R.id.button_test).setOnClickListener(v -> {
             MessageUtil.addMessage(this.getContext(),new Message("测试消息", "这是一条测试消息", "2021-09-01 12:00:00"));
             ArrayList<Message> messageList = MessageUtil.getMessageList(this.getContext());
                Log.d("DeviceFragment", "Message list: " + messageList);
         });

        // Inflate the layout for this fragment
        return rootView;
    }

    // 初始化天气信息
    private void initWeather(View rootView) {
        TextView weatherInfo = rootView.findViewById(R.id.weather_info);
        ImageView weatherIcon = rootView.findViewById(R.id.weather_pic);

        // 获取城市信息
        String code = ParamUtil.getString(getContext(), ParamUtil.CITY, null);
        if (code == null) {
            String cityInfo = GaodeClient.getCity();
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
        }
        else if (weatherType.contains("晴")) {
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
}