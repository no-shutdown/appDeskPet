package com.xl.pet.ui.forest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

import com.xl.pet.R;
import com.xl.pet.utils.Utils;

/**
 * 单个田地组件
 */
public class FieldView extends View {

    //预设大小（dp）
    private static final int WIDTH_DP = 210;
    private static final int HEIGHT_DP = 145;
    //实际大小（px）
    protected final int bmpW;
    protected final int bmpH;


    //绘图矩阵
    protected final Matrix matrix = new Matrix();
    //画笔
    protected final Paint paint = new Paint();
    //资源
    protected final Resources res;
    //图片
    protected Bitmap fieldBitmap;

    //是否高亮
    protected boolean light;
    //是否空闲
    protected boolean free = true;
    //圆点半径
    protected int radius = 10;
    //高亮画笔
    protected Paint lightPaint = new Paint();

    public FieldView(Context context) {
        super(context);
        float scale = 0.3f;
        res = context.getResources();
        matrix.setScale(scale, scale);
        bmpW = dipToPx(context, WIDTH_DP * scale);
        bmpH = dipToPx(context, HEIGHT_DP * scale);
    }

    public FieldView(Context context, float scale) {
        super(context);
        res = context.getResources();
        matrix.setScale(scale, scale);
        bmpW = dipToPx(context, WIDTH_DP * scale);
        bmpH = dipToPx(context, HEIGHT_DP * scale);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(Color.BLUE);
        fieldBitmap = Utils.decodeResource(res, R.drawable.forest_field);
        if (fieldBitmap != null) {
            canvas.drawBitmap(fieldBitmap, matrix, paint);
        }
        if (light) {
            float offset = radius / 2f;
            canvas.drawCircle(bmpW / 2f - offset, bmpH / 2f - offset, radius, lightPaint);
        }
    }

    public void unLight() {
        this.light = false;
        invalidate();
    }

    public void light() {
        this.light = true;
        lightPaint.setColor(free ? 0xFF4169E1 : 0xFFDB7093);
        lightPaint.setAlpha(200); // 设置透明度
        invalidate();
    }

    private int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
