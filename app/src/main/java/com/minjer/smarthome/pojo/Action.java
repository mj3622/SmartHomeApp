package com.minjer.smarthome.pojo;

public class Action {
    private String deviceId;
    private Integer actionType;
    private String msg;

    public Action(String deviceId, Integer actionType, String msg) {
        this.deviceId = deviceId;
        this.actionType = actionType;
        this.msg = msg;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
