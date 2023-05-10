package com.xl.pet.flowWindow;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.xl.pet.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

import static com.xl.pet.constants.Constants.LOG_TAG;

public class FloatWindowService extends Service {

    /**
     * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
     */
    private Timer timer;
    /**
     * 用于在线程中创建或移除悬浮窗。
     */
    private Handler handler = new Handler();
    /**
     * 悬浮窗管理
     */
    private FloatWindowManager floatManager;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        floatManager = new FloatWindowManager();
        // 开启定时器，每隔0.5秒刷新一次
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
        }

        Log.i(LOG_TAG,"服务开启");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG,"服务关闭");
        super.onDestroy();
        // Service被终止的同时也停止定时器继续运行
        if (timer != null){
            timer.cancel();
        }
        timer = null;
        if (floatManager != null){
            floatManager.removeData(getApplicationContext());
        }
    }


    class RefreshTask extends TimerTask {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
        @Override
        public void run() {
            boolean isHome = Utils.isHome(getApplicationContext());
            // 当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗。
            if (isHome && !floatManager.isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(LOG_TAG,"创建悬浮窗");
                        floatManager.createPerson(getApplicationContext());
                    }
                });
            }
            // 当前界面不是桌面，且有悬浮窗显示，则移除悬浮窗。
            else if (!isHome && floatManager.isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(LOG_TAG,"移除悬浮窗");
                        floatManager.removePerson(getApplicationContext());
                    }
                });
            }
//            // 当前界面是桌面，且有悬浮窗显示，则更新内存数据。
//            else if (isHome && floatManager.isWindowShowing()) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        floatManager.updateUsedPercent(getApplicationContext());
//                    }
//                });
//            }
        }

    }

}
