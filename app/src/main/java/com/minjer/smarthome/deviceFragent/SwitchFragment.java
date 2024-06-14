package com.minjer.smarthome.deviceFragent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minjer.smarthome.R;
import com.minjer.smarthome.http.ActionClient;
import com.minjer.smarthome.pojo.Action;
import com.minjer.smarthome.pojo.Device;
import com.minjer.smarthome.utils.DialogUtil;
import com.minjer.smarthome.utils.TimeUtil;

public class SwitchFragment extends Fragment {
    private static final String TAG = "SwitchFragment";
    private Device device;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            device = getArguments().getParcelable("device");
        }
    }

    public static SwitchFragment newInstance(Device device) {
        SwitchFragment fragment = new SwitchFragment();
        Bundle args = new Bundle();
        args.putParcelable("device", device);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_switch, container, false);

        initButton(rootView);

        TextView tv_status = rootView.findViewById(R.id.switch_status);

        String status = ActionClient.getSwitchStatus(this.getContext(), device.getID());

        if (status.equals("1")) {
            tv_status.setText("开启");
        } else if (status.equals("0")) {
            tv_status.setText("关闭");
        } else {
            tv_status.setText("未知");
        }

        tv_status.setOnClickListener(v -> {
            String status1 = ActionClient.getSwitchStatus(this.getContext(), device.getID());
            if (status1.equals("1")) {
                tv_status.setText("开启");
            } else if (status1.equals("0")) {
                tv_status.setText("关闭");
            } else {
                tv_status.setText("未知");
            }
            DialogUtil.showToastShort(getContext(), "刷新成功");
        });

        return rootView;
    }

    private void initButton(View rootView) {
        // 打开开关
        rootView.findViewById(R.id.switch_btn_on).setOnClickListener(v -> {
            Action action = new Action(device.getID(), Action.ACTION_TYPE_OPEN, TimeUtil.getNowMillis(), Device.TYPE_SWITCH);
            ActionClient.sendAction(getContext(), action);
            Log.d(TAG, "Action:" + action.toString());
        });

        // 关闭开关
        rootView.findViewById(R.id.switch_btn_off).setOnClickListener(v -> {
            Action action = new Action(device.getID(), Action.ACTION_TYPE_CLOSE, TimeUtil.getNowMillis(), Device.TYPE_SWITCH);
            ActionClient.sendAction(getContext(), action);
            Log.d(TAG, "Action:" + action.toString());
        });
    }
}