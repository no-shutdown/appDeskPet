package com.xl.pet.flowWindow.pet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;

import com.xl.pet.flowWindow.pet.action.Actionable;

/**
 * 抽象宠物父类
 */
public abstract class Pet extends View implements Actionable {

    //每帧间隔，默认150
    private static final int FRAME_TIME = 150;

    //画笔
    protected Paint paint;
    //图片
    protected Bitmap bitmapFPS;
    //资源
    protected Resources res;
    //界面大小
    protected int screenW, screenH;
    //人物图片的宽高
    protected int bmpW, bmpH;
    //绘图矩阵
    protected Matrix matrix = new Matrix();
    //动画大小比例
    protected float personSize = 1.0f;
    //当前坐标位置
    protected float x, y;

    private final Handler handler = new Handler();
    //得到每一帧的bitMap刷新view
    private final DrawRunnable drawRunnable = new DrawRunnable();

    public Pet(Context context) {
        super(context);
        init(context);
        handler.postDelayed(drawRunnable, FRAME_TIME);
    }

    private void init(Context context) {
        //初始化画笔
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(20);
        paint.setAntiAlias(true);        //抗边缘锯齿
        paint.setFilterBitmap(true);    //滤波过滤
        setFocusable(true);
        res = context.getResources();
        //获取界面大小
        screenH = res.getDisplayMetrics().heightPixels;
        screenW = res.getDisplayMetrics().widthPixels;
        //默认资源大小
        bmpW = 300;
        bmpH = 300;
        //默认坐标
        x = 100;
        y = 200;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        actionFPS();
        if (bitmapFPS != null) {
            canvas.drawBitmap(bitmapFPS, matrix, paint);
        }
    }

    //每一帧的动画
    protected abstract void actionFPS();

    //每一帧刷新后的回调
    protected abstract void refreshFPSCallback();

    //停止渲染
    public void destroy() {
        handler.removeCallbacksAndMessages(null);
    }

    class DrawRunnable implements Runnable {
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            try {
                invalidate();    //进行重绘，会再次调用onDraw
                refreshFPSCallback();
                long end = System.currentTimeMillis();
                if (end - start < FRAME_TIME) {
                    handler.postDelayed(this, FRAME_TIME - (end - start));
                } else {
                    handler.post(this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getBmpW() {
        return (int) Math.ceil(bmpW * personSize);
    }

    public int getBmpH() {
        return (int) Math.ceil(bmpH * personSize);
    }
}
