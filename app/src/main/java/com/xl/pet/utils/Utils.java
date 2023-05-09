package com.xl.pet.utils;

import android.app.ActivityManager;
import android.content.Intent;

import java.util.List;

public class Utils {
    // 判断是否在桌面
    public static boolean isHome(ActivityManager activityManager, String packageName) {
        //TODO 如何在5.0及以上版本判断是否在页面上
        List<ActivityManager.AppTask> appTasks = activityManager.getAppTasks();
        if (appTasks.size() > 0) {
            ActivityManager.RecentTaskInfo recentTaskInfo = appTasks.get(0).getTaskInfo();
            return recentTaskInfo.baseIntent.hasCategory(Intent.CATEGORY_HOME)
                    && recentTaskInfo.topActivity.getPackageName().equals(packageName);
        }
        return false;
    }

}
