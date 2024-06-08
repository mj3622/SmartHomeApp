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

import java.util.List;

public class ActionPackAdapter extends ArrayAdapter<ActionPack> {
    private int resourceLayout;
    private Context mContext;

    public ActionPackAdapter(Context context, int resource, List<ActionPack> items) {
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

        ActionPack ap = getItem(position);

        if (ap != null) {
            TextView titleTextView = view.findViewById(R.id.taskpack_title);
            TextView descTextView = view.findViewById(R.id.taskpack_desc);
            TextView listTextView = view.findViewById(R.id.taskpack_list);
            ImageView imageView = view.findViewById(R.id.taskpack_delete_button);
            ImageView imageView2 = view.findViewById(R.id.taskpack_run_button);

            if (titleTextView != null) {
                titleTextView.setText(ap.getPackName());
            }
            if (descTextView != null) {
                descTextView.setText(ap.getPackDescription());
            }
            if (listTextView != null) {
                List<Action> actions = ap.getActions();
                StringBuilder sb = new StringBuilder();
                List<Device> devices = DeviceUtil.getDeviceList(mContext);
                for (Action action : actions) {
                    sb.append(action.getActionType()).append(" ");
                    for (Device device : devices) {
                        if (device.getID().equals(action.getDeviceId())) {
                            sb.append(device.getName()).append("\n");
                            break;
                        }
                    }
                }
                listTextView.setText(sb.toString());
            }
            if (imageView != null) {
                imageView.setOnClickListener(v -> {
                    // 删除
                    new AlertDialog.Builder(mContext)
                            .setTitle("删除调度")
                            .setMessage("您确认要删除任务调度" + ap.getPackName() + "吗？")
                            .setPositiveButton("确认", (dialog, which) -> {
                                remove(ap);
                                TaskPackUtil.removeActionPack(mContext, ap);
                                notifyDataSetChanged();
                            })
                            .setNegativeButton("取消", null)
                            .show();
                });
            }
            if (imageView2 != null) {
                imageView2.setOnClickListener(v -> {
                    // 弹窗确认是否执行任务
                    new AlertDialog.Builder(mContext)
                            .setTitle("执行调度")
                            .setMessage("您确认要执行任务调度 " + ap.getPackName() + " 吗？")
                            .setPositiveButton("确认", (dialog, which) -> {
                                ActionClient.executeActionPack(getContext(), ap);
                                DialogUtil.showToastShort(mContext, "任务调度已执行");
                            })
                            .setNegativeButton("取消", null)
                            .show();
                });
            }

        }

        return view;
    }
}
