package com.quawlebs.drupp.mvibase;


/**
 * @param <T> This is ViewState in mvi pattern which contains business model
 */
public interface MviBaseView<T> {

    void bindViewModel();

    void render(T viewState);

}
