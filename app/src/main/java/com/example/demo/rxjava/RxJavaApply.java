package com.example.demo.rxjava;

import android.annotation.SuppressLint;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxJavaApply {

    public static Subscription subscription;
    // 倒计时观察者
    public static Observer<Long> countdownSubscriber;
    //下游控制
    public static Disposable disposable;

    /**
     * 先加载缓存，后覆盖后台数据
     */
    public static void publisher() {
        Publisher<ArrayList<RxJavaBean>> publisherCache = Flowable.create(new FlowableOnSubscribe<ArrayList<RxJavaBean>>() {
            @Override
            public void subscribe(final FlowableEmitter<ArrayList<RxJavaBean>> emitter) {
                //缓存
                ArrayList<RxJavaBean> cacheRoomList = new ArrayList<>();
                if (cacheRoomList.size() > 0) {
                    emitter.onNext(cacheRoomList);
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io());

        Publisher<ArrayList<RxJavaBean>> publisherHttp = Flowable.create(new FlowableOnSubscribe<ArrayList<RxJavaBean>>() {
            @Override
            public void subscribe(final FlowableEmitter<ArrayList<RxJavaBean>> emitter) {
                //后台返回数据
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io());

        List<Publisher<ArrayList<RxJavaBean>>> publisherList = new ArrayList<>();
        publisherList.add(publisherCache);
        publisherList.add(publisherHttp);
        Flowable.concatEager(publisherList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<RxJavaBean>>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(ArrayList<RxJavaBean> roomInfoList) {
                        //处理数据，回调
                    }

                    @Override
                    public void onError(Throwable t) {
                        //错误信息
                    }

                    @Override
                    public void onComplete() {
                        //完成信息
                    }
                });
    }

    /**
     * 加锁
     *
     * @param time 锁住的时间
     */
    @SuppressLint("CheckResult")
    public static void lock(int time) {
        cancelLock();
        Flowable.just("")
                .delay(time, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subs) {
                        subscription = subs;
                        //加锁期间
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String aLong) {
                cancelLock();
                //解锁之后
            }
        });
    }

    public static void cancelLock() {
        if (subscription != null) {
            subscription = null;
        }
    }

    public static void getData() {
        RxTaskScheduler.postLogicMainTask(new RxTaskCallBack<List<RxJavaBean>>() {
            @Override
            public List<RxJavaBean> doWork() {
                //数据处理
                return new ArrayList<>();
            }

            @Override
            public void onSuccess(List<RxJavaBean> homeItemBeans) {
                super.onSuccess(homeItemBeans);
                //回调成功
            }
        });
    }

    /**
     * 倒计时
     * @param time
     */
    public static void startCodeCountdown(int time){
        // 重新new观察者
        countdownSubscriber = null;
        countdownSubscriber = new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Long aLong) {
                if (!disposable.isDisposed()) {
                    //倒计时请求间隔倒计时
                }
            }

            @Override
            public void onError(Throwable e) {
                if (!disposable.isDisposed()) {
                    //错误信息
                }
            }

            @Override
            public void onComplete() {
                if (disposable.isDisposed()) {
                    //倒计时结束
                }
            }
        };
        // 开始倒计时
        RxJavaUtils.observableCountdown(time).subscribe(countdownSubscriber);
        //请求失败调用cancelCountDown();
    }

    private void cancelCountDown() {
        if (countdownSubscriber != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
