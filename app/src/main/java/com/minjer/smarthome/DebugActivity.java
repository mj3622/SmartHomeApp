package com.minjer.smarthome;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.minjer.smarthome.dialog.ControlDialog;
import com.minjer.smarthome.http.ActionClient;
import com.minjer.smarthome.pojo.Action;
import com.minjer.smarthome.pojo.Device;
import com.minjer.smarthome.pojo.Message;
import com.minjer.smarthome.utils.DeviceUtil;
import com.minjer.smarthome.utils.DialogUtil;
import com.minjer.smarthome.utils.MessageUtil;
import com.minjer.smarthome.utils.TimeUtil;

import java.time.LocalDateTime;

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
            Action action = new Action("AAA", Action.ACTION_TYPE_OPEN, TimeUtil.getNowMillis(), Device.TYPE_LIGHT, "ffffaa");
            ActionClient.sendAction(this, action);
            Log.d("DebugActivity", "发送消息成功");
        });

        findViewById(R.id.debug_add_custom_device).setOnClickListener(v -> {
            // Create a custom dialog
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

        findViewById(R.id.debug_show_custom_window).setOnClickListener(v -> {
            ControlDialog dialog = new ControlDialog(this);
            dialog.setTitle("自定义窗口")
                    .setMessage("这是一个自定义窗口")
                    .setOnClickListener(new ControlDialog.OnClickListener() {
                        @Override
                        public void onCancelClick() {
                            DialogUtil.showToastShort(DebugActivity.this, "点击了取消");
                        }

                        @Override
                        public void onConfirmClick() {
                            DialogUtil.showToastShort(DebugActivity.this, "点击了确认");
                        }
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
    }
}