package com.xl.pet.ui.home;

import android.view.MotionEvent;
import android.view.View;

import com.xl.pet.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FieldViewGroupTouchListener implements View.OnTouchListener {
    private float touchX;
    private float touchY;

    private BuildingView buildingView;
    private final FieldView[][] fieldViews;


    private final int[] minDistancePoint = new int[2];
    private final int[] minDistanceIndex = new int[2];
    private final List<FieldViewGroup.FieldLight> lightFields = new ArrayList<>();

    public FieldViewGroupTouchListener(FieldViewGroup fieldViewGroup, BuildingView buildingView) {
        this.buildingView = buildingView;
        fieldViews = fieldViewGroup.fieldViews;
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
                unLightOld();
                lightNew(touchX, touchY, buildingView.n, buildingView.m);
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("123");
                break;
            case MotionEvent.ACTION_UP:
                System.out.println(456);
                break;

        }
        return true;
    }

    private void unLightOld() {
        for (FieldViewGroup.FieldLight fieldLight : lightFields) {
            fieldViews[fieldLight.xI][fieldLight.yI].unLight();
        }
    }

    private void lightNew(float x, float y, int n, int m) {
        lightFields.clear();
        FieldViewGroup.FieldLight lightField = findRecentlyField(x, y);
        lightFields.add(lightField);
        fieldViews[lightField.xI][lightField.yI].light(lightField.free);
        //TODO 寻找周边区域
    }

    private FieldViewGroup.FieldLight findRecentlyField(float x, float y) {
        double minDistance = -1;
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
        return new FieldViewGroup.FieldLight(minDistanceIndex[0], minDistanceIndex[1], true);
    }
}
