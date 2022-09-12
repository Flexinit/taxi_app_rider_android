package com.quawlebs.drupp.view.ui.chat;

import com.quawlebs.drupp.mvibase.MviBaseView;

import org.json.JSONObject;

import io.reactivex.Observable;

interface ChatView<T> extends MviBaseView<T> {

    Observable<JSONObject> sendIntent();
}
