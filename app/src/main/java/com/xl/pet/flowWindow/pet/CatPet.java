package com.xl.pet.flowWindow.pet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.xl.pet.R;
import com.xl.pet.constants.Constants;
import com.xl.pet.flowWindow.pet.action.ActionFlag;
import com.xl.pet.utils.Utils;

import java.util.Random;


public class CatPet extends Pet {

    private static final int MAX_IMAGES_INDEX = 9;
    //随机变动最大的时间间隔
    private static final int MAX_RANDOM_CHANGE_INTERVAL = 10000;

    //动画标识
    Action actionFlag = null;
    Random random = new Random();
    Action[] randomChangeList = Action.values();
    //上次行为变动的时间
    private long lastActionChangeTime;

    //主图
    private final Bitmap[] actionImages = new Bitmap[MAX_IMAGES_INDEX];
    private int actionIndex = 0;
    //左右判别，向左1，右-1。默认左
    protected int leftOrRight = 1;
    //上下判别，向上1，下-1
    protected int upOrDown = 1;

    //点击坐标
    protected float touchX, touchY;
    protected float lastTouchX, lastTouchY;

    private WindowManager bindWindowManager;
    private WindowManager.LayoutParams bindWindowManagerLayoutParams;

    public CatPet(Context context) {
        super(context);
        Bitmap bitmap = Utils.decodeResource(res, R.drawable.cat_stand);
        bmpH = bitmap.getHeight() + 10; //10伸缩空间
        bmpW = bitmap.getWidth() + 30; //30伸缩空间
        personSize = 0.8f; //百分之80比例缩放
        actionChange(Action.DOUBT);
        getHandler().postDelayed(this::randomChange, MAX_RANDOM_CHANGE_INTERVAL);
    }

    @Override
    protected void actionFPS() {
        matrix.reset();
        int bmpW = getBmpW(), bmpH = getBmpH();
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
        if (actionFlag == flag) {
            return;
        }
        actionFlag = (Action) flag;
        actionIndex = 0; //重置动作下标
        clearImages();
        switch (actionFlag) {
            case STAND:
                actionImages[1] = Utils.decodeResource(res, R.drawable.cat_stand);
                break;
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
                this.postDelayed(() -> actionChange(Action.STAND), 5000);
                break;
            case COLD:
                actionImages[0] = Utils.decodeResource(res, R.drawable.cat_cold_1);
                actionImages[1] = Utils.decodeResource(res, R.drawable.cat_cold_3);
                this.postDelayed(() -> actionChange(Action.STAND), 5000);
                break;
            case BALL:
                actionImages[0] = Utils.decodeResource(res, R.drawable.cat_ball_1);
                actionImages[1] = Utils.decodeResource(res, R.drawable.cat_ball_2);
                actionImages[2] = Utils.decodeResource(res, R.drawable.cat_ball_3);
                actionImages[3] = Utils.decodeResource(res, R.drawable.cat_ball_4);
                actionImages[4] = Utils.decodeResource(res, R.drawable.cat_ball_5);
                break;
            case HELLO:
                actionImages[0] = Utils.decodeResource(res, R.drawable.cat_hello_1);
                actionImages[1] = Utils.decodeResource(res, R.drawable.cat_hello_2);
                actionImages[2] = Utils.decodeResource(res, R.drawable.cat_hello_3);
                actionImages[3] = Utils.decodeResource(res, R.drawable.cat_hello_4);
                actionImages[4] = Utils.decodeResource(res, R.drawable.cat_hello_5);
                actionImages[5] = Utils.decodeResource(res, R.drawable.cat_hello_6);
                actionImages[6] = Utils.decodeResource(res, R.drawable.cat_hello_7);
                this.postDelayed(() -> actionChange(Action.STAND), 5000);
                break;
            case DOUBT:
                actionImages[0] = Utils.decodeResource(res, R.drawable.cat_doubt_2);
                actionImages[1] = Utils.decodeResource(res, R.drawable.cat_doubt_2);
                actionImages[2] = Utils.decodeResource(res, R.drawable.cat_doubt_5);
                actionImages[3] = Utils.decodeResource(res, R.drawable.cat_doubt_5);
                this.postDelayed(() -> actionChange(Action.STAND), 5000);
                break;
            default:
                Log.w(Constants.LOG_TAG, "未知动作标识：" + flag);
        }
    }

    @Override
    public void randomChange() {
        if (actionFlag == Action.BALL) {
            return;
        }
        int i = random.nextInt(MAX_IMAGES_INDEX);
        Action randomAction = randomChangeList[i];
        // 如果是球说明正在移动，就不变化
        if (Action.BALL != randomAction) {
            actionChange(randomAction);
        }
        //提交下次随机变化的时间
        int interval = random.nextInt(MAX_RANDOM_CHANGE_INTERVAL) + 10;
        getHandler().postDelayed(this::randomChange, interval);
    }

    /**
     * 触屏事件
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getRawX();
        touchY = event.getRawY();
        int bmpW = getBmpW(), bmpH = getBmpH();
        switch (event.getAction()) {
            //点击
            case MotionEvent.ACTION_DOWN:
                randomChange();
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                break;
            //移动
            case MotionEvent.ACTION_MOVE:
                actionChange(Action.BALL);
                float currentX = event.getX();
                float currentY = event.getY();
                float deltaY = currentY - lastTouchY;
                // 大于0向下移动反之向上移动
                upOrDown = deltaY > 0 ? 1 : -1;
                lastTouchX = currentX;
                lastTouchY = currentY;
                //触摸点的显示作用
                x = touchX - bmpW / 2;
                y = touchY - bmpH / 2;
                break;
            //抬起
            case MotionEvent.ACTION_UP:
                actionChange(Action.STAND);
                upOrDown = 1;
                break;
        }
        return true;
    }

    private void clearImages() {
        for (int i = 0; i < MAX_IMAGES_INDEX; i++) {
            actionImages[i] = null;
        }
    }

    class RandomChangeRunnable implements Runnable {
        @Override
        public void run() {
            randomChange();
        }
    }

    enum Action implements ActionFlag {
        STAND,
        FIGHT,
        COLD,
        BALL,
        HELLO,
        DOUBT,
    }

}
