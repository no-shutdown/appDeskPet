package com.xl.pet.ui.forest;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.xl.pet.database.entity.ForestDO;
import com.xl.pet.ui.forest.mode.BarCharMode;
import com.xl.pet.ui.forest.mode.DateRange;
import com.xl.pet.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ForestBarChar extends BarChart {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");
    private static final ValueFormatter VALUE_FORMATTER = new BarValueFormatter();
    private static final long ONE_MINUTE = 60 * 1000;
    private static final long ONE_HOUR = 60 * 60 * 1000;
    private static final long ONE_DAY = 24 * 60 * 60 * 1000;

    public ForestBarChar(Context context) {
        super(context);
    }

    public ForestBarChar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setData(int segmentItem, DateRange dateRange, List<ForestDO> rawData) {
        if (rawData.isEmpty()) {
            return;
        }

        BarCharMode.Mode data = buildBarCharMode(segmentItem, dateRange, rawData);
        BarDataSet dataSet = new BarDataSet(data.data, "统计");
        dataSet.setColor(0xFF7aa667); // 设置柱状图颜色
        BarData barData = new BarData(dataSet);
        barData.setValueFormatter(VALUE_FORMATTER);
        this.getXAxis().setValueFormatter(new IndexAxisValueFormatter(data.xLabel));
        this.setData(barData);
        this.invalidate(); // 刷新图表
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private BarCharMode.Mode buildBarCharMode(int segmentItem, DateRange dateRange, List<ForestDO> rawData) {
        if (0 == segmentItem)
            return buildMode(dateRange, rawData, BarCharMode.XLabel.DAY, (i) -> ONE_HOUR, ONE_MINUTE);
        if (1 == segmentItem)
            return buildMode(dateRange, rawData, BarCharMode.XLabel.WEEK, (i) -> ONE_DAY, ONE_MINUTE);
        if (2 == segmentItem) {
            int monthDayOfTime = Utils.getMonthDayOfTime(dateRange.getStart());
            return buildMode(dateRange, rawData, BarCharMode.XLabel.MONTH.subList(0, monthDayOfTime), (i) -> ONE_DAY, ONE_MINUTE);
        }
        if (3 == segmentItem)
            return buildMode(dateRange, rawData, BarCharMode.XLabel.YEAR, (i) -> Utils.getDayOfMonth(i) * ONE_DAY, ONE_MINUTE);
        throw new RuntimeException();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private BarCharMode.Mode buildMode(DateRange dateRange, List<ForestDO> rawData, List<String> xLabels, Function<Integer, Long> intervalFunction, long cell) {
        List<BarEntry> entries = new ArrayList<>();
        DateRange intervalItem = new DateRange();
        DateRange dataItem = new DateRange();
        for (int i = 0; i < xLabels.size(); i++) {
            long interval = intervalFunction.apply(i);
            intervalItem.reset(dateRange.getStart() + i * interval, dateRange.getStart() + (i + 1) * interval);
            long sumTime = 0;
            //计算每一条专注记录与间隔的交集时长
            for (ForestDO data : rawData)
                sumTime += countIntersection(dataItem.reset(data.startTime, data.endTime), intervalItem);
            entries.add(new BarEntry(i, Float.parseFloat(DECIMAL_FORMAT.format(sumTime * 1.0f / cell))));
        }
        return new BarCharMode.Mode(xLabels, entries);
    }

    private long countIntersection(DateRange dateRange1, DateRange dateRange2) {
        long start = Math.max(dateRange1.getStart(), dateRange2.getStart());
        long end = Math.min(dateRange1.getEnd(), dateRange2.getEnd());
        return Math.max(0, end - start);
    }

    protected static class BarValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return DECIMAL_FORMAT.format(value);
        }
    }
}
