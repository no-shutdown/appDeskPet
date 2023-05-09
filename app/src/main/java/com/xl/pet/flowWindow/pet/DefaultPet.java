package com.xl.pet.flowWindow.pet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import com.xl.pet.R;
import com.xl.pet.constants.Constants;



public class DefaultPet extends Pet {

    //主图
    private Bitmap[] bmpImage = new Bitmap[3];
    //公共矩阵
    protected Matrix matrix;
    //左右判别，向左1，右-1。默认左
    protected int leftOrRight = 1;
    //上下判别，向上1，下-1
    protected int upOrDown;


    public DefaultPet(Context context) {
        super(context);
        //初始化画笔
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(20);
        paint.setAntiAlias(true);        //抗边缘锯齿
        paint.setFilterBitmap(true);    //滤波过滤
        setFocusable(true);
        res = context.getResources();
        //获取界面大小
        measureScreen();
        bmpW = decodeResource(res, R.drawable.chopper_ball).getWidth();
        bmpH = decodeResource(res, R.drawable.chopper_ball).getHeight();
        //初始化的xy,显示在屏幕上的位置
        x = bmpW / 2;
        y = bmpH / 2;
        actionFlag = Action.ACTION_SIT; //默认动作是坐下
        needDraw = false; //因为初始化view的时候会触发onDraw，所以无需在drawRunnable 线程中重复绘画
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        draw(canvas, paint);
    }

    /**
     * 绘图
     */
    private void draw(Canvas canvas, Paint paint) {
        //公用的设置
        float dis = (1 - personSize) * bmpW / 2;
        //Matrix只实现了缩放功能
        matrix = new Matrix();
        ///第一个参数的正负会改变图片的左右镜面对称，主要用在触摸拖动中，以及球形滚动时，行走时。
        matrix.postScale(personSize * leftOrRight, personSize, bmpW / 2, bmpH / 2);
        matrix.postTranslate(-dis, -dis);
        switch (actionFlag) {
            case ACTION_SIT:
                actionSit(canvas, paint);
                break;
             default:
                 Log.i(Constants.LOG_TAG,"未知动作，" + actionFlag);
        }
    }



    @Override
    public void actionSit(Canvas canvas, Paint paint) {
        bmpImage[0] = decodeResource(res, R.drawable.chopper_sit);
        bmpImage[1] = null;
        bmpImage[2] = null;
        canvasDraw(canvas, bmpImage[0], matrix, paint);
    }

    @Override
    public void randomChange() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

}
