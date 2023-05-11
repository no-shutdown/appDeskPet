package com.xl.pet.flowWindow;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.xl.pet.R;
import com.xl.pet.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

import static com.xl.pet.constants.Constants.AUTH_TAG;
import static com.xl.pet.constants.Constants.LOG_TAG;

public class FloatWindowService extends Service {



    //定时器，定时进行检测当前应该创建还是移除悬浮窗。
    private Timer timer;
    //用于在线程中创建或移除悬浮窗
    private final Handler handler = new Handler();
    //悬浮窗管理
    private FloatWindowManager floatManager;

    private static final String CHANNEL_ID = "petChannelId";
    private static final String CHANNEL_NAME = "petChannelName";

    @Override
    public void onCreate() {
        super.onCreate();
        //如果api大于26在通知栏显示一个通知
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel chan = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_SECRET);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                    this, CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(AUTH_TAG)
                    .setPriority(NotificationManager.IMPORTANCE_LOW)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setChannelId(CHANNEL_ID)
                    .build();

            startForeground(1, notification);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG,"服务开启");
        floatManager = new FloatWindowManager();
        // 开启定时器，每隔0.5秒刷新一次
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
        }
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
            floatManager.removeAll(getApplicationContext());
        }
        stopForeground(true);
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
