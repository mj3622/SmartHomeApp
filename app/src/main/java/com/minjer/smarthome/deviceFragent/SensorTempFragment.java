package com.minjer.smarthome.deviceFragent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minjer.smarthome.R;
import com.minjer.smarthome.http.ActionClient;
import com.minjer.smarthome.pojo.Device;

import java.util.Map;

public class SensorTempFragment extends Fragment {
    private static final String TAG = "LightFragment";
    private Device device;

    Map<String,String> tempAndHumidityMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            device = getArguments().getParcelable("device");
        }
    }

    public static SensorTempFragment newInstance(Device device) {
        SensorTempFragment fragment = new SensorTempFragment();
        Bundle args = new Bundle();
        args.putParcelable("device", device);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sensor_temp, container, false);

        tempAndHumidityMap = ActionClient.getTempertureAndHumidity(getContext(),device.getID());

        initTemperture(rootView);

        initHumidity(rootView);

        return rootView;
    }

    private void initHumidity(View rootView) {
        TextView tv_humidity = rootView.findViewById(R.id.sensor_value_humidity);
        TextView tv_humidity_hint = rootView.findViewById(R.id.sensor_humidity_hint);

        String humidity = tempAndHumidityMap.get(Device.HUMIDITY);

        if (tempAndHumidityMap != null && humidity != null) {
            tv_humidity.setText(humidity + "%");
            // 湿度提示
            if (Float.parseFloat(humidity) < 40) {
                tv_humidity_hint.setText("室内相对干燥\n建议使用加湿器或多喝水保持舒适🌬️");
            } else if (Float.parseFloat(humidity) > 70) {
                tv_humidity_hint.setText("室内湿度较高\n注意通风或使用除湿器🌧️");
            } else {
                tv_humidity_hint.setText("室内湿度适宜😌");
            }

        } else {
            tv_humidity.setText(Device.UNKNOW_HUMIDITY);
        }

    }

    private void initTemperture(View rootView) {

        TextView tv_temperture = rootView.findViewById(R.id.sensor_value_temperature);
        TextView tv_temperture_hint = rootView.findViewById(R.id.sensor_temperature_hint);

        String temperture = tempAndHumidityMap.get(Device.TEMPERATURE);

        if (tempAndHumidityMap != null && temperture != null) {
            tv_temperture.setText(temperture + "℃");
            // 温度提示
            if (Float.parseFloat(temperture) < 10) {
                tv_temperture_hint.setText("室内温度较低\n注意保暖🧣");
            } else if (Float.parseFloat(temperture) > 30) {
                tv_temperture_hint.setText("室内温度较高\n注意通风或使用空调❄️");
            } else {
                tv_temperture_hint.setText("室内温度适宜😌");
            }
        } else {
            tv_temperture.setText(Device.UNKNOW_TEMPERATURE);
        }


    }

}