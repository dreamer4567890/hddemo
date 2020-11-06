package com.example.demo.active.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.demo.active.activity.ScreenBroadcastListener;
import com.example.demo.active.activity.ScreenManager;


public class LiveService extends Service {

    public static void toLiveService(Context context) {
        Intent intent = new Intent(context, LiveService.class);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final ScreenManager screenManager = ScreenManager.getInstance(this);
        ScreenBroadcastListener listener = new ScreenBroadcastListener(this);
        listener.setScreenStateListener(new ScreenBroadcastListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                screenManager.finishActivity();
            }

            @Override
            public void onScreenOff() {
                screenManager.startActivity();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }
}
