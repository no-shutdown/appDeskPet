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

public class BuildingView extends View {

    //预设大小（dp）
    private static final int WIDTH_DP = 180;
    private static final int HEIGHT_DP = 200;
    //实际大小（px）
    private final int bmpW;
    private final int bmpH;

    //图片
    protected Bitmap fieldBitmap;
    //绘图矩阵
    protected Matrix matrix = new Matrix();
    //画笔
    protected Paint paint = new Paint();
    //资源
    protected Resources res;


    //n*m
    public int n;
    public int m;

    public BuildingView(Context context) {
        super(context);
        float scale = 0.3f;
        res = context.getResources();
        matrix.setScale(scale, scale);
        bmpW = dipToPx(context, WIDTH_DP * scale);
        bmpH = dipToPx(context, HEIGHT_DP * scale);
    }

    public BuildingView(Context context, float scale) {
        super(context);
        res = context.getResources();
        matrix.setScale(scale, scale);
        bmpW = dipToPx(context, WIDTH_DP * scale);
        bmpH = dipToPx(context, HEIGHT_DP * scale);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        fieldBitmap = Utils.decodeResource(res, R.drawable.tree);
        canvas.drawColor(Color.BLUE);
        if (fieldBitmap != null) {
            canvas.drawBitmap(fieldBitmap, matrix, paint);
        }
    }

    private int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int getBmpW() {
        return bmpW;
    }

    public int getBmpH() {
        return bmpH;
    }
}
