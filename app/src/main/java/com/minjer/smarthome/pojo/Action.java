package com.minjer.smarthome.pojo;

public class Action {

    public static final String ACTION_TYPE_OPEN = "open";
    public static final String ACTION_TYPE_CLOSE = "close";


    private String deviceId;
    private String deviceType;
    private String actionType;
    private String Time;

    public Action(String deviceId, String actionType, String time, String deviceType) {
        this.deviceId = deviceId;
        this.actionType = actionType;
        this.Time = time;
        this.deviceType = deviceType;
    }

    public Action() {
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
