package com.minjer.smarthome.utils;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    private static final String TAG = "TimeUtil";
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);

    private static final Long WEAHTER_REFREASH_INTERVAL = (long) (1000 * 60 * 60 * 3);

    public static String getNowTime() {
        return simpleDateFormat.format(new Date());
    }

    /**
     * 判断是否需要刷新天气
     *
     * @return 是否需要刷新
     */
    public static Boolean needResreshWeather(Context context) {
        try {
            // 获取上次刷新时间
            String lastTime = ParamUtil.getString(context, ParamUtil.WEATHER_RESRESH_TIME, null);
            if (lastTime == null) return true;
            Date lastDate = simpleDateFormat.parse(lastTime);
            Date nowDate = new Date();
            long diff = nowDate.getTime() - lastDate.getTime();
            Log.d(TAG, "Time diff: " + diff);
            return diff > WEAHTER_REFREASH_INTERVAL;
        } catch (Exception e) {
            return true;
        }
    }
}
