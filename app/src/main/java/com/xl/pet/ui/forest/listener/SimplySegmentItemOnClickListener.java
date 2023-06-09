package com.xl.pet.ui.forest.listener;

import androidx.lifecycle.MutableLiveData;

import com.xl.pet.ui.common.SegmentView;
import com.xl.pet.ui.forest.mode.DateRange;
import com.xl.pet.utils.Utils;

import java.util.Calendar;

public class SimplySegmentItemOnClickListener implements SegmentView.SegmentItemClickListener {

    private MutableLiveData<DateRange> selectDateRange;

    public SimplySegmentItemOnClickListener(MutableLiveData<DateRange> selectDateRange) {
        this.selectDateRange = selectDateRange;
    }

    @Override
    public void onItemClick(SegmentView.ItemView item, int checkedItem) {
        Calendar calendar;
        if (checkedItem == 0) {
            //日
            calendar = Utils.getToday();
        } else if (checkedItem == 1) {
            //周
            calendar = Utils.getFirstDayOfMonth();
        } else if (checkedItem == 2) {
            //月
            calendar = Utils.getFirstDayOfMonth();
        } else {
            //年
            calendar = Utils.getFirstDayOfYear();
        }
        selectDateRange.setValue(new DateRange(calendar.getTimeInMillis(), System.currentTimeMillis()));
    }
}
