package com.example.demo.model;

import com.example.demo.http.ExceptionHandle;

import io.reactivex.disposables.Disposable;

public abstract class Observer<T> implements io.reactivex.Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {
        OnDisposable(d);
    }

    @Override
    public void onNext(T t) {
        OnSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        OnFail(ExceptionHandle.handleException(e));
    }

    @Override
    public void onComplete() {
        OnCompleted();
    }


    protected abstract void OnDisposable(Disposable d);

    protected abstract void OnSuccess(T t);

    protected abstract void OnFail(ExceptionHandle.ResponseException e);

    protected abstract void OnCompleted();
}

