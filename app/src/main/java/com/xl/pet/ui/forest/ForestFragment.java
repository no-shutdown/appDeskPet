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

import com.github.mikephil.charting.components.XAxis;
import com.xl.pet.R;
import com.xl.pet.database.dao.ForestDao;
import com.xl.pet.database.entity.ForestDO;
import com.xl.pet.ui.common.SegmentView;
import com.xl.pet.ui.forest.listener.SimplySegmentItemOnClickListener;
import com.xl.pet.ui.forest.mode.DateRange;
import com.xl.pet.utils.DatabaseHelper;
import com.xl.pet.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ForestFragment extends Fragment {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd");

    private final ForestDao forestDao = DatabaseHelper.forestDao();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        SegmentView segmentView = root.findViewById(R.id.top_segment);
        TextView topTitle = root.findViewById(R.id.top_title);
        CardView cardView = root.findViewById(R.id.card_data_count);
        AreaViewGroup areaView = root.findViewById(R.id.layout_area);
        ForestBarChar chart = root.findViewById(R.id.chart);

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
        segmentView.setOnSegmentItemClickListener(new SimplySegmentItemOnClickListener(viewModel.getSegmentItem()));
        viewModel.getSegmentItem().observe(getViewLifecycleOwner(), (item) -> viewModel.getDateRange().setValue(changeDateRange(item)));
        viewModel.getDateRange().observe(getViewLifecycleOwner(), (dateRange) -> new Thread(() -> fetchData(dateRange, topTitle, viewModel)).start());
        viewModel.getForestData().observe(getViewLifecycleOwner(), (data) -> {
            Integer checkItem = viewModel.getSegmentItem().getValue();
            DateRange dateRange = viewModel.getDateRange().getValue();
            areaView.setData(checkItem, data);
            chart.setData(checkItem, dateRange, data);
        });

        viewModel.getSegmentItem().setValue(0);
        return root;
    }

    private DateRange changeDateRange(int checkItem) {
        Calendar calendar;
        if (checkItem == 0) {
            //日
            calendar = Utils.getToday();
        } else if (checkItem == 1) {
            //周
            calendar = Utils.getFirstDayOfWeek();
        } else if (checkItem == 2) {
            //月
            calendar = Utils.getFirstDayOfMonth();
        } else {
            //年
            calendar = Utils.getFirstDayOfYear();
        }
        return new DateRange(calendar.getTimeInMillis(), System.currentTimeMillis());
    }

    private void fetchData(DateRange dateRange, TextView topTitle, ForestViewModel viewModel) {
        topTitle.setText(topTitleText(dateRange));
        List<ForestDO> byRange = forestDao.findByRange(dateRange.getStart(), dateRange.getEnd());
        getActivity().runOnUiThread(() -> {
            viewModel.getForestData().setValue(byRange);
            //TODO
//            ForestDO mock1 = new ForestDO();
//            mock1.id = 1;
//            mock1.startTime = Utils.getToday().getTimeInMillis();
//            mock1.endTime = mock1.startTime + 3 * 60 * 60 * 1000;
//            mock1.resId = R.drawable.b_forest1;
//            ForestDO mock2 = new ForestDO();
//            mock2.id = 2;
//            mock2.startTime = Utils.getFirstDayOfWeek().getTimeInMillis();
//            mock2.endTime = mock2.startTime + 3 * 60 * 60 * 1000;
//            mock2.resId = R.drawable.b_forest1;
//            ForestDO mock3 = new ForestDO();
//            mock3.id = 3;
//            mock3.startTime = Utils.getFirstDayOfMonth().getTimeInMillis();
//            mock3.endTime = mock3.startTime + 3 * 60 * 60 * 1000;
//            mock3.resId = R.drawable.b_forest1;
//            ForestDO mock4 = new ForestDO();
//            mock4.id = 4;
//            mock4.startTime = Utils.getFirstDayOfYear().getTimeInMillis();
//            mock4.endTime = mock4.startTime + 3 * 60 * 60 * 1000;
//            mock4.resId = R.drawable.b_forest1;
//            List<ForestDO> mockData = List.of(mock1, mock2, mock3, mock4);
//            viewModel.getForestData().setValue(mockData);
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

    public int fetchScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

}