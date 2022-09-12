package com.quawlebs.drupp.helpers;

public interface IActivityInteractor<T> {
    void doNetworkRequest();

    T getSubject();
}
