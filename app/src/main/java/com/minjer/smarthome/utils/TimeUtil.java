package com.minjer.smarthome.utils;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class TimeUtil {
    private static final String TAG = "TimeUtil";
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);
    private static final String DAY_PATTERN = "yyyy-MM-dd";
    private static SimpleDateFormat daySimpleDateFormat = new SimpleDateFormat(DAY_PATTERN);

    private static final Long WEAHTER_REFREASH_INTERVAL = (long) (1000 * 60 * 60 * 1);

    public static String getNowTime() {
        return simpleDateFormat.format(new Date());
    }

    public static String getNowDay() {
        return daySimpleDateFormat.format(new Date());
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

    public static String getNowMillis() {
        long millis = System.currentTimeMillis();
        Log.d(TAG, "Millis: " + millis);
        return String.valueOf(millis);
    }

    public static String getMillis(LocalDateTime time) {
        long millis = time.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Log.d(TAG, "Millis: " + millis);
        return String.valueOf(millis);
    }
}
