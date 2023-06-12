package com.xl.pet.utils;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.TypedValue;

import androidx.annotation.RequiresApi;

import com.haibin.calendarview.Calendar;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Utils {


    private static final Map<Integer, Bitmap> RES_CACHE = new HashMap<>();

    // 判断是否在桌面
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static boolean isHome(Context context) {
        String topActivity = "";
        //api 22以上
        UsageStatsManager m = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        if (null != m) {
            long now = System.currentTimeMillis();
            List<UsageStats> stats = m.queryUsageStats(UsageStatsManager.INTERVAL_BEST, now - 60 * 1000 * 60, now);
            if ((stats != null) && (!stats.isEmpty())) {
                int j = 0;
                for (int i = 0; i < stats.size(); i++) {
                    if (stats.get(i).getLastTimeUsed() > stats.get(j).getLastTimeUsed()) {
                        j = i;
                    }
                }
                topActivity = stats.get(j).getPackageName();
            }
//            Log.d(LOG_TAG,"顶部activity "+topActivity);
        }
        return "com.android.launcher3".equals(topActivity) //低版本安卓
                || "com.google.android.apps.nexuslauncher".equals(topActivity) //高版本安卓
                || "com.huawei.android.launcher".equals(topActivity); //兼容华为设备
    }


    //解析资源
    public static Bitmap decodeResource(Resources resources, int id) {
        Bitmap cache = RES_CACHE.get(id);
        if (null == cache) {
            TypedValue value = new TypedValue();
            resources.openRawResource(id, value);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inTargetDensity = value.density;
            cache = BitmapFactory.decodeResource(resources, id, opts);
            RES_CACHE.put(id, cache);
        }
        return cache;
    }


    //日历往后推n天
    public static Calendar calendarAdd(Calendar calendar, int n) {
        java.util.Calendar javaCalendar = java.util.Calendar.getInstance();
        javaCalendar.setTimeInMillis(calendar.getTimeInMillis());
        javaCalendar.add(java.util.Calendar.DATE, n);

        Calendar result = new Calendar();
        result.setYear(javaCalendar.get(java.util.Calendar.YEAR));
        result.setMonth(javaCalendar.get(java.util.Calendar.MONTH) + 1);
        result.setDay(javaCalendar.get(java.util.Calendar.DATE));
        return result;
    }

    //获取今天日历
    public static Calendar todayCalendar() {
        java.util.Calendar javaCalendar = java.util.Calendar.getInstance();
        Calendar result = new Calendar();
        result.setYear(javaCalendar.get(java.util.Calendar.YEAR));
        result.setMonth(javaCalendar.get(java.util.Calendar.MONTH) + 1);
        result.setDay(javaCalendar.get(java.util.Calendar.DATE));
        return result;
    }

    //计算平均值
    public static double avg(List<Integer> list) {
        int sum = 0;
        for (int num : list) {
            sum += num;
        }
        return (double) sum / list.size();
    }

    //计算两点之间的距离
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }


    // 获取今天的日期
    public static java.util.Calendar getToday() {
        return setZeroClock(java.util.Calendar.getInstance());
    }

    // 获取本周的第一天（周日）
    public static java.util.Calendar getFirstDayOfWeek() {
        java.util.Calendar calendar = getToday();
        calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.SUNDAY);
        return setZeroClock(calendar);
    }

    // 获取本月的第一天
    public static java.util.Calendar getFirstDayOfMonth() {
        java.util.Calendar calendar = getToday();
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
        return setZeroClock(calendar);
    }

    // 获取本年的第一天
    public static java.util.Calendar getFirstDayOfYear() {
        java.util.Calendar calendar = getToday();
        calendar.set(java.util.Calendar.DAY_OF_YEAR, 1);
        return setZeroClock(calendar);
    }

    //设置0点0分0秒
    public static java.util.Calendar setZeroClock(java.util.Calendar calendar) {
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        return calendar;
    }

    //得到最小的平方数大于等于指定数
    public static int getMinSquare(int number) {
        if (number <= 0) {
            return 0;
        }
        int square = 0;
        int i = 1;
        while (square < number) {
            square = i * i;
            i++;
        }
        return square;
    }

}
