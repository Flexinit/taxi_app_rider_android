package com.quawlebs.drupp.mvibase;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @param(T) Acutal View(Activity ,Fragment ) for UI action and response
 * @S Actual View State which contains business logic
 * @R Partial State for business logic
 */
public abstract class MviBaseViewModel<T, S, R> {

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected BehaviorSubject<R> stateSubject = BehaviorSubject.create();

    public abstract void bind(T view);

    public void unbind() {
        compositeDisposable.dispose();
    }

    public abstract S reduce(S previousState, R partialViewState);
}

