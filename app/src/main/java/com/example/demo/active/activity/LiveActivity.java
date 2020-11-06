package com.example.demo.active.activity;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.demo.R;
import com.example.demo.activity.BaseUiActivity;

public class LiveActivity extends BaseUiActivity {

    public static final String TAG = LiveActivity.class.getSimpleName();

    public static void actionToLiveActivity(Context context) {
        Intent intent = new Intent(context, LiveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live;
    }

    @Override
    protected void initData() {
        super.initData();
        Window window = getWindow();
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = 1;
        layoutParams.height = 1;
        layoutParams.x = 0;
        layoutParams.y = 0;
        window.setAttributes(layoutParams);

        ScreenManager.getInstance(this).setActivity(this);
    }
}
