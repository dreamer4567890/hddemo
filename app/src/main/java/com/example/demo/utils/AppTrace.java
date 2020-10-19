package com.example.demo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

/**
 * Created by hefeng on 2018/5/11.
 */
public class AppTrace {
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
    @SuppressLint("StaticFieldLeak")
    private volatile static Activity sActivity;

    public static void setContext(Context context){
        sContext = context;
    }

    public static Context getContext(){
        return sContext;
    }

    public static void setActivity(Activity activity) {
        sActivity = activity;
    }

    public static Activity getActivity() {
        return sActivity;
    }


}
