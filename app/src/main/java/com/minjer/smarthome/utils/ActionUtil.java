package com.minjer.smarthome.utils;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.minjer.smarthome.OnTimeActivity;
import com.minjer.smarthome.pojo.Action;

import java.util.ArrayList;
import java.util.List;


public class ActionUtil {
    public static final String TAG = "ActionUtil";

    public static ArrayList<Action> getPackCreatTempList(Context context) {
        String actionListJson = ParamUtil.getString(context, ParamUtil.PACK_CREAT_TEMP_LIST, null);
        if (actionListJson == null) {
            return new ArrayList<>();
        }
        ArrayList<Action> actionList = JsonUtil.parseToObject(actionListJson, new TypeToken<List<Action>>() {
        }.getType());
        return actionList;
    }

    public static void addToPackCreatTempList(Context context, Action action) {
        ArrayList<Action> actionList = getPackCreatTempList(context);
        actionList.add(0, action);
        String actionListJson = JsonUtil.toJson(actionList);
        ParamUtil.saveString(context, ParamUtil.PACK_CREAT_TEMP_LIST, actionListJson);
    }

    public static void clearPackCreatTempList(Context context) {
        ParamUtil.remove(context, ParamUtil.PACK_CREAT_TEMP_LIST);
    }

    public static ArrayList<Action> getOnTimeList(Context context) {
        String actionListJson = ParamUtil.getString(context, ParamUtil.ON_TIME_LIST, null);
        if (actionListJson == null) {
            return new ArrayList<>();
        }
        ArrayList<Action> actionList = JsonUtil.parseToObject(actionListJson, new TypeToken<List<Action>>() {
        }.getType());
        return actionList;
    }

    public static void addToOnTimeList(Context context, Action action) {
        ArrayList<Action> actionList = getOnTimeList(context);
        actionList.add(0, action);
        String actionListJson = JsonUtil.toJson(actionList);
        ParamUtil.saveString(context, ParamUtil.ON_TIME_LIST, actionListJson);
    }

    public static void clearOnTimeList(Context context) {
        ParamUtil.remove(context, ParamUtil.ON_TIME_LIST);
    }

    public static void removeOnTime(Context context, Action action) {
        ArrayList<Action> actionList = getOnTimeList(context);
        for (int i = 0; i < actionList.size(); i++) {
            if (actionList.get(i).getDeviceId().equals(action.getDeviceId()) && actionList.get(i).getActionType().equals(action.getActionType())) {
                actionList.remove(i);
                break;
            }
        }
        String actionListJson = JsonUtil.toJson(actionList);
        ParamUtil.saveString(context, ParamUtil.ON_TIME_LIST, actionListJson);
    }

    public static void clearOutTimeAction(Context context) {
        ArrayList<Action> actionList = getOnTimeList(context);
        ArrayList<Action> outTimeActionList = new ArrayList<>();
        for (Action action : actionList) {
            if (TimeUtil.isOutTime(action.getTime())) {
                outTimeActionList.add(action);
            }
        }
        for (Action action : outTimeActionList) {
            actionList.remove(action);
        }
        String actionListJson = JsonUtil.toJson(actionList);
        ParamUtil.saveString(context, ParamUtil.ON_TIME_LIST, actionListJson);
    }
}
