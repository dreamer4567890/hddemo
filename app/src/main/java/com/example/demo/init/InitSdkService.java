/*
package com.example.demo.init;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

public class InitSdkService extends JobIntentService {
    public static final String TAG = InitSdkService.class.getSimpleName();
    public static final int JOB_ID = 0x003;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, InitSdkService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        initJPush();
        initDB();
        initOss();
        initImageService();
    }

    private void initDB() {
        //创建通用数据库
        DaoActionHelper
                .init()
                .makeCommon(SmartApplication.getContext());
    }

    private void initOss() {
        //OSS环境初始化
        OssBusinessConfig.init();
        OssManager.init(this, OssBusinessConfig.getStsTokenBaseUrl());
    }

    private void initImageService() {
        ImageService.init(ImageService.TYPE_GLIDE);
    }

    private void initJPush() {
        JVerificationInterface.setDebugMode(false);
        JVerificationInterface.init(this, new RequestCallback<String>() {
            @Override
            public void onResult(int code, String result) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BHLog.d(TAG, "init end");
    }
}
*/
