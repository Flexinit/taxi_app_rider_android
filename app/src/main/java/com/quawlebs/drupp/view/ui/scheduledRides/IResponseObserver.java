package com.quawlebs.drupp.view.ui.scheduledRides;

public interface IResponseObserver {
    void onFailure(String message);

    void onSuccess(String message);
}
