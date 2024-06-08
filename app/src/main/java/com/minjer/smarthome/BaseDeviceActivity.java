package com.minjer.smarthome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.minjer.smarthome.deviceFragent.CurtainFragment;
import com.minjer.smarthome.deviceFragent.LightFragment;
import com.minjer.smarthome.deviceFragent.RadarFragment;
import com.minjer.smarthome.deviceFragent.SensorLightFragment;
import com.minjer.smarthome.deviceFragent.SensorTempFragment;
import com.minjer.smarthome.deviceFragent.SwitchFragment;
import com.minjer.smarthome.http.DataClient;
import com.minjer.smarthome.pojo.Device;
import com.minjer.smarthome.utils.DeviceUtil;
import com.minjer.smarthome.utils.DialogUtil;
import com.minjer.smarthome.utils.JsonUtil;
import com.minjer.smarthome.utils.PageUtil;
import com.minjer.smarthome.utils.ParamUtil;

public class BaseDeviceActivity extends AppCompatActivity {
    private static final String TAG = "BaseDeviceActivity";

    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private Device currentDevice;
    private TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_device);

        Intent intent = getIntent();
        String deviceJson = intent.getStringExtra("device");
        currentDevice = JsonUtil.parseToObject(deviceJson, Device.class);

        if (currentDevice == null) {
            Log.e(TAG, "device is null");
            DialogUtil.showToastShort(this, "未获取到设备信息");
            return;
        }

        // 初始化返回按钮
        findViewById(R.id.device_control_go_back_button).setOnClickListener(v -> finish());

        // 初始化修改设备按钮
        findViewById(R.id.device_control_change_info_button).setOnClickListener(v -> {
            EditText et_deviceName = new EditText(this);
            et_deviceName.setText(currentDevice.getName());
            et_deviceName.setMaxEms(8);
            EditText et_deviceDesc = new EditText(this);
            et_deviceDesc.setText(currentDevice.getDescription());
            et_deviceDesc.setMaxEms(20);

            // 创建 LinearLayout 容器,添加两个输入框
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(et_deviceName);
            linearLayout.addView(et_deviceDesc);

            // 创建提示窗
            new AlertDialog.Builder(this)
                    .setTitle("修改设备信息")
                    .setView(linearLayout)
                    .setPositiveButton("确定", (dialog, which) -> {
                        String name = et_deviceName.getText().toString();
                        String desc = et_deviceDesc.getText().toString();
                        if (name.isEmpty() || desc.isEmpty()) {
                            DialogUtil.showToastShort(this, "设备名称和描述不能为空");
                            return;
                        }
                        currentDevice.setName(name);
                        currentDevice.setDescription(desc);
                        DeviceUtil.updateDevice(this, currentDevice);
                        tv_name.setText(name);

                        // 更新设备信息
                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_OK, resultIntent);
                    })
                    .setNegativeButton("取消", null)
                    .show();

        });

        // 初始化设备名称
        tv_name = findViewById(R.id.device_control_title);
        tv_name.setText(currentDevice.getName());

        Fragment fragment = null;
        // 初始化设备控制界面
        if (currentDevice.getType().equals(Device.TYPE_CURTAIN)) {
            fragment = new CurtainFragment();
        } else if (currentDevice.getType().equals(Device.TYPE_LIGHT)) {
            fragment = new LightFragment();
        } else if (currentDevice.getType().equals(Device.TYPE_RADAR)) {
            fragment = new RadarFragment();
        } else if (currentDevice.getType().equals(Device.TYPE_SWITCH)) {
            fragment = SwitchFragment.newInstance(currentDevice);
        } else if (currentDevice.getType().equals(Device.TYPE_SENSOR_LIGHT)) {
            fragment = new SensorLightFragment();
        } else if (currentDevice.getType().equals(Device.TYPE_SENSOR_TEMP)) {
            fragment = new SensorTempFragment();
        } else {
            Log.e(TAG, "unknown device type: " + currentDevice.getType());
            DialogUtil.showToastShort(this, "未知设备类型");
        }
        Log.d(TAG, "device type: " + currentDevice.getType());
        if (fragment != null) {
            displayFragment(fragment);
        }
    }

    private void displayFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.device_control_container, fragment);
        transaction.commit();
    }
}