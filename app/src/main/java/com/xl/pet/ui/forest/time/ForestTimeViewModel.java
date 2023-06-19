package com.xl.pet.ui.forest.time;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ForestTimeViewModel extends ViewModel {

    private final MutableLiveData<String> text;

    public ForestTimeViewModel() {
        text = new MutableLiveData<>();
    }

    public MutableLiveData<String> getText() {
        return text;
    }
}