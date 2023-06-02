package com.xl.pet.ui.home;

import android.content.Context;

public class MultBuildingView extends BaseBuildingView {

    //n*m
    public int n;
    public int m;

    public MultBuildingView(Context context, float scale, int resId, int n, int m) {
        super(context, scale, resId, n * 190);
        this.n = n;
        this.m = m;
    }


}
