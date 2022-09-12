package com.quawlebs.drupp.helpers;

public interface IResponseObserver {
    void onFailure(String message);

    void onSuccess(String message);
}
