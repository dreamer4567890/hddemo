/**
 *****************************************************************************
 * Copyright (C) 2018-2023 HD Corporation. All rights reserved
 * File        : BaseDialog.java
 *
 * Description : BaseDialog,
 *
 * Creation    : 2018-08-09
 * Author      : tanguilong@evergrande.cn
 * History     : 2018-08-09, Creation
 *****************************************************************************
 */
package com.example.demo.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.demo.activity.BaseActivity;

import io.reactivex.disposables.Disposable;

public class BaseDialog extends Dialog implements Disposable {

    public final static String TAG = "BaseDialog";

    private Context mContext;
    private boolean mDisposed;

    public BaseDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @Override
    public void show() {
        if (mContext instanceof BaseActivity) { // 在Activity销毁时，自动关闭对话框
            BaseActivity activity = (BaseActivity) mContext;
            activity.addDisposable(this);
        }
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        super.show();
    }

    @Override
    public void dispose() { // Activity退出时调用
        Log.i(TAG, String.format("dispose %s", this));

        dismiss();
        mDisposed = true;
    }

    @Override
    public boolean isDisposed() {
        Log.i(TAG, String.format("%s isDisposed: %s", this, mDisposed));

        return mDisposed;
    }

}
