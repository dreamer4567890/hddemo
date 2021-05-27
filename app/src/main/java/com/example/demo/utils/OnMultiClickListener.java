package com.example.demo.utils;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;


public abstract class OnMultiClickListener implements View.OnClickListener {

    //两次点击事件不能少于1000ms
    private static final int MIN_CLICK_DELAY_TIME = 1000;

    private static long lastClickTime = 0;

    private int duration = MIN_CLICK_DELAY_TIME;
    private boolean isVibration;

    /**
     * 响应点击事件
     *
     * @param view
     */
    public abstract void onMultiClick(View view);

    public OnMultiClickListener() {
//        lastClickTime = 0;
    }

    public OnMultiClickListener(int duration) {
//        lastClickTime = 0;
        this.duration = duration;
    }

    public OnMultiClickListener(int duration, boolean isVibration) {
//        lastClickTime = 0;
        this.duration = duration;
        this.isVibration = isVibration;
    }

    /**
     * 无效的点击
     *
     * @param view
     */
    public void onInvalidClick(View view) {
        Log.i("OnMultiClickListener", "onInvalidClick ");
    }

    @Override
    public void onClick(View v) {
        if (isVibration) {
            Vibrator vibrator = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(new long[]{10}, -1);
        }

        long curClickTime = System.currentTimeMillis();
        if (curClickTime - lastClickTime < 0) {
            lastClickTime = curClickTime;
        }

        if ((curClickTime - lastClickTime) >= duration) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            lastClickTime = curClickTime;
            onMultiClick(v);
        } else {
            onInvalidClick(v);
        }
    }
}
