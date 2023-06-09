package com.xl.pet.ui.forest;


import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ForestFragment extends Fragment {

    private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd");

    private ForestDao forestDao = DatabaseHelper.forestDao(); //TODO ??? NPE


    private List<BuildingMode.Mode> buildBuildingModes(List<ForestDO> rawData) {
        //TODO
        return new ArrayList<>();
    }

    private void barCharSetData(List<ForestDO> rawData, BarChart chart) {
        ArrayList<String> xLabels = new ArrayList<>();
        xLabels.add("标签1");
        xLabels.add("标签2");
        xLabels.add("标签3");
        xLabels.add("标签4");
        xLabels.add("标签5");
        xLabels.add("标签6");
        xLabels.add("标签7");
        xLabels.add("标签8");
        xLabels.add("标签9");
        xLabels.add("标签10");
        xLabels.add("标签11");
        xLabels.add("标签12");
        xLabels.add("标签13");
        xLabels.add("标签14");
        xLabels.add("标签15");
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabels));

        //柱状图数据
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

        BarDataSet dataSet = new BarDataSet(entries, "销售量");
        dataSet.setColor(0xFF7aa667); // 设置柱状图颜色

        BarData barData = new BarData(dataSet);
        chart.setData(barData);

        chart.invalidate(); // 刷新图表
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        SegmentView segmentView = root.findViewById(R.id.top_segment);
        TextView topTitle = root.findViewById(R.id.top_title);
        CardView cardView = root.findViewById(R.id.card_data_count);
        //设置卡片宽度为 90%
        ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
        layoutParams.width = (int) (fetchScreenWidth() * 0.9);
        cardView.setLayoutParams(layoutParams);
        AreaViewGroup areaView = root.findViewById(R.id.layout_area);
        BarChart chart = root.findViewById(R.id.chart);
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
            topTitle.setText(topTitleText(dateRange));
            viewModel.getForestData().setValue(forestDao.findByRange(dateRange.getStart(), dateRange.getEnd()));
        });
        viewModel.getForestData().observe(getViewLifecycleOwner(), (data) -> {
            areaView.setData(buildBuildingModes(data));
            barCharSetData(data, chart);
        });
        return root;
    }

    private String topTitleText(DateRange dateRange) {
        String start = dateFormat.format(dateRange.getStart());
        String end = dateFormat.format(dateRange.getEnd());
        if (start.equals(end)) {
            return start;
        }
        return start + "~" + end;
    }

    public int fetchScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

}