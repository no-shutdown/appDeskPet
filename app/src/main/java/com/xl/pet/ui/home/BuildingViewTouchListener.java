package com.xl.pet.ui.home;

import android.view.MotionEvent;
import android.view.View;

import com.xl.pet.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 建筑触摸监听器
 */
public class BuildingViewTouchListener implements View.OnTouchListener {
    private float touchX;
    private float touchY;

    private BuildingView buildingView;
    private final AreaViewGroup areaViewGroup;


    private final int[] minDistancePoint = new int[2];
    private final int[] minDistanceIndex = new int[2];
    private final List<AreaViewGroup.FieldLight> findLightFields = new ArrayList<>();

    public BuildingViewTouchListener(AreaViewGroup areaViewGroup, BuildingView buildingView) {
        this.buildingView = buildingView;
        this.areaViewGroup = areaViewGroup;
    }

    public void setBuildingView(BuildingView buildingView) {
        this.buildingView = buildingView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        touchX = event.getRawX();
        touchY = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                areaViewGroup.buildingDoAlpha();
                break;
            case MotionEvent.ACTION_MOVE:
                areaViewGroup.light(finLightFields(touchX, touchY, buildingView.n, buildingView.m));
                break;
            case MotionEvent.ACTION_UP:
                areaViewGroup.buildingUndoAlpha();
                areaViewGroup.unLight();
                break;
        }
        return true;
    }


    private List<AreaViewGroup.FieldLight> finLightFields(float x, float y, int n, int m) {
        findLightFields.clear();
        AreaViewGroup.FieldLight lightField = findRecentlyField(x, y);
        findLightFields.add(lightField);
        //TODO 寻找周边区域
        return findLightFields;
    }

    private AreaViewGroup.FieldLight findRecentlyField(float x, float y) {
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
        return new AreaViewGroup.FieldLight(minDistanceIndex[0], minDistanceIndex[1], true);
    }
}
