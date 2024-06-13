package com.minjer.smarthome.pojo;

public class Action {

    public static final String ACTION_TYPE_OPEN = "open";
    public static final String ACTION_TYPE_CLOSE = "close";

    public static final String ACTION_TYPE_PAUSE = "pause";

    public static final String ACTION_TYPE_CONFIG_SPEED = "speed";

    public static final String ACTION_TYPE_CONFIG_POSITION = "position";

    public static final String DEFAULT_LIGHT_COLOR = "ff0000";
    public static final String ACTION_TYPE_CONFIG_SWITCH_HALL = "hall";

    public static final String ACTION_TYPE_CONFIG_SWITCH_ANGLE = "angle";


    private String deviceId;
    private String deviceType;
    private String actionType;
    private String Time;

    private String info;

    public Action(String deviceId, String actionType, String time, String deviceType, String info) {
        this.deviceId = deviceId;
        this.actionType = actionType;
        this.Time = time;
        this.deviceType = deviceType;
        this.info = info;
    }

    public Action(String deviceId, String actionType, String time, String deviceType) {
        this.deviceId = deviceId;
        this.actionType = actionType;
        this.Time = time;
        this.deviceType = deviceType;
        this.info = null;
    }

    public Action() {
    }

    public static String translateColor(int r, int g, int b) {
        return String.format("%02x%02x%02x", r, g, b);
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }
}
