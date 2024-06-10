package com.minjer.smarthome.deviceFragent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minjer.smarthome.R;

import com.minjer.smarthome.http.ActionClient;
import com.minjer.smarthome.pojo.Action;
import com.minjer.smarthome.pojo.Device;
import com.minjer.smarthome.utils.PageUtil;

import java.util.Map;
import java.util.Random;

public class RadarFragment extends Fragment {
    private static final String TAG = "RadarFragment";
    private Device device;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            device = getArguments().getParcelable("device");
        }
    }

    public static RadarFragment newInstance(Device device) {
        RadarFragment fragment = new RadarFragment();
        Bundle args = new Bundle();
        args.putParcelable("device", device);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_radar, container, false);

        // 获取传感器数据
        Map<String, String> res_map = ActionClient.getRadarData(getContext(), device.getID());
        String exist_people = res_map.get(Device.RADAR_EXIST_PEOPLE);
        String move_object = res_map.get(Device.RADAR_MOVE_OBJECT);
        String static_object = res_map.get(Device.RADAR_STATIC_OBJECT);

        if (exist_people == null) {
            exist_people = "设备状态异常";
        }
        if (move_object == null) {
            move_object = "获取失败";
        }
        if (static_object == null) {
            static_object = "获取失败";
        }

        TextView tv_exist_people = rootView.findViewById(R.id.radar_exist_people);
        TextView tv_move_object = rootView.findViewById(R.id.radar_move_object);
        TextView tv_static_object = rootView.findViewById(R.id.radar_static_object);

        // TODO 接入传感器数据，还要进行数据判别
        if (exist_people.equals("1")) {
            tv_exist_people.setText("检测到附近有人");
        } else {
            tv_exist_people.setText("未检测到附近有人");
        }
        tv_move_object.setText("运动物体的距离：" + move_object + "米");
        tv_static_object.setText("静止物体的距离：" + static_object + "米");

        rootView.findViewById(R.id.radar_refresh).setOnClickListener(v -> {
            PageUtil.refreshPage(getActivity());
        });

        return rootView;
    }

}