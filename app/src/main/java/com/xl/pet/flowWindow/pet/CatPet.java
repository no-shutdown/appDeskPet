package com.xl.pet.flowWindow.pet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.xl.pet.R;
import com.xl.pet.constants.Constants;
import com.xl.pet.flowWindow.pet.action.ActionFlag;
import com.xl.pet.utils.Utils;

import java.util.Random;


public class CatPet extends Pet {

    //一组图片最大数量
    private static final int MAX_IMAGES_INDEX = 10;
    //随机变动最大的时间间隔
    private static final int MAX_RANDOM_CHANGE_INTERVAL = 60000;
    //随机变动前至少静止的时间间隔
    private static final int MIN_STAND_TIME_BEFORE_RANDOM = 10000;
    //一个动作的时长
    private static final int A_ACTION_INTERVAL = 3000;

    //动画标识
    Action actionFlag = null;

    //随机变动
    private final Handler handler = new Handler();
    private final RandomRunnable randomRunnable = new RandomRunnable();
    Random random = new Random();
    Action[] randomChangeList = Action.values();
    private long lastStandTime;

    //主图
    private final Bitmap[] actionImages = new Bitmap[MAX_IMAGES_INDEX];
    private int actionIndex = 0;
    //左右判别，向左1，右-1。默认左
    protected int leftOrRight = 1;
    //上下判别，向上1，下-1
    protected int upOrDown = 1;

    //点击坐标
    protected float touchX, touchY;
    private long lastTouchTime = 0;
    protected float lastTouchX, lastTouchY;

    private WindowManager bindWindowManager;
    private WindowManager.LayoutParams bindWindowManagerLayoutParams;

