package com.xl.pet.ui.forest.listener;

import static com.xl.pet.ui.forest.mode.BuildingMode.MULTI_PARAM_MAP;

import android.view.MotionEvent;
import android.view.View;

import com.xl.pet.ui.forest.AreaViewGroup;
import com.xl.pet.ui.forest.BuildingView;
import com.xl.pet.ui.forest.FieldView;
import com.xl.pet.ui.forest.MultiBuildingView;
import com.xl.pet.ui.forest.mode.BuildingMode;
import com.xl.pet.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 暂时不用这个类
 * 建筑触摸监听器（可将building移动到对应的点上）
 */
public class BuildingViewTouchListener implements View.OnTouchListener {

    private final AreaViewGroup areaViewGroup;


    private final int[] minDistancePoint = new int[2];
    private final int[] minDistanceIndex = new int[2];
    private final List<AreaViewGroup.FieldPoint> findFields = new ArrayList<>();

    public BuildingViewTouchListener(AreaViewGroup areaViewGroup) {
        this.areaViewGroup = areaViewGroup;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float touchX = event.getRawX();
        float touchY = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //建筑透明
                areaViewGroup.buildingDoAlpha();
                break;
            case MotionEvent.ACTION_MOVE:
                //高亮区域（偏移量是为了高亮点不被手指挡住）
                if (v instanceof BuildingView) {
                    areaViewGroup.light(finLightFields(touchX - 150, touchY + 100));
                } else if (v instanceof MultiBuildingView) {
                    MultiBuildingView multiBuildingView = (MultiBuildingView) v;
                    areaViewGroup.light(finLightFields(touchX - 150, touchY + 100, multiBuildingView.n, multiBuildingView.m));
                }
                break;
            case MotionEvent.ACTION_UP:
                //取消建筑透明
                areaViewGroup.buildingUndoAlpha();
                //取消区域高亮
                areaViewGroup.unLight();
                //如果全部空间空闲，则新建建筑
                if (areaViewGroup.isAllFieldsFree(findFields)) {
                    AreaViewGroup.FieldPoint point = findFields.get(0);
                    if (v instanceof BuildingView) {
                        areaViewGroup.createBuilding(point, ((BuildingView) v).resId, null);
                        areaViewGroup.refreshAreaAfterPoint(point);
                    } else if (v instanceof MultiBuildingView) {
                        MultiBuildingView multiBuildingView = (MultiBuildingView) v;
                        BuildingMode.MultiParam multiParam = MULTI_PARAM_MAP.get(multiBuildingView.resId);
                        areaViewGroup.createBuilding(point, multiBuildingView.resId, multiParam);
                        areaViewGroup.refreshAreaAfterPoint(point);
                    }
                }
                break;
        }
        return true;
    }


    private List<AreaViewGroup.FieldPoint> finLightFields(float x, float y) {
        findFields.clear();
        AreaViewGroup.FieldPoint lightField = findRecentlyField(x, y);
        findFields.add(lightField);
        return findFields;
    }

    private List<AreaViewGroup.FieldPoint> finLightFields(float x, float y, int n, int m) {
        findFields.clear();
        AreaViewGroup.FieldPoint lightField = findRecentlyField(x, y);
        int groupN = areaViewGroup.getN();
        int i = Math.min(lightField.i, groupN - m), j = Math.min(lightField.j, groupN - n);
        for (int mm = 0; mm < m; mm++) {
            for (int nn = 0; nn < n; nn++) {
                findFields.add(new AreaViewGroup.FieldPoint(i + mm, j + nn));
            }
        }
        return findFields;
    }

    private AreaViewGroup.FieldPoint findRecentlyField(float x, float y) {
        double minDistance = -1;
        FieldView[][] fieldViews = areaViewGroup.fieldViews;
        for (int i = 0; i < fieldViews.length; i++) {
            FieldView[] row = fieldViews[i];
            for (int j = 0; j < row.length; j++) {
                FieldView view = row[j];
                view.getLocationOnScreen(minDistancePoint);
                double distance = Utils.distance(x, y, minDistancePoint[0], minDistancePoint[1]);
                if (-1 == minDistance || distance < minDistance) {
                    minDistance = distance;
                    minDistanceIndex[0] = i;
                    minDistanceIndex[1] = j;
                }
            }
        }
        return new AreaViewGroup.FieldPoint(minDistanceIndex[0], minDistanceIndex[1]);
    }
}
