package com.xl.pet.ui.home;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

import com.xl.pet.R;
import com.xl.pet.utils.Utils;

/**
 * 建筑组件
 */
public class BuildingView extends View {

    //预设大小（dp）
    private static final int WIDTH_DP = 105; //固定宽度
    private static int HEIGHT_DP;//根据图片比例计算预设高度
    //实际大小（px）
    private final int bmpW;
    private final int bmpH;

    //图片
    protected Bitmap buildingBitmap;
    //绘图矩阵
    protected Matrix matrix = new Matrix();
    //画笔
    protected Paint paint = new Paint();
    //资源
    protected Resources res;
    //透明度 (0 完全透明 | 255 完全可见)
    protected int alpha = 255;


    //n*m
    public int n;
    public int m;

    public BuildingView(Context context, float scale, int resId, int n, int m) {
        super(context);
        res = context.getResources();
        buildingBitmap = Utils.decodeResource(res, resId);
        HEIGHT_DP = (int) (1.0f * buildingBitmap.getHeight() / buildingBitmap.getWidth() * WIDTH_DP);
        bmpW = dipToPx(context, WIDTH_DP * scale);
        bmpH = dipToPx(context, HEIGHT_DP * scale);
        this.n = n;
        this.m = m;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (buildingBitmap != null) {
            // 计算缩放比例，使得图片适应画布的尺寸
            float scaleX = (float) getWidth() / buildingBitmap.getWidth();
            float scaleY = (float) getHeight() / buildingBitmap.getHeight();
            float imgScale = Math.min(scaleX, scaleY); // 使用最小的缩放比例，以保持图片完整显示
            matrix.setScale(imgScale, imgScale);
            paint.setAlpha(alpha);
            canvas.drawBitmap(buildingBitmap, matrix, paint);
        }
    }

    //透明
    public void doAlpha() {
        alpha = 100;
        invalidate();
    }

    //取消透明
    public void undoAlpha() {
        alpha = 255;
        invalidate();
    }

    public int getBmpW() {
        return bmpW;
    }

    public int getBmpH() {
        return bmpH;
    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
