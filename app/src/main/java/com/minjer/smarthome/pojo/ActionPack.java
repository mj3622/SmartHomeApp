package com.minjer.smarthome.pojo;

import java.util.ArrayList;

public class ActionPack {
    private String packName;
    private String packDescription;
    private ArrayList<Action> actions;

    public ActionPack() {
    }

    public ActionPack(String packName, String packDescription, ArrayList<Action> actions) {
        this.packName = packName;
        this.packDescription = packDescription;
        this.actions = actions;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getPackDescription() {
        return packDescription;
    }

    public void setPackDescription(String packDescription) {
        this.packDescription = packDescription;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }
}
