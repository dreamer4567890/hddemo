package com.example.demo.rxjava;

import io.reactivex.disposables.Disposable;

public interface BaseRxTask<T> {
    void onStart(Disposable disposable);

    void onSuccess(T t);

    void onFailed(Throwable e);

    void onComplete();

    T doWork() throws Exception;
}
