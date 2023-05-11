package com.xl.pet.flowWindow.pet;

import android.view.MotionEvent;


public interface DefaultActionable {
    //随机动画产生
    void randomChange();

    //触屏事件
    boolean onTouchEvent(MotionEvent event);


    //静坐5秒,sit
    void actionSit();


    enum Action {
        ACTION_FLAG,
        ACTION_WALK,
        ACTION_WALK2,
        ACTION_EAT,
        ACTION_SIT,
        ACTION_UP,
        ACTION_DOWN,
        ACTION_BALL,
        ACTION_HAPPY,
        ACTION_SLEEP,
        ACTION_SHOCK,
        ACTION_STAR;
    }

}
