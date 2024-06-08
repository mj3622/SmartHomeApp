package com.minjer.smarthome;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.minjer.smarthome.adapter.ActionAdapter;
import com.minjer.smarthome.http.ActionClient;
import com.minjer.smarthome.pojo.Action;
import com.minjer.smarthome.pojo.Device;
import com.minjer.smarthome.utils.ActionUtil;
import com.minjer.smarthome.utils.DeviceUtil;
import com.minjer.smarthome.utils.DialogUtil;
import com.minjer.smarthome.utils.TimeUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class OnTimeActivity extends AppCompatActivity {

    LinearLayout emptyView;
    ListView onTimeActionListView;
    ArrayList<Action> actions = new ArrayList<>();
    ActionAdapter actionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_time);

        ActionUtil.clearOutTimeAction(this);

        onTimeActionListView = findViewById(R.id.ontimelist_item_list);
        emptyView = findViewById(R.id.ontimelist_empty);
        actions = ActionUtil.getOnTimeList(this);
        actionAdapter = new ActionAdapter(this, R.layout.item_action, actions);
        onTimeActionListView.setAdapter(actionAdapter);

        onTimeActionListView.setOnItemClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(this)
                    .setTitle("删除行为")
                    .setMessage("确定删除该行为吗？")
                    .setPositiveButton("确定", (dialog, which) -> {
                        Action action = actions.get(position);
                        ActionUtil.removeOnTime(this, action);
                        actions.remove(position);
                        actionAdapter.notifyDataSetChanged();
                        checkEmptyView();
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });

        checkEmptyView();

        initTopbar();

    }

    private void checkEmptyView() {
        if (actions.isEmpty()) {
            emptyView.setVisibility(LinearLayout.VISIBLE);
            onTimeActionListView.setVisibility(ListView.GONE);
        } else {
            emptyView.setVisibility(LinearLayout.GONE);
            onTimeActionListView.setVisibility(ListView.VISIBLE);
        }
    }

    private void initTopbar() {
        findViewById(R.id.ontimelist_go_back_button).setOnClickListener(v -> finish());

        ArrayList<Device> devices = DeviceUtil.getDeviceList(this);

        findViewById(R.id.ontimelist_add_button).setOnClickListener(v -> {
            LinearLayout createActionView = new LinearLayout(this);
            createActionView.setPadding(50, 50, 50, 50);
            createActionView.setOrientation(LinearLayout.VERTICAL);
            // 创建名字选择栏
            Spinner nameSpinner = new Spinner(this);
            nameSpinner.setPadding(0, 0, 0, 50);
            ArrayList<String> deviceName = new ArrayList<>();
            for (Device device : devices) {
                if (DeviceUtil.isControlDevice(device.getType())) {
                    deviceName.add(device.getName());
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, deviceName.toArray(new String[0]));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            nameSpinner.setAdapter(adapter);
            createActionView.addView(nameSpinner);

            // 创建行为选择栏
            Spinner actionSpinner = new Spinner(this);
            ArrayAdapter<String> actionTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"打开", "关闭"});
            actionSpinner.setAdapter(actionTypeAdapter);
            actionSpinner.setPadding(0, 0, 0, 50);
            createActionView.addView(actionSpinner);

            TimePicker timePicker = new TimePicker(this);
            timePicker.setIs24HourView(true);
            createActionView.addView(timePicker);

            // 创建添加行为
            new AlertDialog.Builder(this)
                    .setTitle("添加行为")
                    .setView(createActionView)
                    .setPositiveButton("确定", (dialog, which) -> {
                        String name = (String) nameSpinner.getSelectedItem();
                        String action = (String) actionSpinner.getSelectedItem();

                        int plusDay = 0;
                        if (timePicker.getHour() < LocalDateTime.now().getHour() || (timePicker.getHour() == LocalDateTime.now().getHour() && timePicker.getMinute() < LocalDateTime.now().getMinute())) {
                            plusDay = 1;
                        }
                        LocalDateTime selectedTime = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), timePicker.getHour(), timePicker.getMinute());
                        selectedTime = selectedTime.plusDays(plusDay);


                        Device device = null;
                        for (Device d : devices) {
                            if (d.getName().equals(name)) {
                                device = d;
                                break;
                            }
                        }
                        if (device == null) {
                            DialogUtil.showToastShort(this, "设备不存在");
                            return;
                        }
                        Action newAction = new Action();
                        newAction.setDeviceId(device.getID());
                        newAction.setDeviceType(device.getType());
                        newAction.setTime(TimeUtil.getMillis(selectedTime));
                        newAction.setActionType(action.equals("打开") ? Action.ACTION_TYPE_OPEN : Action.ACTION_TYPE_CLOSE);

                        ActionUtil.addToOnTimeList(this, newAction);
                        ActionClient.sendAction(this, newAction);
                        actions.add(newAction);
                        actionAdapter.notifyDataSetChanged();
                        checkEmptyView();
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });
    }

}