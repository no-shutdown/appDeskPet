package com.xl.pet.ui.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DividerView extends View {
    private Paint paint;

    public DividerView(Context context) {
        super(context);
        init();
    }

    public DividerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DividerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 创建画笔并设置属性
        paint = new Paint();
        paint.setColor(0xFFBCBCBC);
        paint.setStrokeWidth(2); // 分割线的宽度
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制一条水平的分割线
        int cellWidth = getWidth() / 18;
        canvas.drawLine(cellWidth, 0, cellWidth * 17, 0, paint);

    }
}
