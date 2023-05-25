package com.xl.pet.ui.menstruation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xl.pet.database.entity.MenstruationDO;

import java.util.List;

public class MenstruationViewModel extends ViewModel {

    private final MutableLiveData<String> mMonth;
    private final MutableLiveData<List<MenstruationDO>> data;


    public MenstruationViewModel() {
        mMonth = new MutableLiveData<>();
        data = new MutableLiveData<>();
    }

    public MutableLiveData<String> getmMonth() {
        return mMonth;
    }

    public MutableLiveData<List<MenstruationDO>> getData() {
        return data;
    }

}