    public CatPet(Context context) {
        super(context);
        Bitmap bitmap = Utils.decodeResource(res, R.drawable.cat_stand);
        bmpH = bitmap.getHeight() + 10; //10伸缩空间
        bmpW = bitmap.getWidth() + 30; //30伸缩空间
        personSize = 0.8f; //百分之80比例缩放
        actionChange(Action.STAND);
        handler.postDelayed(randomRunnable, MAX_RANDOM_CHANGE_INTERVAL);
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
                lastStandTime = System.currentTimeMillis();
                actionImages[0] = Utils.decodeResource(res, R.drawable.cat_stand);
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
                this.postDelayed(() -> actionChange(Action.STAND), A_ACTION_INTERVAL);
                break;
            case COLD:
                actionImages[0] = Utils.decodeResource(res, R.drawable.cat_cold_1);
                actionImages[1] = Utils.decodeResource(res, R.drawable.cat_cold_2);
                this.postDelayed(() -> actionChange(Action.STAND), A_ACTION_INTERVAL);
                break;
            case HELLO:
                actionImages[0] = Utils.decodeResource(res, R.drawable.cat_hello_1);
                actionImages[1] = Utils.decodeResource(res, R.drawable.cat_hello_2);
                actionImages[2] = Utils.decodeResource(res, R.drawable.cat_hello_3);
                actionImages[3] = Utils.decodeResource(res, R.drawable.cat_hello_2);
                actionImages[4] = Utils.decodeResource(res, R.drawable.cat_hello_1);
                actionImages[5] = Utils.decodeResource(res, R.drawable.cat_hello_2);
                actionImages[6] = Utils.decodeResource(res, R.drawable.cat_hello_3);
                this.postDelayed(() -> actionChange(Action.STAND), A_ACTION_INTERVAL);
                break;
            case DOUBT:
                actionImages[0] = Utils.decodeResource(res, R.drawable.cat_doubt_1);
                actionImages[1] = Utils.decodeResource(res, R.drawable.cat_doubt_1);
                actionImages[2] = Utils.decodeResource(res, R.drawable.cat_doubt_2);
                actionImages[3] = Utils.decodeResource(res, R.drawable.cat_doubt_2);
                this.postDelayed(() -> actionChange(Action.STAND), A_ACTION_INTERVAL);
                break;
            case AUHAUH:
                actionImages[0] = Utils.decodeResource(res, R.drawable.cat_auhauh_1);
                actionImages[1] = Utils.decodeResource(res, R.drawable.cat_auhauh_2);
                actionImages[2] = Utils.decodeResource(res, R.drawable.cat_auhauh_3);
                actionImages[3] = Utils.decodeResource(res, R.drawable.cat_auhauh_2);
                actionImages[4] = Utils.decodeResource(res, R.drawable.cat_auhauh_1);
                actionImages[5] = Utils.decodeResource(res, R.drawable.cat_auhauh_2);
                actionImages[6] = Utils.decodeResource(res, R.drawable.cat_auhauh_3);
                actionImages[7] = Utils.decodeResource(res, R.drawable.cat_auhauh_2);
                this.postDelayed(() -> actionChange(Action.STAND), A_ACTION_INTERVAL);
                break;
            case EXERCISE:
                actionImages[0] = Utils.decodeResource(res, R.drawable.cat_exercise_1);
                actionImages[1] = Utils.decodeResource(res, R.drawable.cat_exercise_2);
                actionImages[2] = Utils.decodeResource(res, R.drawable.cat_exercise_3);
                actionImages[3] = Utils.decodeResource(res, R.drawable.cat_exercise_4);
                actionImages[4] = Utils.decodeResource(res, R.drawable.cat_exercise_5);
                actionImages[5] = Utils.decodeResource(res, R.drawable.cat_exercise_6);
                actionImages[6] = Utils.decodeResource(res, R.drawable.cat_exercise_7);
                this.postDelayed(() -> actionChange(Action.STAND), A_ACTION_INTERVAL);
                break;
            case CRY:
                actionImages[0] = Utils.decodeResource(res, R.drawable.cat_cry_1);
                actionImages[1] = Utils.decodeResource(res, R.drawable.cat_cry_2);
                this.postDelayed(() -> actionChange(Action.STAND), A_ACTION_INTERVAL);
                break;
            case BALL:
                actionImages[0] = Utils.decodeResource(res, R.drawable.cat_ball_1);
                actionImages[1] = Utils.decodeResource(res, R.drawable.cat_ball_2);
                actionImages[2] = Utils.decodeResource(res, R.drawable.cat_ball_3);
                actionImages[3] = Utils.decodeResource(res, R.drawable.cat_ball_4);
                actionImages[4] = Utils.decodeResource(res, R.drawable.cat_ball_5);
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
        int i = random.nextInt(randomChangeList.length);
        Action randomAction = randomChangeList[i];
        // 如果是球说明正在移动，就不变化
        if (Action.BALL != randomAction) {
            actionChange(randomAction);
        }
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
                // 记录当前点击的时间和位置
                long currentTime = System.currentTimeMillis();
                // 判断是否双击
                if (currentTime - lastTouchTime < 500
                        && Math.abs(touchX - lastTouchX) < 50
                        && Math.abs(touchY - lastTouchY) < 50) {
                    randomChange();
                }
                lastTouchTime = currentTime;
                lastTouchX = touchX;
                lastTouchY = touchY;
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
                if (Action.BALL == actionFlag) {
                    actionChange(Action.STAND);
                    upOrDown = 1;
                }
                break;
        }
        return true;
    }

    class RandomRunnable implements Runnable {

        @Override
        public void run() {
            if (actionFlag == Action.STAND
                    && System.currentTimeMillis() - lastStandTime > MIN_STAND_TIME_BEFORE_RANDOM) {
                randomChange();
            }
            //提交下次随机变化的时间
            int interval = random.nextInt(MAX_RANDOM_CHANGE_INTERVAL);
            handler.postDelayed(this, interval);
        }
    }

    private void clearImages() {
        for (int i = 0; i < MAX_IMAGES_INDEX; i++) {
            actionImages[i] = null;
        }
    }
    enum Action implements ActionFlag {
        STAND,
        FIGHT,
        COLD,
        BALL,
        HELLO,
        DOUBT,
        AUHAUH,
        EXERCISE,
        CRY,
    }

}
