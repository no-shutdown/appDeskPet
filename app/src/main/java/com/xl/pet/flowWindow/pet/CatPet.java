package com.xl.pet.flowWindow.pet;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.xl.pet.R;
import com.xl.pet.constants.Constants;
import com.xl.pet.flowWindow.pet.action.ActionFlag;
import com.xl.pet.utils.Utils;


public class CatPet extends Pet {

    private static final int MAX_IMAGES_INDEX = 9;

    //动画标识
    Action actionFlag = null;
    //主图
    private final Bitmap[] actionImages = new Bitmap[MAX_IMAGES_INDEX];
    private int actionIndex = 0;
    //左右判别，向左1，右-1。默认左
    protected int leftOrRight = 1;
    //上下判别，向上1，下-1
    protected int upOrDown = 1;


    private WindowManager bindWindowManager;
    private WindowManager.LayoutParams bindWindowManagerLayoutParams;

    public CatPet(Context context) {
        super(context);
        personSize = 0.8f; //百分之80比例缩放
        actionChange(Action.COLD);
    }

    @Override
    protected void actionFPS() {
        matrix.reset();
        float dis = (1 - personSize) * bmpW / 2;
        //调整缩放、方向
        matrix.postScale(personSize * leftOrRight, personSize * upOrDown, bmpW / 2, bmpH / 2);
        //调整位置
        matrix.postTranslate(-dis, -dis);
        //渲染帧图
        actionIndex++;
        if (actionIndex >= MAX_IMAGES_INDEX || actionImages[actionIndex] == null) {
            actionIndex = 0;
        }
        bitmapFPS = actionImages[actionIndex];
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
    public void actionChange(ActionFlag flag) {
        actionFlag = (Action) flag;
        actionIndex = 0; //重置动作下标
        switch (actionFlag) {
            case FIGHT:
                actionImages[0] = Utils.decodeResource(res, R.drawable.cat_fight_1);
                actionImages[1] = Utils.decodeResource(res, R.drawable.cat_fight_2);
                actionImages[2] = Utils.decodeResource(res, R.drawable.cat_fight_3);
                actionImages[3] = Utils.decodeResource(res, R.drawable.cat_fight_4);
                actionImages[4] = Utils.decodeResource(res, R.drawable.cat_fight_5);
                actionImages[5] = Utils.decodeResource(res, R.drawable.cat_fight_6);
                actionImages[6] = Utils.decodeResource(res, R.drawable.cat_fight_7);
                actionImages[7] = Utils.decodeResource(res, R.drawable.cat_fight_8);
                actionImages[8] = Utils.decodeResource(res, R.drawable.cat_fight_9);
                break;
            case COLD:
                actionImages[0] = Utils.decodeResource(res, R.drawable.cat_cold_1);
                actionImages[1] = Utils.decodeResource(res, R.drawable.cat_cold_2);
                actionImages[2] = Utils.decodeResource(res, R.drawable.cat_cold_3);
                actionImages[3] = Utils.decodeResource(res, R.drawable.cat_cold_4);
                actionImages[4] = Utils.decodeResource(res, R.drawable.cat_cold_5);
                break;
            default:
                Log.w(Constants.LOG_TAG, "未知动作标识：" + flag);
        }
    }

    @Override
    public void randomChange() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    enum Action implements ActionFlag {
        FIGHT,
        COLD,

    }

}
