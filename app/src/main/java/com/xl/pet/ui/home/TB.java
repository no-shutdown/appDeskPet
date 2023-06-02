package com.xl.pet.ui.home;

import com.xl.pet.R;

import java.util.ArrayList;
import java.util.List;

public class TB {

    public static List<Mode> modes = new ArrayList<>();

    static {
        modes.add(new Mode(0, 0, R.drawable.tree3, 1, 1));
        modes.add(new Mode(0, 1, R.drawable.tree3, 1, 1));
        modes.add(new Mode(0, 2, R.drawable.tree3, 1, 1));
        modes.add(new Mode(0, 3, R.drawable.tree3, 1, 1));
        modes.add(new Mode(3, 2, R.drawable.room_floor, 2, 2));
    }

    static class Mode {
        public int xI;
        public int yI;
        public int resId;
        public int n;
        public int m;

        public Mode(int xI, int yI, int resId, int n, int m) {
            this.xI = xI;
            this.yI = yI;
            this.resId = resId;
            this.n = n;
            this.m = m;
        }
    }

}
