package com.xl.pet.ui.forest;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xl.pet.database.entity.ForestDO;
import com.xl.pet.ui.forest.mode.DateRange;

import java.util.List;

public class ForestViewModel extends ViewModel {

    private final MutableLiveData<DateRange> selectDateRange;
    private final MutableLiveData<List<ForestDO>> forestData;

    public ForestViewModel() {
        selectDateRange = new MutableLiveData<>();
        forestData = new MutableLiveData<>();
    }

    public MutableLiveData<DateRange> getSelectDateRange() {
        return selectDateRange;
    }

    public MutableLiveData<List<ForestDO>> getForestData() {
        return forestData;
    }
}