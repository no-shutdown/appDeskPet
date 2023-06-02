package com.xl.pet.ui.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 区域组件
 */
public class AreaViewGroup extends RelativeLayout {

    public FieldView[][] fieldViews;
    public BuildingView[][] buildingViews;

    //当前高亮区域
    private final List<FieldLight> fieldLights = new ArrayList<>();
    //建筑是否透明
    private boolean buildingAlpha;

    //顶部预留空间
    private static final int MAX_HEIGHT = 150;

    public AreaViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFields(context);
    }

    //绘制区域内的子组件
    private void createFields(Context context) {
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int n = 6; //n*n
        float scale = computeScale(widthPixels, n, 297); //计算缩放比例 297是图片长宽的斜边dp，即x轴长度
        int offset_top = dipToPx(60 * scale); //偏移量 60固定偏移量才能刚好重合
        int offset_left = dipToPx(103 * scale); //偏移量 103固定偏移量才能刚好重合

        fieldViews = new FieldView[n][n];
        buildingViews = new BuildingView[n][n];
        int startMarginLeft = (n - 1) * offset_left;
        for (int i = 0; i < n; i++) {
            int baseTop = MAX_HEIGHT + offset_top * i;
            int baseLeft = startMarginLeft - offset_left * i;
            for (int j = 0; j < n; j++) {
                // field
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

                // building
                BuildingView buildingView = new BuildingView(context, scale);
                RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                int offsetYByField = buildingView.getBmpH() - fieldView.getBmpH() / 2;
                int offsetXByField = fieldView.getBmpW() / 2 - buildingView.getBmpW() / 2;
                layoutParams1.topMargin = baseTop + offset_top * j - offsetYByField;
                layoutParams1.leftMargin = baseLeft + offset_left * j + offsetXByField;
                layoutParams1.width = buildingView.getBmpW();
                layoutParams1.height = buildingView.getBmpH();
                buildingView.setLayoutParams(layoutParams1);
                buildingViews[i][j] = buildingView;
                this.addView(buildingView);
            }
        }
    }

    //所有building透明
    public void buildingDoAlpha() {
        if (!buildingAlpha) {
            for (BuildingView[] row : buildingViews) {
                for (BuildingView buildingView : row) {
                    buildingView.doAlpha();
                }
            }
            buildingAlpha = true;
        }
    }

    //所有building不透明
    public void buildingUndoAlpha() {
        if (buildingAlpha) {
            //building不透明
            for (BuildingView[] row : buildingViews) {
                for (BuildingView buildingView : row) {
                    buildingView.undoAlpha();
                }
            }
            buildingAlpha = false;
        }
    }

    //高亮区域
    public void light(List<FieldLight> findFieldLights) {
        unLight();
        //高亮新的特定区域
        fieldLights.addAll(findFieldLights);
        for (FieldLight fieldLight : fieldLights) {
            fieldViews[fieldLight.xI][fieldLight.yI].light(fieldLight.free);
        }
    }

    //取消高亮
    public void unLight() {
        //取消特定区域的高亮
        for (FieldLight fieldLight : fieldLights) {
            fieldViews[fieldLight.xI][fieldLight.yI].unLight();
        }
        fieldLights.clear();
    }

    private float computeScale(int parentWidth, int n, int viewDp) {
        //100px伸缩空间
        return 1.0f * (parentWidth - 100) / n / viewDp / 2;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int dipToPx(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static class FieldLight {
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
