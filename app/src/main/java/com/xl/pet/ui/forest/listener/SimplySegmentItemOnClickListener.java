package com.xl.pet.ui.forest.listener;

import androidx.lifecycle.MutableLiveData;

import com.xl.pet.ui.common.SegmentView;
import com.xl.pet.ui.forest.mode.DateRange;
import com.xl.pet.utils.Utils;

import java.util.Calendar;

public class SimplySegmentItemOnClickListener implements SegmentView.SegmentItemClickListener {

    private final MutableLiveData<Integer> segmentItem;

    public SimplySegmentItemOnClickListener(MutableLiveData<Integer> segmentItem) {
        this.segmentItem = segmentItem;
    }

    @Override
    public void onItemClick(SegmentView.ItemView item, int checkedItem) {

        segmentItem.setValue(checkedItem);
    }
}
