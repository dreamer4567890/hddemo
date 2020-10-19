package com.example.demo.utils;

/**
 * Created by liquan on 2020-8-27
 */
public class FastClickUtil {

    private static final int MIN_DELAY_TIME = 500;

    private static long lastClickTime;

    public static boolean isFastClick() {
        return isFastClick(MIN_DELAY_TIME);
    }

    public static boolean isFastClick(int duration) {
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) <= duration) {
            return true;
        }

        lastClickTime = currentClickTime;
        return false;
    }
}
