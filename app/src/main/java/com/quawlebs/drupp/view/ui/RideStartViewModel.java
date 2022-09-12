package com.quawlebs.drupp.view.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RideStartViewModel extends ViewModel {

    MutableLiveData<Boolean> isShowNewsDialog = new MutableLiveData<>();
    MutableLiveData<Boolean> isShowShopDialog = new MutableLiveData<>();

    public void setIsShowNewsDialog() {
        isShowNewsDialog.postValue(true);
    }

    public void setIsShowShopDialog() {
        isShowShopDialog.postValue(true);
    }
}
