package com.minjer.smarthome;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.minjer.smarthome.pojo.Device;
import com.minjer.smarthome.pojo.Message;
import com.minjer.smarthome.utils.DeviceUtil;
import com.minjer.smarthome.utils.DialogUtil;
import com.minjer.smarthome.utils.MessageUtil;
import com.minjer.smarthome.utils.TimeUtil;

import java.util.List;
import java.util.Random;

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



    }
}