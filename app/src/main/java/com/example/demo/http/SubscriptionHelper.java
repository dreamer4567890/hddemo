package com.example.demo.http;

import io.reactivex.disposables.Disposable;

public interface SubscriptionHelper<T> {
    /**
     * 添加
     * @param d
     */
    void add(Disposable d);

    /**
     * 取消
     * @param d
     */
    void cancel(Disposable d);

    /**
     * 取消所有
     */
    void cancelAll();
}

