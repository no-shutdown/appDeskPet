package com.xl.pet.ui.home;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DraggableViewTouchListener implements View.OnTouchListener {
    private float touchX;
    private float touchY;
    private final FieldViewGroup fieldViewGroup;

    public DraggableViewTouchListener(FieldViewGroup fieldViewGroup) {
        this.fieldViewGroup = fieldViewGroup;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        DraggableView draggableView = (DraggableView) v;
        touchX = event.getRawX();
        touchY = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                List<FieldViewGroup.FieldLight> fieldLights = computeLight(touchX, touchY, draggableView.n, draggableView.m);
                for (FieldViewGroup.FieldLight fieldLight : fieldLights) {
                    fieldViewGroup.fieldViews[fieldLight.xI][fieldLight.yI].light(fieldLight.free);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return true;
    }

    private List<FieldViewGroup.FieldLight> computeLight(float x, float y, int n, int m) {
        List<FieldViewGroup.FieldLight> result = new ArrayList<>();
        result.add(new FieldViewGroup.FieldLight(1, 1, true));
        result.add(new FieldViewGroup.FieldLight(2, 1, true));
        result.add(new FieldViewGroup.FieldLight(1, 2, true));
        result.add(new FieldViewGroup.FieldLight(2, 2, false));
        return result;
    }
}
