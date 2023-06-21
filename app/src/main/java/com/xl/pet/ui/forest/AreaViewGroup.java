package com.xl.pet.ui.forest;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.xl.pet.R;
import com.xl.pet.database.entity.ForestDO;
import com.xl.pet.ui.forest.mode.BuildingMode;
import com.xl.pet.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 区域组件
 */
public class AreaViewGroup extends RelativeLayout {

    private static final Random RANDOM = new Random();
    private static final int TREE_MINUTE = 10 * 60 * 1000;

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
            createBuilding(new FieldPoint(n / 2, n / 2), R.drawable.forest_tree_none, null);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setData(int segmentItem, List<ForestDO> rawData) {
        int treeNum = countTreeNum(rawData);
        if (rawData.isEmpty()) n = 5;
        else n = Math.max(minN(segmentItem), Utils.getMinSquare(treeNum));
        this.data = buildBuildingMode(rawData, n);
        removeAllViews();
        refreshArea(getContext());
        requestLayout();
        invalidate();
    }

    private int minN(int segmentItem) {
        if (0 == segmentItem) return 5;
        if (1 == segmentItem) return 8;
        if (2 == segmentItem) return 10;
        if (3 == segmentItem) return 15;
        throw new RuntimeException();
    }

    private int countTreeNum(List<ForestDO> rawData) {
        long sum = 0;
        for (ForestDO i : rawData) {
            sum += (i.endTime - i.startTime);
        }
        return (int) (sum / TREE_MINUTE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<BuildingMode.Mode> buildBuildingMode(List<ForestDO> rawData, int n) {
        List<BuildingMode.Mode> modes = new ArrayList<>();
        Set<FieldPoint> set = new HashSet<>();
        for (int index = 0; index < rawData.size(); index++) {
            ForestDO rawDatum = rawData.get(index);
            int num = (int) ((rawDatum.endTime - rawDatum.startTime) / TREE_MINUTE);
            if (0 == num) {
                //不足时间，显示一颗枯树
                int i = RANDOM.nextInt(n);
                int j = RANDOM.nextInt(n);
                modes.add(new BuildingMode.Mode(i, j, R.drawable.forest_tree_decayed));
            } else {
                for (int treeI = 0; treeI < num; treeI++) {
                    int i = RANDOM.nextInt(n);
                    int j = RANDOM.nextInt(n);
                    AreaViewGroup.FieldPoint fieldPoint = new AreaViewGroup.FieldPoint(i, j);
                    if (set.contains(fieldPoint)) {
                        treeI--;
                        continue;
                    }
                    set.add(fieldPoint);
                    modes.add(new BuildingMode.Mode(i, j, rawDatum.resId));
                }
            }
        }
        Stream<BuildingMode.Mode> sorted = modes.stream().sorted();
        return sorted.collect(Collectors.toList());
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FieldPoint that = (FieldPoint) o;

            if (i != that.i) return false;
            return j == that.j;
        }

        @Override
        public int hashCode() {
            int result = i;
            result = 31 * result + j;
            return result;
        }
    }

}
