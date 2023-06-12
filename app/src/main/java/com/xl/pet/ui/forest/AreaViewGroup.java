package com.xl.pet.ui.forest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xl.pet.R;
import com.xl.pet.ui.forest.mode.BuildingMode;
import com.xl.pet.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 区域组件
 */
public class AreaViewGroup extends RelativeLayout {

    public FieldView[][] fieldViews;
    public AbstractBuildingView[][] buildingViews;

    //modes
    private List<BuildingMode.Mode> data;
    //n*n
    private int n;
    //缩放
    private float scale;
    //起点左边距
    private int startMarginLeft;
    //field重合偏移量
    private int offset_top;
    private int offset_left;

    //当前高亮区域
    private final List<FieldPoint> fieldPoints = new ArrayList<>();
    //建筑是否透明
    private boolean buildingAlpha;

    //顶部预留空间
    private static final int PRE_TOP = 150;

    public AreaViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //绘制区域内的子组件
    private void refreshArea(Context context) {
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        scale = computeScale(widthPixels, n, 297); //计算缩放比例 297是图片长宽的斜边dp，即x轴长度
        offset_top = dipToPx(60 * scale); //偏移量 60固定偏移量才能刚好重合
        offset_left = dipToPx(103 * scale); //偏移量 103固定偏移量才能刚好重合

        fieldViews = new FieldView[n][n];
        buildingViews = new AbstractBuildingView[n][n];
        startMarginLeft = (n - 1) * offset_left;

        //fields
        for (int i = 0; i < n; i++) {
            int baseTop = PRE_TOP + offset_top * i;
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
                layoutParams.width = fieldView.bmpW;
                layoutParams.height = fieldView.bmpH;
                fieldView.setLayoutParams(layoutParams);
                fieldViews[i][j] = fieldView;
                this.addView(fieldView);
            }
        }

        // buildings
        if (null == data || data.isEmpty()) {
            createBuilding(new FieldPoint(2, 2), R.drawable.b_none, null);
        } else {
            for (BuildingMode.Mode mode : data) {
                createBuilding(new FieldPoint(mode.xI, mode.yI), mode.resId, mode.multiParam);
            }
        }
    }

    //新建建筑
    public void createBuilding(FieldPoint point, int resId, BuildingMode.MultiParam multiParam) {
        int i = point.i, j = point.j;
        int baseTop = PRE_TOP + offset_top * i;
        int baseLeft = startMarginLeft - offset_left * i;
        FieldView fieldView = fieldViews[i][j];
        AbstractBuildingView buildingView = AbstractBuildingView.buildingView(this.getContext(), scale, resId, multiParam);
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        float offsetYByField = 0, offsetXByField = 0;
        //如果是1*1则移动到中心位置。否则将占地面积的左上角移动到所在地块的左上角
        if (buildingView instanceof BuildingView) {
            //移动到中心位置
            offsetYByField = -1 * (buildingView.bmpH - fieldView.bmpH / 2f);
            offsetXByField = fieldView.bmpW / 2f - buildingView.bmpW / 2f;

            //标记地域忙碌状态
            fieldViews[i][j].free = false;
        } else if (buildingView instanceof MultiBuildingView) {
            MultiBuildingView multiBuildingView = (MultiBuildingView) buildingView;
            //占地参考点（占地区域的左上角）移动到building图片左上角
            offsetYByField = buildingView.bmpH * multiParam.offsetY;
            offsetXByField = buildingView.bmpW * multiParam.offsetX;
            //再移动到field显示效果的左上角
            offsetXByField = offsetXByField + (fieldView.bmpW / 2f);

            //标记地域忙碌状态
            fieldViews[i][j].free = false;
            for (int mm = 0; mm < multiBuildingView.m; mm++) {
                for (int nn = 0; nn < multiBuildingView.n; nn++) {
                    fieldViews[i + mm][j + nn].free = false;
                }
            }
        }
        layoutParams1.topMargin = (int) (baseTop + offset_top * j + offsetYByField);
        layoutParams1.leftMargin = (int) (baseLeft + offset_left * j + offsetXByField);
        layoutParams1.width = buildingView.bmpW;
        layoutParams1.height = buildingView.bmpH;
        buildingView.setLayoutParams(layoutParams1);
        buildingViews[i][j] = buildingView;
        this.addView(buildingView);
    }

    //刷新参数点以下的所有建筑，避免后面的建筑显示在前面
    public void refreshAreaAfterPoint(FieldPoint point) {
        boolean first = true;
        for (int i = point.i; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (first) {
                    first = false;
                    continue;
                }
                if (null == buildingViews[i][j]) {
                    continue;
                }
                this.removeView(buildingViews[i][j]);
                this.addView(buildingViews[i][j]);
            }
        }
    }

    //所有building透明
    public void buildingDoAlpha() {
        if (!buildingAlpha) {
            for (AbstractBuildingView[] row : buildingViews) {
                if (null == row) {
                    continue;
                }
                for (AbstractBuildingView buildingView : row) {
                    if (null == buildingView) {
                        continue;
                    }
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
            for (AbstractBuildingView[] row : buildingViews) {
                if (null == row) {
                    continue;
                }
                for (AbstractBuildingView buildingView : row) {
                    if (null == buildingView) {
                        continue;
                    }
                    buildingView.undoAlpha();
                }
            }
            buildingAlpha = false;
        }
    }

    //高亮区域
    public void light(List<FieldPoint> findFieldPoints) {
        unLight();
        //高亮新的特定区域
        fieldPoints.addAll(findFieldPoints);
        for (FieldPoint fieldPoint : fieldPoints) {
            fieldViews[fieldPoint.i][fieldPoint.j].light();
        }
    }

    //取消高亮
    public void unLight() {
        //取消特定区域的高亮
        for (FieldPoint fieldPoint : fieldPoints) {
            fieldViews[fieldPoint.i][fieldPoint.j].unLight();
        }
        fieldPoints.clear();
    }

    //判断区域是否全部可用，如果为空，则返回false
    public boolean isAllFieldsFree(List<FieldPoint> findFieldPoints) {
        if (findFieldPoints.isEmpty()) {
            return false;
        }
        for (FieldPoint findFieldPoint : findFieldPoints) {
            if (!fieldViews[findFieldPoint.i][findFieldPoint.j].free) {
                return false;
            }
        }
        return true;
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

    public void setData(List<BuildingMode.Mode> data) {
        this.data = data;
        n = Math.max(5, Utils.getMinSquare(data.size()));
        removeAllViews();
        refreshArea(getContext());
        requestLayout();
        invalidate();

    }

    public int getN() {
        return n;
    }

    public static class FieldPoint {
        public int i;
        public int j;

        public FieldPoint(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

}
