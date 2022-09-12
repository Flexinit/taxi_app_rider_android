package com.quawlebs.drupp.view.ui.chat;

import com.quawlebs.drupp.mvibase.MviBaseViewModel;

import org.json.JSONObject;

import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

class ChatViewModel extends MviBaseViewModel<ChatView, ChatViewState, PartialChatViewState> {

    private ChatInteractor chatInteractor;

    public ChatViewModel(ChatInteractor chatInteractor) {
        this.chatInteractor = chatInteractor;
    }

    @Override
    public void bind(ChatView view) {

        Observable<PartialChatViewState> sendStateObservable = view.sendIntent()
                .flatMap((Function<JSONObject, Observable<PartialChatViewState>>) message -> chatInteractor.sendNotification(message));
        ChatViewState initialState = new ChatViewState("", false);

        BehaviorSubject<PartialChatViewState> mergeIntentObservable = Observable.merge(Arrays.asList(sendStateObservable)).subscribeWith(stateSubject);
        compositeDisposable.add(mergeIntentObservable.scan(initialState, this::reduce).subscribe(view::render));
    }

    @Override
    public ChatViewState reduce(ChatViewState previousState, PartialChatViewState partialViewState) {
        if (partialViewState instanceof PartialChatViewState.LoadingState) {
            previousState.progress = true;
            previousState.error = "";
            previousState.message = "";
            previousState.sent = false;
        } else if (partialViewState instanceof PartialChatViewState.ErrorState) {
            previousState.progress = false;
            previousState.error = ((PartialChatViewState.ErrorState) partialViewState).getError();
            previousState.message = "";
            previousState.sent = false;
        } else if (partialViewState instanceof PartialChatViewState.InitialState) {
            previousState.progress = false;
            previousState.error = "";
            previousState.message = "";
            previousState.sent = false;
        } else if (partialViewState instanceof PartialChatViewState.PostSendState) {
            previousState.message = ((PartialChatViewState.PostSendState) partialViewState).getMessage();
            previousState.error = "";
            previousState.progress = false;
            previousState.sent = true;

        }
        return previousState;

    }

}