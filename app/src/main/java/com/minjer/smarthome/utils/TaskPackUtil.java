package com.minjer.smarthome.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.minjer.smarthome.pojo.ActionPack;

import java.util.ArrayList;
import java.util.List;

public class TaskPackUtil {
    private static final String TAG = "TaskPackUtil";

    public static ArrayList<ActionPack> getActionPackPackList(Context context) {
        String taskPackListJson = ParamUtil.getString(context, ParamUtil.ACTION_PACK_LIST, null);
        if (taskPackListJson == null) {
            return new ArrayList<>();
        }
        ArrayList<ActionPack> taskPackList = JsonUtil.parseToObject(taskPackListJson, new TypeToken<List<ActionPack>>(){}.getType());
        Log.d(TAG, "TaskPack list: " + taskPackListJson);

        return taskPackList;
    }


    public static void addActionPack(Context context, ActionPack taskPack) {
        Log.d(TAG, "Add TaskPack: " + taskPack.getPackName());
        ArrayList<ActionPack> taskPackList = getActionPackPackList(context);
        taskPackList.add(0, taskPack);
        String taskPackListJson = JsonUtil.toJson(taskPackList);
        ParamUtil.saveString(context, ParamUtil.ACTION_PACK_LIST, taskPackListJson);
    }

    public static void clearActionPackList(Context context) {
        ParamUtil.remove(context, ParamUtil.ACTION_PACK_LIST);
    }

    public static void removeActionPack(Context context, ActionPack taskPack) {
        ArrayList<ActionPack> taskPackList = getActionPackPackList(context);
        for (int i = 0; i < taskPackList.size(); i++) {
            if (taskPackList.get(i).getPackName().equals(taskPack.getPackName())) {
                taskPackList.remove(i);
                break;
            }
        }
        String taskPackListJson = JsonUtil.toJson(taskPackList);
        ParamUtil.saveString(context, ParamUtil.ACTION_PACK_LIST, taskPackListJson);
    }
}
