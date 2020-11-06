package com.example.demo.active.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import java.util.Set;

public class ScreenBroadcastListener {

    private Context mContent;
    private ScreenStateListener mListener;
    private ScreenBroadcastReceiver mScreenReceiver;

    public ScreenBroadcastListener(Context mContent) {
        this.mContent = mContent;
        mScreenReceiver = new ScreenBroadcastReceiver();
    }

    public void setScreenStateListener(ScreenStateListener listener) {
        mListener = listener;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mContent.registerReceiver(mScreenReceiver, filter);
    }

    public interface ScreenStateListener {

        void onScreenOn();

        void onScreenOff();
    }

    private class ScreenBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mListener == null) {
                return;
            }
            String action = intent.getAction();
            if (TextUtils.equals(action, Intent.ACTION_SCREEN_ON)) {
                mListener.onScreenOn();
            } else if (TextUtils.equals(action, Intent.ACTION_SCREEN_OFF)) {
                mListener.onScreenOff();
            }
        }
    }
}
