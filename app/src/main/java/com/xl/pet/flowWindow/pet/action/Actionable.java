package com.xl.pet.flowWindow.pet.action;

import android.view.MotionEvent;


public interface Actionable {

    //动作
    void actionChange(ActionFlag flag);

    //随机动画产生
    void randomChange();

    //触屏事件
    boolean onTouchEvent(MotionEvent event);

}
