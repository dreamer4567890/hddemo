package com.example.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;

import com.example.demo.utils.QMUIStatusBarHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity {
    private CompositeDisposable disposableContainer;
    private String TAG = "BaseActivity__";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        /*********************************8.0系统透明activity设置了方向会崩溃的问题****************/
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            boolean result = fixOrientation();
            Log.i(TAG, "onCreate fixOrientation when Oreo, result = " + result);
        }
        /*********************************8.0系统透明activity设置了方向会崩溃的问题修改结束****************/
        super.onCreate(savedInstanceState);
        disposableContainer = new CompositeDisposable();
        if (isStatusBarDarkMode()) {
            QMUIStatusBarHelper.setStatusBarDarkMode(this);
        } else {
            lightStatusBar(enableLightStatusBar());
        }
    }

    public boolean isStatusBarDarkMode() {
        return true;
    }

    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }


    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            Log.i(TAG, "avoid calling setRequestedOrientation when Oreo.");
            return;
        }
        super.setRequestedOrientation(requestedOrientation);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        disposableContainer.dispose();
        disposableContainer = null;
        super.onDestroy();
    }


    protected boolean enableLightStatusBar() {
        return true;
    }

    /**
     * 亮色调状态栏
     */
    private void lightStatusBar(boolean light) {
        if (light && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            int flags = decorView.getSystemUiVisibility();
            decorView.setSystemUiVisibility(flags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 沉浸式效果
     */
    protected void immerse() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0);
        }
        View decorView = getWindow().getDecorView();
        int flags = decorView.getSystemUiVisibility();
        decorView.setSystemUiVisibility(flags | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void addDisposable(Disposable disposable) {
        disposableContainer.add(disposable);
    }

    public void restartApplication(Context context) {
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /*@NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        return SkinManager.getInstance()
                .getDelegate(this, this);
    }*/
}
