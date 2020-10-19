package com.example.demo.thread;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class CentralBindAction {

    private static final String TAG = CentralBindAction.class.getSimpleName();
    private static final String CENTRAL_BIND_THREAD = "central_bind_thread";

    public static final String TAG_BIND_CLOUD = "bind_to_cloud";


    private static final int EVENT_CMD_BIND_CLOUD = 2;//绑定到云端
    private static final int EVENT_CMD_GET_FAMILY_INFO = 3;//获取家庭信息


    private HandlerThread mWorkerThread;
    private ProcessHandler mHandler;
    private IBindCentralCallBack mCallBack;
    private String mDeviceUUID;
    private String mRandomCode;
    private String mCentral_pid;
    private double mLat;
    private double mLon;


    private class ProcessHandler extends Handler {

        ProcessHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EVENT_CMD_BIND_CLOUD:
                    bindCentralToCloud();
                    break;
                case EVENT_CMD_GET_FAMILY_INFO:
                    getFamilyFromCloud();
                    break;
            }
        }
    }

    /**
     * 绑定到云端
     *
     * @param deviceUUID
     * @param randomCode
     * @param userAccountToken
     * @param callBack
     */
    public void bindCentral(String deviceUUID, String randomCode, String userAccountToken, String central_pid, double lat, double lon, IBindCentralCallBack callBack) {
        mWorkerThread = new HandlerThread(CENTRAL_BIND_THREAD);
        mWorkerThread.start();
        mHandler = new ProcessHandler(mWorkerThread.getLooper());
        mDeviceUUID = deviceUUID;
        mRandomCode = randomCode;
        this.mCentral_pid = central_pid;
        mCallBack = callBack;
        mLat = lat;
        mLon = lon;

        Message msg = mHandler.obtainMessage();
        msg.what = EVENT_CMD_BIND_CLOUD;
        msg.obj = userAccountToken;
        mHandler.sendMessage(msg);
    }


    /**
     * 如果中控绑定成功，同步绑定中控的信息到云端
     */
    private void bindCentralToCloud() {
        mHandler.sendEmptyMessage(EVENT_CMD_GET_FAMILY_INFO);
    }

    /**
     * 根据指定familyId获取家庭信息
     *
     */
    private void getFamilyFromCloud() {

    }


    private void onSuccess() {
        ThreadManager.getInstance().postUITask(new Runnable() {
            @Override
            public void run() {
                if (mCallBack != null) {
                    mCallBack.onSuccess();
                }
            }
        });


        if (mWorkerThread != null) {
            mWorkerThread.quit();
        }
    }

    private void onFailed(final String tag, final int errorCode, final String msg) {
        ThreadManager.getInstance().postUITask(new Runnable() {
            @Override
            public void run() {
                if (mCallBack != null) {
                    mCallBack.onFailed(tag, errorCode, msg);
                }
            }
        });

        if (mWorkerThread != null) {
            mWorkerThread.quit();
        }

    }

    public interface IBindCentralCallBack {

        void onSuccess();

        void onFailed(String tag, int errorCode, String msg);

    }
}
