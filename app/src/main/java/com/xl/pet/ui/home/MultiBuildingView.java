package com.xl.pet.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;

@SuppressLint("ViewConstructor")
public class MultiBuildingView extends AbstractBuildingView {

    //n*m
    public int n;
    public int m;

    public MultiBuildingView(Context context, float scale, int resId, int n, int m, int widthDP) {
        super(context, scale, resId, widthDP);
        this.n = n;
        this.m = m;
    }


}
