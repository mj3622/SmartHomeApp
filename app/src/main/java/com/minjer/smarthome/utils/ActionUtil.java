package com.minjer.smarthome.utils;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
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
        ArrayList<Action> actionList = JsonUtil.parseToObject(actionListJson, new TypeToken<List<Action>>(){}.getType());
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

}
