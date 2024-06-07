package com.minjer.smarthome.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.minjer.smarthome.pojo.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageUtil {
    private static final String TAG = "MessageUtil";
    public static void addMessage(Context context, Message message) {
        ArrayList<Message> messageList = getMessageList(context);
        messageList.add(0, message);
        String messageListJson = JsonUtil.toJson(messageList);
        ParamUtil.saveString(context, ParamUtil.MESSAGE_LIST, messageListJson);
    }
    public static void clearMessageList(Context context) {
        ParamUtil.remove(context, ParamUtil.MESSAGE_LIST);
    }
    public static ArrayList<Message> getMessageList(Context context) {
        String messageListJson = ParamUtil.getString(context, ParamUtil.MESSAGE_LIST, null);
        if (messageListJson == null) {
            return new ArrayList<>();
        }
        ArrayList<Message> messageList = JsonUtil.parseToObject(messageListJson, new TypeToken<List<Message>>(){}.getType());
        Log.d(TAG, "Message list: " + messageListJson);

        return messageList;
    }
}
