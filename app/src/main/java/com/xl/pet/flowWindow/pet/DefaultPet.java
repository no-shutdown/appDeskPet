package com.xl.pet.flowWindow.pet;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.xl.pet.R;
import com.xl.pet.constants.Constants;
import com.xl.pet.utils.Utils;


public class DefaultPet extends Pet implements DefaultActionable {

    //动画标识
    protected DefaultActionable.Action actionFlag;
    //主图
    private final Bitmap[] bmpImage = new Bitmap[3];
    //左右判别，向左1，右-1。默认左
    protected int leftOrRight = 1;
    //上下判别，向上1，下-1
    protected int upOrDown = 1;


    private WindowManager bindWindowManager;
    private WindowManager.LayoutParams bindWindowManagerLayoutParams;

    public DefaultPet(Context context) {
        super(context);
        //TODO 1、定义图片数组 2、取每一帧的图片
        personSize = 0.8f; //百分之80比例缩放
        actionFlag = Action.ACTION_SIT; //默认动作是坐下
        bmpImage[0] = Utils.decodeResource(res, R.drawable.cat_fight_12);
    }

    @Override
    protected void refreshFPSAction() {
        matrix.reset();
        float dis = (1 - personSize) * bmpW / 2;
        matrix.postScale(personSize * leftOrRight, personSize * upOrDown, bmpW / 2, bmpH / 2);
        matrix.postTranslate(-dis, -dis);
        switch (actionFlag) {
            case ACTION_SIT:
                //TODO
                bitmap = bmpImage[0];
//                actionSit();
                break;
            default:
                Log.i(Constants.LOG_TAG, "未知动作，" + actionFlag);
        }
    }

    @Override
    public void actionSit() {
        bmpImage[0] = Utils.decodeResource(res, R.drawable.chopper_sit);
        bmpImage[1] = null;
        bmpImage[2] = null;
    }

    @Override
    protected void refreshFPSCallback() {
        //渲染后刷新绑定的悬浮窗布局
        if (bindWindowManager != null && bindWindowManagerLayoutParams != null) {
            bindWindowManagerLayoutParams.x = (int) getX();
            bindWindowManagerLayoutParams.y = (int) getY();
            bindWindowManager.updateViewLayout(this, bindWindowManagerLayoutParams);
        } else {
            Log.w(Constants.LOG_TAG, "未绑定windowManager或windowManagerLayoutParams");
        }
    }

    public void bindWindowManager(WindowManager windowManager, WindowManager.LayoutParams layoutParams) {
        this.bindWindowManager = windowManager;
        this.bindWindowManagerLayoutParams = layoutParams;
    }

    @Override
    public void randomChange() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

}
