package com.xl.pet.ui.home;


import com.xl.pet.R;

import java.util.ArrayList;
import java.util.List;

public class TB {

    public static List<Mode> modes = new ArrayList<>();

    static {
        modes.add(new Mode(0, 0, R.drawable.b_tree3));
        modes.add(new Mode(0, 1, R.drawable.b_tree3));
        modes.add(new Mode(0, 2, R.drawable.b_tree3));
        modes.add(new Mode(0, 3, R.drawable.b_tree3));
        modes.add(new Mode(0, 4, R.drawable.b_tree3));
        modes.add(new Mode(1, 0, R.drawable.b_tree3));
        modes.add(new Mode(1, 1, R.drawable.b_tree3));
        modes.add(new Mode(2, 0, R.drawable.b_tree3));
        modes.add(new Mode(2, 1, R.drawable.b_tree3));
        modes.add(new Mode(3, 0, R.drawable.b_tree3));
        modes.add(new Mode(3, 1, R.drawable.b_tree3));


        modes.add(new Mode(1, 2, R.drawable.b_room1,
                1, 3, -0.5596f, -0.4774f, 0.41f));
        modes.add(new Mode(3, 6, R.drawable.b_car,
                4, 3, -0.2344f, 0f, 0.7619f));

    }

    static class Mode {
        public int xI; //坐标x
        public int yI; //坐标y
        public int resId; //资源id
        //占地面积 n*m (1*-1表示居中展示)
        public int n = 1;
        public int m = -1;

        /**
         * 以下是占用多块地域图片的偏移参数
         */
        //占地面积n在x轴上的投影长度 / 图片宽度
        public float widthP;
        //占地面积左上角顶点与图片左上角在x轴上偏移量 / 图片宽度
        public float offsetX;
        //占地面积左上角顶点与图片左上角在y轴上偏移量 / 图片高度
        public float offsetY;

        public Mode(int xI, int yI, int resId) {
            this.xI = xI;
            this.yI = yI;
            this.resId = resId;
        }

        public Mode(int xI, int yI, int resId, int n, int m) {
            this.xI = xI;
            this.yI = yI;
            this.resId = resId;
            this.n = n;
            this.m = m;
        }

        public Mode(int xI, int yI, int resId, int n, int m, float offsetX, float offsetY, float widthP) {
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
