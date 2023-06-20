package com.xl.pet.ui.forest.time;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xl.pet.database.entity.ForestFlagDO;

public class ForestTimeViewModel extends ViewModel {

    private final MutableLiveData<Integer> resId;
    private final MutableLiveData<ForestFlagDO> flag;

    public ForestTimeViewModel() {
        resId = new MutableLiveData<>();
        flag = new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getResId() {
        return resId;
    }

    public MutableLiveData<ForestFlagDO> getFlag() {
        return flag;
    }

    public void setResId(Integer resId) {
        this.resId.setValue(resId);
    }

    public void setFlag(ForestFlagDO flag) {
        this.flag.setValue(flag);
    }

}