package com.minjer.smarthome.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.minjer.smarthome.MessageBoxActivity;

public class PageUtil {
    /**
     * 刷新页面
     *
     * @param context 上下文
     */
    public static final void refreshPage(Activity context) {
        Intent intent = context.getIntent();
        context.finish(); // 结束当前活动
        context.startActivity(intent); // 启动新实例，触发 onCreate() 方法重新执行
    }
}
