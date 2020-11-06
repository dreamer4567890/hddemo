package com.example.demo.activity;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.http.ExceptionHandle;
import com.example.demo.mvp.IBaseView;
import com.google.gson.Gson;

import java.util.List;


public abstract class BaseUiActivity extends BaseActivity implements IBaseView {

    public String TAG = "cgh";
    public Context context;
    protected TextView tvTitle;
    protected ImageView ivBack;
    protected ImageView ivSearch;
    protected View actionBar;
    protected ComponentName launchComponentName;
    protected ComponentName componentName;
    protected ProgressDialog dialog;

    private long exitTime = 0;

    protected abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(getLayoutId());
        init();
        initContentView();
        initData();
    }

    protected void initData() {
    }

    protected void initContentView() {

    }

    private void init() {
        dialog = new ProgressDialog(this);
        actionBar = findViewById(R.id.action_bar);
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        ivSearch = findViewById(R.id.iv_search);

        PackageManager packageManager = this.getApplication().getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(this.getPackageName());
        launchComponentName = intent.getComponent();
        componentName = this.getComponentName();
    }

    public void setMyActionBar(String strTitle, boolean isSearch) {
        if (!TextUtils.isEmpty(strTitle)) {
            tvTitle.setText(strTitle);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        if (!isSearch) {
            ivSearch.setVisibility(View.INVISIBLE);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (componentName.toString().equals(launchComponentName.toString())) {
                    exit();
                } else {
                    finish();
                }
            }
        });
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "搜索功能暂未开放", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    public String beanToJson(Object bean) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(bean);
        return jsonStr;
    }

    @Override
    public void showLoadingDialog() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(Object object) {
        Log.d(TAG, "onSuccess: " + beanToJson(object));
    }

    @Override
    public void onFail(ExceptionHandle.ResponseException t) {
        Log.d(TAG, t.message.toString());
    }

    @Override
    public void onCompleted() {
        Log.d(TAG, "onCompleted");
    }

    /**
     * 判断app是否处于前台
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        /**
         * 获取Android设备中所有正在运行的App
         */
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }
}
