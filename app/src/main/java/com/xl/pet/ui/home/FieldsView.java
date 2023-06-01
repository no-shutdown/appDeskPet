package com.xl.pet.ui.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class FieldsView extends RelativeLayout {

    private FieldView[][] fieldViews;

    public FieldsView(Context context, AttributeSet attrs) {
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

        fieldViews[n - 1][n - 1].setOnTouchListener(new ItemDragTouchListener());

    }

    private float computeScale(int parentWidth, int n, int viewDp) {
        //100px伸缩空间
        return 1.0f * (parentWidth - 100) / n / viewDp / 2;
    }

    private int dipToPx(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public class ItemDragTouchListener implements OnTouchListener {
        private int initialX;
        private int initialY;
        private int offsetX;
        private int offsetY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = (int) event.getX();
                    initialY = (int) event.getY();
                    offsetX = (int) (event.getRawX() - event.getX());
                    offsetY = (int) (event.getRawY() - event.getY());
                    break;
                case MotionEvent.ACTION_MOVE:
                    int newX = (int) event.getX();
                    int newY = (int) event.getY();
                    int deltaX = newX - initialX;
                    int deltaY = newY - initialY;
                    int newRawX = (int) (event.getRawX() - offsetX);
                    int newRawY = (int) (event.getRawY() - offsetY);

                    // 更新被拖拽物品的位置
                    int draggedItemX = calculateDraggedItemX(newRawX);
                    int draggedItemY = calculateDraggedItemY(newRawY);
                    fieldViews[draggedItemX][draggedItemY].freeLight();

                    fieldViews[7][7].hecticLight();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // 清除被拖拽物品的位置
                    break;
            }
            return true;
        }

        private int calculateDraggedItemX(int rawX) {
            // 根据实际需求计算被拖拽物品的横坐标
            // ...
            return 0;
        }

        private int calculateDraggedItemY(int rawY) {
            // 根据实际需求计算被拖拽物品的纵坐标
            // ...
            return 0;
        }
    }

}
