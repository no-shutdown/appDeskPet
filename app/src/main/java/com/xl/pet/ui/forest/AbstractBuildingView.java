package com.xl.pet.ui.forest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

import com.xl.pet.ui.forest.mode.BuildingMode;
import com.xl.pet.utils.Utils;

public abstract class AbstractBuildingView extends View {

    //实际大小（px）
    protected final int bmpW;
    protected final int bmpH;

    //资源id
    public final int resId;
    //图片
    protected final Bitmap buildingBitmap;
    //绘图矩阵
    protected final Matrix matrix = new Matrix();
    //画笔
    protected final Paint paint = new Paint();
    //资源
    protected final Resources res;
    //透明度 (0 完全透明 | 255 完全可见)
    protected int alpha = 255;

    public static AbstractBuildingView buildingView(Context context, float scale, int resId) {
        return new BuildingView(context, scale, resId);
    }

    public static AbstractBuildingView buildingView(Context context, float scale, int resId, BuildingMode.MultiParam multiParam) {
        if (null == multiParam) {
            return new BuildingView(context, scale, resId);
        }
        return new MultiBuildingView(context, scale, resId, multiParam.n, multiParam.m, (int) ((multiParam.n * 105) / multiParam.widthP));
    }

    public AbstractBuildingView(Context context, float scale, int resId, int widthDP) {
        super(context);
        this.res = context.getResources();
        this.resId = resId;
        buildingBitmap = Utils.decodeResource(res, resId);

        //根据图片比例计算预设高度
        int heightDP = (int) (1.0f * buildingBitmap.getHeight() / buildingBitmap.getWidth() * widthDP);
        bmpW = dipToPx(context, widthDP * scale);
        bmpH = dipToPx(context, heightDP * scale);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (buildingBitmap != null) {
//            canvas.drawColor(Color.RED);
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

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
