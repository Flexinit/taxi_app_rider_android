package com.quawlebs.drupp.view.ui.chat;

import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.mvibase.MviInteractor;
import com.quawlebs.drupp.network.DruppRequestHandler;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

class ChatInteractor extends MviInteractor {

    public Observable<PartialChatViewState> sendNotification(JSONObject message) {
        DruppRequestHandler.clearInstance();
        return DruppRequestHandler.getInstance(SessionManager.getAccessToken())
                .sendFCMNotification(message)
                .compose(applySchedulers())
                .map((Function<JSONObject, PartialChatViewState>) jsonObject -> new PartialChatViewState.PostSendState("Sent"))
                .onErrorReturn(throwable -> new PartialChatViewState.ErrorState(parseError(throwable)));
    }
}
