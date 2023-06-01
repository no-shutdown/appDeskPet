package com.xl.pet.ui.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class FieldViewGroup extends RelativeLayout {

    private float touchX;
    private float touchY;
    private DraggableView draggableView;
    private FieldView[][] fieldViews;

    public FieldViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFields(context);
    }

    private void createFields(Context context) {
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int n = 8; //n*n
        float scale = computeScale(widthPixels, n, 297); //计算缩放比例 297是图片长宽的斜边dp，即x轴长度
        int offset_top = dipToPx(60 * scale); //偏移量 60固定偏移量才能刚好重合
        int offset_left = dipToPx(103 * scale); //偏移量 103固定偏移量才能刚好重合

        fieldViews = new FieldView[n][n];
        int startMarginLeft = (n - 1) * offset_left;
        for (int i = 0; i < n; i++) {
            int baseTop = offset_top * i;
            int baseLeft = startMarginLeft - offset_left * i;
            for (int j = 0; j < n; j++) {
                FieldView fieldView = new FieldView(context, scale);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                layoutParams.topMargin = baseTop + offset_top * j;
                layoutParams.leftMargin = baseLeft + offset_left * j;
                layoutParams.width = fieldView.getBmpW();
                layoutParams.height = fieldView.getBmpH();
                fieldView.setLayoutParams(layoutParams);
                fieldViews[i][j] = fieldView;
                this.addView(fieldView);
            }
        }


        fieldViews[n - 1][n - 1].setOnTouchListener(new DraggableViewTouchListener());
    }


    private float computeScale(int parentWidth, int n, int viewDp) {
        //100px伸缩空间
        return 1.0f * (parentWidth - 100) / n / viewDp / 2;
    }

    private int dipToPx(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public class DraggableViewTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            DraggableView draggableView = (DraggableView) v;
            touchX = event.getRawX();
            touchY = event.getRawY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    List<FieldLight> fieldLights = computeLight(touchX, touchY, draggableView.n, draggableView.m);
                    for (FieldLight fieldLight : fieldLights) {
                        fieldViews[fieldLight.xI][fieldLight.yI].light(fieldLight.free);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;

            }
            return true;
        }
    }

    private List<FieldLight> computeLight(float x, float y, int n, int m) {
        List<FieldLight> result = new ArrayList<>();
        result.add(new FieldLight(1, 1, true));
        result.add(new FieldLight(2, 1, true));
        result.add(new FieldLight(1, 2, true));
        result.add(new FieldLight(2, 2, false));
        return result;
    }

    protected static class FieldLight {
        public int xI;
        public int yI;
        public boolean free;

        public FieldLight(int xI, int yI, boolean free) {
            this.xI = xI;
            this.yI = yI;
            this.free = free;
        }
    }

}
