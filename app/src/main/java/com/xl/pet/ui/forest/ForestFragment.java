package com.xl.pet.ui.forest;


import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.xl.pet.R;
import com.xl.pet.database.dao.ForestDao;
import com.xl.pet.database.entity.ForestDO;
import com.xl.pet.ui.common.SegmentView;
import com.xl.pet.ui.forest.listener.SimplySegmentItemOnClickListener;
import com.xl.pet.ui.forest.mode.BuildingMode;
import com.xl.pet.ui.forest.mode.DateRange;
import com.xl.pet.utils.DatabaseHelper;
import com.xl.pet.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ForestFragment extends Fragment {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd");

    private static final Random RANDOM = new Random();

    private final ForestDao forestDao = DatabaseHelper.forestDao();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        SegmentView segmentView = root.findViewById(R.id.top_segment);
        TextView topTitle = root.findViewById(R.id.top_title);
        CardView cardView = root.findViewById(R.id.card_data_count);
        AreaViewGroup areaView = root.findViewById(R.id.layout_area);
        BarChart chart = root.findViewById(R.id.chart);

        //设置卡片宽度为 90%
        ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
        layoutParams.width = (int) (fetchScreenWidth() * 0.9);
        cardView.setLayoutParams(layoutParams);
        chart.getDescription().setEnabled(false); // 隐藏描述文本
        chart.getLegend().setEnabled(false); // 隐藏图例
        chart.getXAxis().setDrawGridLines(false); //隐藏X轴网格线
        chart.getAxisLeft().setDrawGridLines(false); // 隐藏Y轴网格线
        chart.getAxisRight().setEnabled(false); // 隐藏右侧Y轴
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置 X 轴位置为底部
        xAxis.setGranularity(1f); // 设置 X 轴标签之间的最小间隔

        ForestViewModel viewModel = ViewModelProviders.of(this).get(ForestViewModel.class);
        segmentView.setOnSegmentItemClickListener(new SimplySegmentItemOnClickListener(viewModel.getSelectDateRange()));
        viewModel.getSelectDateRange().observe(getViewLifecycleOwner(), (dateRange) -> {
            new Thread(() -> selectData(dateRange, topTitle, viewModel)).start();
        });
        viewModel.getForestData().observe(getViewLifecycleOwner(), (data) -> {
            areaView.setData(buildAreaViewData(data, areaView.getN()));
            buildCharData(data, chart, segmentView.getCheckedItem());
        });

        viewModel.getSelectDateRange().setValue(new DateRange(Utils.getToday().getTimeInMillis(), System.currentTimeMillis()));
        return root;
    }

    private void selectData(DateRange dateRange, TextView topTitle, ForestViewModel viewModel) {
        topTitle.setText(topTitleText(dateRange));
        List<ForestDO> byRange = forestDao.findByRange(dateRange.getStart(), dateRange.getEnd());
        getActivity().runOnUiThread(() -> {
//            viewModel.getForestData().setValue(byRange);
            //TODO
            ForestDO mock1 = new ForestDO();
            mock1.id = 1;
            mock1.startTime = Utils.getToday().getTimeInMillis();
            mock1.endTime = mock1.startTime + 3 * 60 * 60 * 1000;
            mock1.resId = R.drawable.b_forest1;
            ForestDO mock2 = new ForestDO();
            mock2.id = 2;
            mock2.startTime = Utils.getFirstDayOfWeek().getTimeInMillis();
            mock2.endTime = mock2.startTime + 3 * 60 * 60 * 1000;
            mock2.resId = R.drawable.b_forest1;
            ForestDO mock3 = new ForestDO();
            mock3.id = 3;
            mock3.startTime = Utils.getFirstDayOfMonth().getTimeInMillis();
            mock3.endTime = mock3.startTime + 3 * 60 * 60 * 1000;
            mock3.resId = R.drawable.b_forest1;
            ForestDO mock4 = new ForestDO();
            mock4.id = 4;
            mock4.startTime = Utils.getFirstDayOfYear().getTimeInMillis();
            mock4.endTime = mock4.startTime + 3 * 60 * 60 * 1000;
            mock4.resId = R.drawable.b_forest1;
            List<ForestDO> mockData = List.of(mock1, mock2, mock3, mock4);
            viewModel.getForestData().setValue(mockData);
        });
    }

    private String topTitleText(DateRange dateRange) {
        String start = DATE_FORMAT.format(dateRange.getStart());
        String end = DATE_FORMAT.format(dateRange.getEnd());
        if (start.equals(end)) {
            return start;
        }
        return start + "~" + end;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<BuildingMode.Mode> buildAreaViewData(List<ForestDO> rawData, int n) {
        List<BuildingMode.Mode> modes = new ArrayList<>();
        Set<AreaViewGroup.FieldPoint> set = new HashSet<>();
        for (int index = 0; index < rawData.size(); index++) {
            int i = RANDOM.nextInt(n);
            int j = RANDOM.nextInt(n);
            AreaViewGroup.FieldPoint fieldPoint = new AreaViewGroup.FieldPoint(i, j);
            if (set.contains(fieldPoint)) {
                index--;
                continue;
            }
            set.add(fieldPoint);
            ForestDO rawDatum = rawData.get(index);
            modes.add(new BuildingMode.Mode(i, j, rawDatum.resId));
        }
        Stream<BuildingMode.Mode> sorted = modes.stream().sorted();
        return sorted.collect(Collectors.toList());
    }

    private void buildCharData(List<ForestDO> rawData, BarChart chart, int segmentItem) {

        //柱状图数据
        List<String> xLabels = XLabel.getXLabel(segmentItem);
        for (int i = 0; i < xLabels.size(); i++) {
            //TODO
        }
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 20f));
        entries.add(new BarEntry(1f, 30f));
        entries.add(new BarEntry(2f, 25f));
        entries.add(new BarEntry(3f, 40f));
        entries.add(new BarEntry(4f, 35f));
        entries.add(new BarEntry(5f, 20f));
        entries.add(new BarEntry(6f, 30f));
        entries.add(new BarEntry(7f, 25f));
        entries.add(new BarEntry(8f, 40f));
        entries.add(new BarEntry(9f, 35f));
        entries.add(new BarEntry(10f, 20f));
        entries.add(new BarEntry(11f, 30f));
        entries.add(new BarEntry(12f, 25f));
        entries.add(new BarEntry(13f, 40f));
        entries.add(new BarEntry(14f, 35f));

        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabels));
        BarDataSet dataSet = new BarDataSet(entries, "统计");
        dataSet.setColor(0xFF7aa667); // 设置柱状图颜色
        BarData barData = new BarData(dataSet);
        chart.setData(barData);
        chart.invalidate(); // 刷新图表
    }

    public int fetchScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static class XLabel {
        public static final List<String> DAY = new ArrayList<>();
        public static final List<String> WEEK = new ArrayList<>();
        public static final List<String> MONTH = new ArrayList<>();
        public static final List<String> YEAR = new ArrayList<>();

        static {
            DAY.add("00:00");DAY.add("01:00");DAY.add("02:00");DAY.add("03:00");
            DAY.add("04:00");DAY.add("05:00");DAY.add("06:00");DAY.add("07:00");
            DAY.add("08:00");DAY.add("09:00");DAY.add("10:00");DAY.add("11:00");
            DAY.add("12:00");DAY.add("13:00");DAY.add("14:00");DAY.add("15:00");
            DAY.add("16:00");DAY.add("17:00");DAY.add("18:00");DAY.add("19:00");
            DAY.add("20:00");DAY.add("21:00");DAY.add("22:00");DAY.add("23:00");

            WEEK.add("周一");WEEK.add("周二");WEEK.add("周三");WEEK.add("周四");
            WEEK.add("周五");WEEK.add("周六");WEEK.add("周日");

            MONTH.add("1");MONTH.add("2");MONTH.add("3");
            MONTH.add("4");MONTH.add("5");MONTH.add("6");MONTH.add("7");
            MONTH.add("8");MONTH.add("9");MONTH.add("10");MONTH.add("11");
            MONTH.add("12");MONTH.add("13");MONTH.add("14");MONTH.add("15");
            MONTH.add("16");MONTH.add("17");MONTH.add("18");MONTH.add("19");
            MONTH.add("20");MONTH.add("21");MONTH.add("22");MONTH.add("23");
            MONTH.add("24");MONTH.add("25");MONTH.add("26");MONTH.add("27");
            MONTH.add("28");MONTH.add("29");MONTH.add("30");MONTH.add("31");

            YEAR.add("1");YEAR.add("2");YEAR.add("3");YEAR.add("4");
            YEAR.add("5");YEAR.add("6");YEAR.add("7");YEAR.add("8");
            YEAR.add("9");YEAR.add("10");YEAR.add("11");YEAR.add("12");
        }

        public static List<String> getXLabel(int segmentItem) {
            if (0 == segmentItem) {
                return DAY;
            }
            if (1 == segmentItem) {
                return WEEK;
            }
            if (2 == segmentItem) {
                return MONTH.subList(0, Utils.getDayOfMonth());
            }
            return YEAR;
        }
    }

}