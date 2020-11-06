package com.example.demo.active.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.lang.ref.WeakReference;

public class ScreenManager {
    private static final String TAG = ScreenManager.class.getSimpleName();
    private Context mContent;
    private WeakReference<Activity> mActivityWeak;

    @SuppressLint("StaticFieldLeak")
    private static volatile ScreenManager sInstance;

    public static ScreenManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (ScreenManager.class) {
                if (sInstance == null) {
                    sInstance = new ScreenManager(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public ScreenManager(Context context) {
        mContent = context;
    }

    public void setActivity(Activity activity) {
        mActivityWeak = new WeakReference<>(activity);
    }

    public void startActivity() {
        Log.i(TAG, "LiveActivity startActivity!!!");
        LiveActivity.actionToLiveActivity(mContent);
    }

    public void finishActivity() {
        if (mActivityWeak != null) {
            Activity activity = mActivityWeak.get();
            if (activity != null) {
                Log.i(TAG, "LiveActivity finishActivity!!!");
                activity.finish();
            } else {
                Log.i(TAG, "mActivityWeak is release!!!");
            }
        }
    }

}
