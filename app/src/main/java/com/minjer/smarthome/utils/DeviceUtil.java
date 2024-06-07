package com.minjer.smarthome.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.minjer.smarthome.pojo.Device;
import com.minjer.smarthome.pojo.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeviceUtil {
    private static final String TAG = "DeviceUtil";

    private static final List<String> types = Collections.unmodifiableList(Arrays.asList(
            Device.TYPE_LIGHT,
            Device.TYPE_SWITCH,
            Device.TYPE_CURTAIN,
            Device.TYPE_RADAR,
            Device.TYPE_SENSOR_LIGHT,
            Device.TYPE_SENSOR_TEMP
    ));

    public static void addDevice(Context context, Device device) {
        ArrayList<Device> deviceList = getDeviceList(context);
        deviceList.add(0, device);
        String deviceListJson = JsonUtil.toJson(deviceList);
        ParamUtil.saveString(context, ParamUtil.DEVICE_LIST, deviceListJson);
    }
    public static void clearDeviceList(Context context) {
        ParamUtil.remove(context, ParamUtil.DEVICE_LIST);
    }
    public static ArrayList<Device> getDeviceList(Context context) {
        String messageListJson = ParamUtil.getString(context, ParamUtil.DEVICE_LIST, null);
        if (messageListJson == null) {
            return new ArrayList<>();
        }
        ArrayList<Device> deviceList = JsonUtil.parseToObject(messageListJson, new TypeToken<List<Device>>(){}.getType());
        Log.d(TAG, "Device list: " + messageListJson);

        return deviceList;
    }

    public static Device randomGenerateDevice() {
        Device device = new Device();
        device.setID("AAAAAAAAAAAAA");
        device.setName("测试设备");
        device.setDescription("这是一个测试设备");
        device.setStatus(Device.STATUS_ON);
        // 随机选择一个
        device.setType(types.get((int) (Math.random() * types.size())));

        return device;
    }
}
