package com.minjer.smarthome;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.minjer.smarthome.dialog.ControlDialog;
import com.minjer.smarthome.http.ActionClient;
import com.minjer.smarthome.pojo.Action;
import com.minjer.smarthome.pojo.Device;
import com.minjer.smarthome.pojo.Message;
import com.minjer.smarthome.utils.DeviceUtil;
import com.minjer.smarthome.utils.DialogUtil;
import com.minjer.smarthome.utils.JsonUtil;
import com.minjer.smarthome.utils.MessageUtil;
import com.minjer.smarthome.utils.ParamUtil;
import com.minjer.smarthome.utils.TimeUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        findViewById(R.id.debug_add_message).setOnClickListener(v -> {
            String time = TimeUtil.getNowTime();
            MessageUtil.addMessage(this, new Message("测试消息", "现在是" + time, TimeUtil.getNowDay()));
            DialogUtil.showToastShort(this, "添加消息成功");
        });

        findViewById(R.id.debug_add_device).setOnClickListener(v -> {
            Device device = DeviceUtil.randomGenerateDevice();
            DeviceUtil.addDevice(this, device);
            DialogUtil.showToastShort(this, "添加设备成功");
        });

        findViewById(R.id.debug_clear_device).setOnClickListener(v -> {
            DeviceUtil.clearDeviceList(this);
            DialogUtil.showToastShort(this, "清空设备成功");
        });

        findViewById(R.id.debug_rabbitmq).setOnClickListener(v -> {
            Action action = new Action(Device.RASPBERRY_ID, Action.ACTION_TYPE_OPEN, TimeUtil.getNowMillis(), Device.TYPE_RASPBERRY, "switch");
            ActionClient.sendAction(this, action);
            Log.d("DebugActivity", "发送消息成功");
        });

        findViewById(R.id.debug_add_custom_device).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("添加自定义设别");

            // Create the layout for the dialog
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(16, 16, 16, 16);

            // Add an EditText for the device name
            EditText deviceNameEditText = new EditText(this);
            deviceNameEditText.setHint("Device Id");
            layout.addView(deviceNameEditText);

            // Add a Spinner for the device type selection
            Spinner deviceTypeSpinner = new Spinner(this);
            // Populate the Spinner with your device type options
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{Device.TYPE_LIGHT, Device.TYPE_SWITCH, Device.TYPE_SENSOR_LIGHT, Device.TYPE_SENSOR_TEMP, Device.TYPE_RADAR, Device.TYPE_CURTAIN});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            deviceTypeSpinner.setAdapter(adapter);
            layout.addView(deviceTypeSpinner);

            // Set the custom layout to the dialog
            builder.setView(layout);

            // Add buttons for the dialog
            builder.setPositiveButton("Add", (dialog, which) -> {
                // Retrieve the user input from the EditText and Spinner
                String deviceName = deviceNameEditText.getText().toString();
                String deviceType = (String) deviceTypeSpinner.getSelectedItem();

                Device device = new Device("自定义设备", deviceName, deviceType, Device.STATUS_OFF, "自定义设备");
                DeviceUtil.addDevice(this, device);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // Dismiss the dialog
            });

            // Show the dialog
            builder.show();
        });

        findViewById(R.id.debug_change_device_status).setOnClickListener(v -> {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(16, 16, 16, 16);

            // 选择设备ID
            Spinner deviceSpinner = new Spinner(this);
            ArrayList<Device> deviceList = DeviceUtil.getDeviceList(this);

            ArrayList<String> deviceIds = new ArrayList<>();
            for (Device device : deviceList) {
                deviceIds.add(device.getID());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, deviceIds);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            deviceSpinner.setAdapter(adapter);
            layout.addView(deviceSpinner);

            // 选择状态
            Spinner statusSpinner = new Spinner(this);
            ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{Device.STATUS_ON, Device.STATUS_OFF, Device.STATUS_OFFLINE, Device.STATUS_ERROR});
            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            statusSpinner.setAdapter(statusAdapter);
            layout.addView(statusSpinner);

            new AlertDialog.Builder(this)
                    .setTitle("修改设备状态")
                    .setView(layout)
                    .setPositiveButton("确定", (dialog, which) -> {
                        String deviceId = (String) deviceSpinner.getSelectedItem();

                        Device device = null;
                        for (Device d : deviceList) {
                            if (d.getID().equals(deviceId)) {
                                device = d;
                                break;
                            }
                        }

                        String status = (String) statusSpinner.getSelectedItem();
                        device.setStatus(status);
                        DeviceUtil.updateDevice(this, device);
                        DialogUtil.showToastShort(this, "修改设备状态成功");
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                        // Dismiss the dialog
                    })
                    .show();

        });

        findViewById(R.id.debug_send_delay_task).setOnClickListener(v -> {
            LocalDateTime now = LocalDateTime.now();
            now = now.plusSeconds(10);
            Action action = new Action("BBB", Action.ACTION_TYPE_CLOSE, TimeUtil.getMillis(now), Device.TYPE_CURTAIN);
            ActionClient.sendAction(this, action);
            Log.d("DebugActivity", "发送消息成功");
        });

        findViewById(R.id.debug_open_sync_device_status).setOnClickListener(v -> {
            // Set a boolean value in SharedPreferences to enable automatic device status updates
            ParamUtil.saveBoolean(this, ParamUtil.UPDATE_DEVICE_STATUS, true);
            DialogUtil.showToastShort(this, "开启自动更新设备状态");
        });

        findViewById(R.id.debug_close_sync_device_status).setOnClickListener(v -> {
            // Set a boolean value in SharedPreferences to disable automatic device status updates
            ParamUtil.saveBoolean(this, ParamUtil.UPDATE_DEVICE_STATUS, false);
            DialogUtil.showToastShort(this, "关闭自动更新设备状态");
        });

        findViewById(R.id.debug_config_curtain_position).setOnClickListener(v -> {
            // 弹出一个位置设置对话框，0，10，20，...，100
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(16, 16, 16, 16);

            // 选择设备ID
            Spinner deviceSpinner = new Spinner(this);
            ArrayList<Device> deviceList = DeviceUtil.getDeviceList(this);
            ArrayList<String> deviceIds = new ArrayList<>();
            for (Device device : deviceList) {
                if (device.getType().equals(Device.TYPE_CURTAIN)) {
                    deviceIds.add(device.getID());
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, deviceIds);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            deviceSpinner.setAdapter(adapter);
            layout.addView(deviceSpinner);

            // 选择位置
            Spinner positionSpinner = new Spinner(this);
            ArrayList<String> positions = new ArrayList<>();
            for (int i = 0; i <= 10; i++) {
                positions.add(i * 10 + "%");
            }
            ArrayAdapter<String> positionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, positions);
            positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            positionSpinner.setAdapter(positionAdapter);
            layout.addView(positionSpinner);

            new AlertDialog.Builder(this)
                    .setTitle("设置窗帘位置")
                    .setView(layout)
                    .setPositiveButton("确定", (dialog, which) -> {
                        String deviceId = (String) deviceSpinner.getSelectedItem();
                        String position = (String) positionSpinner.getSelectedItem();
                        ParamUtil.saveString(this, ParamUtil.CURTAIN_POSITION, position.substring(0, position.length() - 1));
                        ActionClient.sendAction(this, new Action(deviceId, Action.ACTION_TYPE_CONFIG_POSITION, TimeUtil.getNowMillis(), Device.TYPE_CURTAIN, position.substring(0, position.length() - 1)));
                        DialogUtil.showToastShort(this, "设置窗帘位置成功");
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                        // Dismiss the dialog
                    })
                    .show();
        });

        findViewById(R.id.debug_config_switch_hall).setOnClickListener(v -> {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(16, 16, 16, 16);

            TextView tv_1 = new TextView(this);
            tv_1.setText("开关1");
            layout.addView(tv_1);

            EditText et_1 = new EditText(this);
            et_1.setInputType(InputType.TYPE_CLASS_NUMBER);
            layout.addView(et_1);

            TextView tv_2 = new TextView(this);
            tv_2.setText("开关2");
            layout.addView(tv_2);

            EditText et_2 = new EditText(this);
            et_2.setInputType(InputType.TYPE_CLASS_NUMBER);
            layout.addView(et_2);

            new AlertDialog.Builder(this)
                    .setTitle("设置开关霍尔值")
                    .setView(layout)
                    .setPositiveButton("确定", (dialog, which) -> {
                        String switch1 = et_1.getText().toString();
                        String switch2 = et_2.getText().toString();
                        ActionClient.sendAction(this, new Action("AAA", Action.ACTION_TYPE_CONFIG_SWITCH_HALL, TimeUtil.getNowMillis(), Device.TYPE_SWITCH, switch1 + "," + switch2));
                        DialogUtil.showToastShort(this, "设置大厅开关成功");
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                        // Dismiss the dialog
                    })
                    .show();
        });

        findViewById(R.id.debug_config_switch_angle).setOnClickListener(v -> {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(16, 16, 16, 16);

            TextView tv_1 = new TextView(this);
            tv_1.setText("开关1");
            layout.addView(tv_1);

            EditText et_1 = new EditText(this);
            et_1.setInputType(InputType.TYPE_CLASS_NUMBER);
            layout.addView(et_1);

            TextView tv_2 = new TextView(this);
            tv_2.setText("开关2");
            layout.addView(tv_2);

            EditText et_2 = new EditText(this);
            et_2.setInputType(InputType.TYPE_CLASS_NUMBER);
            layout.addView(et_2);

            new AlertDialog.Builder(this)
                    .setTitle("设置开关角度")
                    .setView(layout)
                    .setPositiveButton("确定", (dialog, which) -> {
                        String switch1 = et_1.getText().toString();
                        String switch2 = et_2.getText().toString();
                        ActionClient.sendAction(this, new Action("AAA", Action.ACTION_TYPE_CONFIG_SWITCH_ANGLE, TimeUtil.getNowMillis(), Device.TYPE_SWITCH, switch1 + "," + switch2));
                        DialogUtil.showToastShort(this, "设置开关角度成功");
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                        // Dismiss the dialog
                    })
                    .show();
        });

        findViewById(R.id.debug_delete_device).setOnClickListener(v -> {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(16, 16, 16, 16);

            Spinner deviceSpinner = new Spinner(this);
            ArrayList<Device> deviceList = DeviceUtil.getDeviceList(this);
            ArrayList<String> deviceIds = new ArrayList<>();
            for (Device device : deviceList) {
                deviceIds.add(device.getID());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, deviceIds);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            deviceSpinner.setAdapter(adapter);
            layout.addView(deviceSpinner);

            new AlertDialog.Builder(this)
                    .setTitle("删除设备")
                    .setView(layout)
                    .setPositiveButton("确定", (dialog, which) -> {
                        String deviceId = (String) deviceSpinner.getSelectedItem();
                        Device device = null;
                        for (Device d : deviceList) {
                            if (d.getID().equals(deviceId)) {
                                device = d;
                                break;
                            }
                        }
                        DeviceUtil.removeDevice(this, device);
                        DialogUtil.showToastShort(this, "删除设备成功");
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                        // Dismiss the dialog
                    })
                    .show();
        });

    }
}