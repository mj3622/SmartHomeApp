package com.minjer.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minjer.smarthome.R;
import com.minjer.smarthome.pojo.Device;
import com.minjer.smarthome.pojo.Message;

import java.util.List;

public class DeviceAdapter extends ArrayAdapter<Device> {
    private int resourceLayout;
    private Context mContext;

    public DeviceAdapter(Context context, int resource, List<Device> items) {
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

        Device d = getItem(position);

        if (d != null) {
            TextView titleTextView = view.findViewById(R.id.device_name);
            TextView contentTextView = view.findViewById(R.id.device_desc);
            ImageView imageView = view.findViewById(R.id.device_photo);

            if (titleTextView != null) {
                titleTextView.setText(d.getName());
            }
            if (contentTextView != null) {
                String status = d.getStatus();
                String desc = d.getDescription();
                if (status.equals(Device.STATUS_ON)) {
                    contentTextView.setText("开启 | " + desc);
                } else if (status.equals(Device.STATUS_OFF)) {
                    contentTextView.setText("关闭 | " + desc);
                } else if (status.equals(Device.STATUS_OFFLINE)){
                    contentTextView.setText("离线 | " + desc);
                } else {
                    contentTextView.setText("未知状态 | " + desc);
                }
            }
            if (imageView != null) {
                String type = d.getType();
                if (type.equals(Device.TYPE_LIGHT)) {
                    imageView.setImageResource(R.drawable.jiazhuangjiajuleimu);
                } else if (type.equals(Device.TYPE_SWITCH)) {
                    imageView.setImageResource(R.drawable.kaiguan);
                } else if (type.equals(Device.TYPE_SENSOR_LIGHT)) {
                    imageView.setImageResource(R.drawable.guangzhaochuanganqi);
                } else if (type.equals(Device.TYPE_SENSOR_TEMP)) {
                    imageView.setImageResource(R.drawable.wenshiduchuanganqi_o);
                } else if (type.equals(Device.TYPE_RADAR)) {
                    imageView.setImageResource(R.drawable.leidatance);
                } else if (type.equals(Device.TYPE_CURTAIN)) {
                    imageView.setImageResource(R.drawable.chuanglian);
                }
            }
        }

        return view;
    }
}
