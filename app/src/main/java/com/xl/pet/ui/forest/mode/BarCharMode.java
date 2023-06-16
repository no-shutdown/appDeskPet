package com.xl.pet.ui.forest.mode;


import com.github.mikephil.charting.data.BarEntry;
import com.xl.pet.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class BarCharMode {

    public static class Mode {
        public List<String> xLabel;
        public List<BarEntry> data;

        public Mode(List<String> xLabel, List<BarEntry> data) {
            this.xLabel = xLabel;
            this.data = data;
        }
    }


    public static class XLabel {
        public static final List<String> DAY = new ArrayList<>();
        public static final List<String> WEEK = new ArrayList<>();
        public static final List<String> MONTH = new ArrayList<>();
        public static final List<String> YEAR = new ArrayList<>();

        static {
            DAY.add("00:00");
            DAY.add("01:00");
            DAY.add("02:00");
            DAY.add("03:00");
            DAY.add("04:00");
            DAY.add("05:00");
            DAY.add("06:00");
            DAY.add("07:00");
            DAY.add("08:00");
            DAY.add("09:00");
            DAY.add("10:00");
            DAY.add("11:00");
            DAY.add("12:00");
            DAY.add("13:00");
            DAY.add("14:00");
            DAY.add("15:00");
            DAY.add("16:00");
            DAY.add("17:00");
            DAY.add("18:00");
            DAY.add("19:00");
            DAY.add("20:00");
            DAY.add("21:00");
            DAY.add("22:00");
            DAY.add("23:00");

            WEEK.add("周一");
            WEEK.add("周二");
            WEEK.add("周三");
            WEEK.add("周四");
            WEEK.add("周五");
            WEEK.add("周六");
            WEEK.add("周日");

            MONTH.add("1");
            MONTH.add("2");
            MONTH.add("3");
            MONTH.add("4");
            MONTH.add("5");
            MONTH.add("6");
            MONTH.add("7");
            MONTH.add("8");
            MONTH.add("9");
            MONTH.add("10");
            MONTH.add("11");
            MONTH.add("12");
            MONTH.add("13");
            MONTH.add("14");
            MONTH.add("15");
            MONTH.add("16");
            MONTH.add("17");
            MONTH.add("18");
            MONTH.add("19");
            MONTH.add("20");
            MONTH.add("21");
            MONTH.add("22");
            MONTH.add("23");
            MONTH.add("24");
            MONTH.add("25");
            MONTH.add("26");
            MONTH.add("27");
            MONTH.add("28");
            MONTH.add("29");
            MONTH.add("30");
            MONTH.add("31");

            YEAR.add("1");
            YEAR.add("2");
            YEAR.add("3");
            YEAR.add("4");
            YEAR.add("5");
            YEAR.add("6");
            YEAR.add("7");
            YEAR.add("8");
            YEAR.add("9");
            YEAR.add("10");
            YEAR.add("11");
            YEAR.add("12");
        }
    }
}
