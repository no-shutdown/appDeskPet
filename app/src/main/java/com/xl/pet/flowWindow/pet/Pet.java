package com.xl.pet.flowWindow.pet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.View;

/**
 * 抽象宠物父类
 */
public abstract class Pet extends View implements Actionable{

    //动画标识
    protected Actionable.Action actionFlag;
    //动画标识是否需要被渲染
    protected boolean needDraw;
    //画笔
    protected Paint paint;
    //资源
    protected Resources res;
    //界面大小
    protected int screenW, screenH;
    //人物图片的宽高
    protected int bmpW, bmpH;
    //动画大小比例
    protected float personSize = 1.0f;
    //当前坐标位置
    protected float x, y;


    public Pet(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    //画出view
    protected void canvasDraw(Canvas canvas, Bitmap bitmap, Matrix matrix, Paint paint) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, matrix, paint);
        }
    }

    protected Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }

    public void measureScreen() {
        screenH = res.getDisplayMetrics().heightPixels;
        screenW = res.getDisplayMetrics().widthPixels;
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
