package com.minjer.smarthome.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minjer.smarthome.R;
import com.minjer.smarthome.http.ActionClient;
import com.minjer.smarthome.pojo.Action;
import com.minjer.smarthome.pojo.ActionPack;
import com.minjer.smarthome.pojo.Device;
import com.minjer.smarthome.utils.DeviceUtil;
import com.minjer.smarthome.utils.DialogUtil;
import com.minjer.smarthome.utils.TaskPackUtil;
import com.minjer.smarthome.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;


public class ActionAdapter extends ArrayAdapter<Action> {
    private int resourceLayout;
    private Context mContext;

    public ActionAdapter(Context context, int resource, List<Action> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            view = vi.inflate(resourceLayout, null);
        }

        Action ac = getItem(position);

        if (ac != null) {
            TextView tv_name = view.findViewById(R.id.action_name);
            TextView tv_desc = view.findViewById(R.id.action_description);
            TextView tv_time = view.findViewById(R.id.action_time);

            ArrayList<Device> devices = DeviceUtil.getDeviceList(mContext);
            for (Device device : devices) {
                if (device.getID().equals(ac.getDeviceId())) {
                    if (tv_name != null) {
                        tv_name.setText(device.getName());
                    }
                    break;
                }
            }

            if (tv_desc != null) {
                if (ac.getActionType().equals(Action.ACTION_TYPE_OPEN)) {
                    tv_desc.setText("打开");
                } else {
                    tv_desc.setText("关闭");
                }

                if (tv_time != null) {
                    String time = ac.getTime();
                    String nowMillis = TimeUtil.getNowMillis();
                    if (Long.parseLong(time) < Long.parseLong(nowMillis)) {
                        tv_time.setText("");
                    } else {
                        tv_time.setText(TimeUtil.parseMillis(time).replace(" ", "\n"));
                    }
                }
            }
        }
        return view;
    }
}