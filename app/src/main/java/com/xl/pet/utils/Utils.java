package com.xl.pet.utils;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.List;

import static com.xl.pet.constants.Constants.LOG_TAG;

public class Utils {
    // 判断是否在桌面
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static boolean isHome(Context context) {
        String topActivity = "";
        //android5.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager m = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            if (null!=m){
                long now = System.currentTimeMillis();
                //obtain recent in 1 hour app status
                List<UsageStats> stats = m.queryUsageStats(UsageStatsManager.INTERVAL_BEST, now - 60 * 1000 * 60, now);
                //obtain recent run app，maybe running app
                if ((stats != null) && (!stats.isEmpty())) {
                    int j = 0;
                    for (int i = 0; i < stats.size(); i++) {
                        if (stats.get(i).getLastTimeUsed() > stats.get(j).getLastTimeUsed()) {
                            j = i;
                        }
                    }
                    topActivity = stats.get(j).getPackageName();
                }
                Log.d(LOG_TAG,"顶部activity"+topActivity);
            }
            return "com.android.launcher3".equals(topActivity)
                    || "com.google.android.apps.nexuslauncher".equals(topActivity);
        }
        return true;
    }

}
