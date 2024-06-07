package com.minjer.smarthome.pojo;

public class Device {
    public static final String TYPE_LIGHT = "light";
    public static final String TYPE_SWITCH = "switch";
    public static final String TYPE_SENSOR_LIGHT = "sensor_light";
    public static final String TYPE_SENSOR_TEMP = "sensor_temp";
    public static final String TYPE_RADAR = "radar";
    public static final String TYPE_CURTAIN = "curtain";

    public static final String STATUS_ON = "on";
    public static final String STATUS_OFF = "off";
    public static final String STATUS_OFFLINE = "offline";


    private String name;
    private String ID;
    private String type;
    private String status;
    private String description;

    public Device(String name, String ID, String type, String status, String description) {
        this.name = name;
        this.ID = ID;
        this.type = type;
        this.status = status;
        this.description = description;
    }

    public Device() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
