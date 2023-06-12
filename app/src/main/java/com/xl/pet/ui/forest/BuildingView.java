package com.xl.pet.ui.forest;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * 建筑组件
 */
@SuppressLint("ViewConstructor")
public class BuildingView extends AbstractBuildingView {


    public BuildingView(Context context, float scale, int resId) {
        super(context, scale, resId, 170);
    }

    public BuildingView(Context context, float scale, int resId, int widthDP) {
        super(context, scale, resId, widthDP);
    }


}
