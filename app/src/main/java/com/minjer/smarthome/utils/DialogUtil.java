package com.minjer.smarthome.utils;

import android.content.Context;
import android.widget.Toast;

public class DialogUtil {
    /**
     * 显示短时间的Toast消息
     *
     * @param context 上下文
     * @param message 要显示的消息
     */
    public static void showToastShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示长时间的Toast消息
     *
     * @param context 上下文
     * @param message 要显示的消息
     */
    public static void showToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


}
