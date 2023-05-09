package com.xl.pet.utils;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    /**
     * 判断当前界面是否是桌面
     */
    public static boolean isHome(ActivityManager mActivityManager, PackageManager packageManager) {
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        //TODO 6.0版本中如何判断当前是否在桌面
        return true;
//        return getHomes(packageManager).contains(rti.get(0).topActivity.getPackageName());
    }

    /**
     * 获得属于桌面的应用的应用包名称
     */
    public static List<String> getHomes(PackageManager packageManager) {
        List<String> names = new ArrayList<String>();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }
}
