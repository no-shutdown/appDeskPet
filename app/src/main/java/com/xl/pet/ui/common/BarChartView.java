package com.xl.pet.ui.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BarChartView extends View {
    private static final int BAR_WIDTH = 50;
    private static final int MAX_BAR_HEIGHT = 400;
    private static final int SPACE_BETWEEN_BARS = 20;

    private int[] data; // 柱状图的数据

    public BarChartView(Context context) {
        super(context);
        init();
    }

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        data = new int[]{100, 200, 300, 400, 500}; // 示例数据
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int numBars = data.length;
        int totalBarWidth = (BAR_WIDTH + SPACE_BETWEEN_BARS) * numBars - SPACE_BETWEEN_BARS;
        int startX = (getWidth() - totalBarWidth) / 2;
        int startY = getHeight() - MAX_BAR_HEIGHT;

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);

        for (int i = 0; i < numBars; i++) {
            int barHeight = (int) (((float) data[i] / MAX_BAR_HEIGHT) * MAX_BAR_HEIGHT);
            int left = startX + i * (BAR_WIDTH + SPACE_BETWEEN_BARS);
            int right = left + BAR_WIDTH;
            int top = startY + (MAX_BAR_HEIGHT - barHeight);
            int bottom = startY + MAX_BAR_HEIGHT;

            canvas.drawRect(left, top, right, bottom, paint);
        }
    }
}
