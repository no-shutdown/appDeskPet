package com.xl.pet.ui.forest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xl.pet.ui.forest.mode.DateRange;

public class ForestViewModel extends ViewModel {

    private MutableLiveData<DateRange> selectDateRange;

    public ForestViewModel() {
        selectDateRange = new MutableLiveData<>();
    }

    public LiveData<DateRange> getSelectDateRange() {
        return selectDateRange;
    }
}