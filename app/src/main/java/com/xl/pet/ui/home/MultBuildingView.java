package com.xl.pet.ui.home;

import android.content.Context;

public class MultBuildingView extends BaseBuildingView {

    //n*m
    public int n;
    public int m;

    public MultBuildingView(Context context, float scale, int resId, int n, int m, float widthP) {
        super(context, scale, resId, (int) (n * 105 / widthP));
        this.n = n;
        this.m = m;
    }


}
