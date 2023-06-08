package com.xl.pet.ui.forest.mode;


import com.xl.pet.R;

import java.util.HashMap;
import java.util.Map;

public class BuildingMode {


    public static Map<Integer, MultiParam> MULTI_PARAM_MAP = new HashMap<>();

    static {
        MULTI_PARAM_MAP.put(R.drawable.b_room1, new MultiParam(1, 2, -0.5596f, -0.4774f, 0.41f));
        MULTI_PARAM_MAP.put(R.drawable.b_car, new MultiParam(6, 2, -0.2344f, 0f, 0.7619f));
    }

    public static class Mode {
        public int xI; //坐标x
        public int yI; //坐标y
        public int resId; //资源id
        public MultiParam multiParam;

        public Mode(int xI, int yI, int resId) {
            this.xI = xI;
            this.yI = yI;
            this.resId = resId;
        }

        public Mode(int xI, int yI, int resId, MultiParam multiParam) {
            this.xI = xI;
            this.yI = yI;
            this.resId = resId;
            this.multiParam = multiParam;
        }
    }


    public static class MultiParam {
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


        public MultiParam(int n, int m, float offsetX, float offsetY, float widthP) {
            this.n = n;
            this.m = m;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.widthP = widthP;
        }
    }

}
