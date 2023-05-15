package com.xl.pet.utils;

import static com.xl.pet.constants.Constants.LOG_TAG;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.RequiresApi;

import java.util.List;


public class Utils {
    // 判断是否在桌面
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static boolean isHome(Context context) {
        String topActivity = "";
        //api 22以上
        UsageStatsManager m = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        if (null!=m){
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
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }

}
