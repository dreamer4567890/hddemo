package com.example.demo.widget.colorpicker;

import android.view.MotionEvent;

class ThrottledTouchEventHandler {
    static final int EVENT_MIN_INTERVAL = 1000 / 60; // 16ms
    static final int SELECTOR_RADIUS_DP = 20;
    static final float COLOR_PICK_RADIUS_F = 0.4f;         //取色盘占宽高百分比
    private int minInterval = EVENT_MIN_INTERVAL;
    private Updatable updatable;
    private long lastPassedEventTime = 0;

    ThrottledTouchEventHandler(Updatable updatable) {
        this(EVENT_MIN_INTERVAL, updatable);
    }

    private ThrottledTouchEventHandler(int minInterval, Updatable updatable) {
        this.minInterval = minInterval;
        this.updatable = updatable;
    }

    void onTouchEvent(MotionEvent event) {
        if (updatable == null) return;
        long current = System.currentTimeMillis();
        if (current - lastPassedEventTime <= minInterval) {
            return;
        }
        lastPassedEventTime = current;
        updatable.update(event);
    }
}
