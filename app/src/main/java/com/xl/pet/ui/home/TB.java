package com.xl.pet.ui.home;


import com.xl.pet.R;

import java.util.ArrayList;
import java.util.List;

public class TB {

    public static List<Mode> modes = new ArrayList<>();

    static {
        modes.add(new Mode(0, 0, R.drawable.tree3));
        modes.add(new Mode(0, 1, R.drawable.tree3));
        modes.add(new Mode(0, 2, R.drawable.tree3));
        modes.add(new Mode(0, 3, R.drawable.tree3));
        modes.add(new Mode(1, 0, R.drawable.room1,
                0.5596f, 0.4774f, 0.41f, 1, 3));
        modes.add(new Mode(3, 0, R.drawable.car,
                0.2344f, 0f, 0.7619f, 4, 3));
    }

    static class Mode {
        public int xI;
        public int yI;
        public int resId;
        public int n = 1;
        public int m = 1;

        public float widthP;
        public float offsetX;
        public float offsetY;

        public Mode(int xI, int yI, int resId) {
            this.xI = xI;
            this.yI = yI;
            this.resId = resId;
        }

        public Mode(int xI, int yI, int resId, float offsetX, float offsetY, float widthP, int n, int m) {
            this.xI = xI;
            this.yI = yI;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.widthP = widthP;
            this.resId = resId;
            this.n = n;
            this.m = m;
        }
    }

}
