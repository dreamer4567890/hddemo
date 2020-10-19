package com.example.demo.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 *****************************************************************************
 * Copyright (C) 2018-2023 HD Corporation. All rights reserved
 * File        : AppVisibleMonitor.java
 *
 * Description : 统计apk的生命周期
 *
 * Creation    : 2018-10-30
 * Author      : zhoulong1@evergrande.cn
 * Notes：
 * 1，提供注册回调监听接口
 * 2，提供当前是否前台状态查询接口
 *****************************************************************************
 */

public class AppVisibleMonitor implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "AppVisibleMonitor";

    //当前app进程是否处于前台
    private static boolean mIsForeground = false;

    private List<IAppVisibilityChangedListener> mListeners =  new CopyOnWriteArrayList<>();

    private volatile int mForeGroundActivityCount;

    static WeakReference<Activity> mTopVisibleActivity;
    static List<Activity> sActivityList = new LinkedList<>();

    private static volatile AppVisibleMonitor sInstance;

    public static AppVisibleMonitor getInstance() {

        if (sInstance == null) {
            synchronized (AppVisibleMonitor.class) {
                if (sInstance == null) {
                    sInstance = new AppVisibleMonitor();
                }
            }
        }

        return sInstance;
    }

    private AppVisibleMonitor() {
        Log.i(TAG, "AppVisibleMonitor enter");
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated enter");
        sActivityList.add(activity);
        setTopActivityWeakRef(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.i(TAG, "onActivityStarted enter");

        setTopActivityWeakRef(activity);

        mForeGroundActivityCount++;

        if (!mIsForeground) {
            mIsForeground = true;
            Log.i(TAG, String.format("onActivityStarted(%s) Application goto foreground", activity));
            // 应用进入前台时，初始化网络层
            notifyAppForegroundChanged(true);
        }

        Log.i(TAG, String.format("onActivityStarted(%s) sForeGroundActivityCount=%d, mIsForeground=%s leave", activity.getClass().getSimpleName(), mForeGroundActivityCount, mIsForeground));
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.i(TAG, "onActivityResumed enter");
        setTopActivityWeakRef(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.i(TAG, "onActivityPaused enter");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.i(TAG, "onActivityStopped enter");

        mForeGroundActivityCount--;

        if (mForeGroundActivityCount <= 0) {
            Log.i(TAG, String.format("onActivityStopped(%s) Application goto background", activity));

            if (mIsForeground) {
                notifyAppForegroundChanged(false);
            }

            mIsForeground = false;
        }

        if (mTopVisibleActivity != null && activity == mTopVisibleActivity.get())
        {
            Log.i(TAG,"onActivityStopped mTopVisibleActivity = null");
            mTopVisibleActivity = null;
        }

        Log.i(TAG, String.format("onActivityStopped(%s) sForeGroundActivityCount=%d, mIsForeground=%s leave", activity.getClass().getSimpleName(), mForeGroundActivityCount, mIsForeground));
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.i(TAG, "onActivityDestroyed enter");
        sActivityList.remove(activity);
    }

    private static void setTopActivityWeakRef(Activity activity) {
        if (mTopVisibleActivity == null || !activity.equals(mTopVisibleActivity.get())) {
            mTopVisibleActivity = new WeakReference<>(activity);
        }
    }

    public synchronized void registerStateListener(IAppVisibilityChangedListener listener) {
        if (listener == null) {
            return;
        }

        if (mListeners.contains(listener)) {
            return;
        } else {
            mListeners.add(listener);
            listener.onAppForeGoundChanged(isForeground());
        }

    }

    public synchronized void removeStateListener(IAppVisibilityChangedListener listener) {
        if (listener == null) {
            return;
        }

        if (!mListeners.contains(listener)) {
            return;
        } else {
            mListeners.remove(listener);
        }

    }

    private void notifyAppForegroundChanged(boolean foreground) {
        List<IAppVisibilityChangedListener> listeners = mListeners;

        Log.i(TAG, "notifyAppForegroundChanged enter");

        if (listeners == null || listeners.isEmpty()) {
            Log.i(TAG, "notifyAppForegroundChanged: No listeners");
            return;
        }

        for (IAppVisibilityChangedListener listener : listeners) {
            listener.onAppForeGoundChanged(foreground);
        }

        Log.i(TAG, "notifyAppForegroundChanged leave");
    }

    public interface IAppVisibilityChangedListener {
        /**
         * App前台可见属性发生变化时，则触发本回调
         * @param foregound false - App此时在后台
         *                    true - App此时在前台
         */
        void onAppForeGoundChanged(boolean foregound);
    }

    public boolean isForeground() {
        return mIsForeground;
    }

    public void clearListeners() {
        mListeners.clear();
    }
}
