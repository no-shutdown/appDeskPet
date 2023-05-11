package com.xl.pet.flowWindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;


import com.xl.pet.flowWindow.pet.DefaultPet;

public class FloatWindowManager {

    //用于控制在屏幕上添加或移除悬浮窗
    private WindowManager windowManager;
    //悬浮窗布局
    private final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
    //宠物类
    private DefaultPet person;

    /**
     * 创建一个卡通人物
     */
    public void createPerson(Context context) {
        WindowManager windowManager = getOrCreateWindowManager(context);
        //参数设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        layoutParams.format = PixelFormat.RGBA_8888;//图片格式，背景透明
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        try {
            person = new DefaultPet(context);
            //参数设置
            layoutParams.x = (int) person.getX();
            layoutParams.y = (int) person.getY();
            layoutParams.width = person.getBmpW();//设置大小
            layoutParams.height = person.getBmpH();

        } catch (Exception e) {
            e.printStackTrace();
        }
        person.bindWindowManager(windowManager, layoutParams);
        windowManager.addView(person, layoutParams);
    }




    /**
     * 将卡通人物从屏幕上移除。
     */
    public void removePerson(Context context) {
        if (person != null) {
            //停止pet的线程
            person.stop();
            WindowManager windowManager = getOrCreateWindowManager(context);
            windowManager.removeView(person);
            person = null;
        }
    }


    public void removeAll(Context context){
        removePerson(context);
    }

    public boolean isWindowShowing() {
        return person != null;
    }
    private WindowManager getOrCreateWindowManager(Context context) {
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return windowManager;
    }


}
