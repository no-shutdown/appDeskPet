package com.xl.pet.ui.forest;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xl.pet.database.entity.ForestDO;
import com.xl.pet.ui.forest.mode.DateRange;

import java.util.List;

public class ForestViewModel extends ViewModel {

    private final MutableLiveData<Integer> segmentItem;
    private final MutableLiveData<DateRange> dateRange;
    private final MutableLiveData<List<ForestDO>> forestData;

    public ForestViewModel() {
        segmentItem = new MutableLiveData<>();
        dateRange = new MutableLiveData<>();
        forestData = new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getSegmentItem() {
        return segmentItem;
    }

    public MutableLiveData<DateRange> getDateRange() {
        return dateRange;
    }

    public MutableLiveData<List<ForestDO>> getForestData() {
        return forestData;
    }
}