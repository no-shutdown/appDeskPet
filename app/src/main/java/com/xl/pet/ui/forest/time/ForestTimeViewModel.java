package com.xl.pet.ui.forest.time;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ForestTimeViewModel extends ViewModel {

    private final MutableLiveData<Integer> seconds;

    public ForestTimeViewModel() {
        seconds = new MutableLiveData<>();
        seconds.setValue(0);
    }

    public MutableLiveData<Integer> getSeconds() {
        return seconds;
    }
}