package com.example.demo.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

/**
 * Created by hef on 2017/5/22.
 */

public class DynamicPermission {
    private static final String TAG = "DynamicPermission";

    public static final int REQUEST_MY_PERMISSIONS = 1;
    public static final int REQUEST_TAKE_CAMERA = 2;
    public static final int REQUEST_EXTERNAL_STORAGE = 3;
    public static final int REQUEST_PHONE_STATE = 4;
    public static final int REQUEST_RECORD_AUDIO = 5;
    public static final int REQUEST_CONTACTS = 6;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final int MASK_PERMISSION_CAMERA = 1;
    public static final int MASK_PERMISSION_PHONE_STATE = 2;
    public static final int MASK_PERMISSION_CONTACTS = 4;
    public static final int MASK_PERMISSION_RECORD_AUDIO = 8;
    public static final int MASK_PERMISSION_EXTERNAL_STORAGE_WRITE = 16;

    public static final int PERMISSION_CAMERA = 1;
    public static final int PERMISSION_PHONE_STATE = 2;
    public static final int PERMISSION_CONTACTS = 3;
    public static final int PERMISSION_RECORD_AUDIO = 4;
    public static final int PERMISSION_EXTERNAL_STORAGE_WRITE = 5;

    public static void verifyPermissions(Activity activity, int mask) {
        String[] permissions = new String[10];
        boolean apply = false;
        int permit = PERMISSION_DENIED;
        int i = 0;

        if (Build.VERSION.SDK_INT < 23) {
            return;
        }

        /* 需要申请照相权限 */
        if ((mask & MASK_PERMISSION_CAMERA) != 0) {
            permit = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

            if (permit != PackageManager.PERMISSION_GRANTED) {
                permissions[i++] = Manifest.permission.CAMERA;
                apply = true;
            }
        }

        /* 需要申请获取手机信息权限 */
        if ((mask & MASK_PERMISSION_PHONE_STATE) != 0) {
            permit = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);

            if (permit != PackageManager.PERMISSION_GRANTED) {
                permissions[i++] = Manifest.permission.READ_PHONE_STATE;
                apply = true;
            }
        }

        /* 需要申请读取电话本权限 */
        if ((mask & MASK_PERMISSION_CONTACTS) != 0) {
            permit = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);

            if (permit != PackageManager.PERMISSION_GRANTED) {
                permissions[i++] = Manifest.permission.READ_CONTACTS;
                apply = true;
            }
        }

        /* 需要音视频录取权限 */
        if ((mask & MASK_PERMISSION_RECORD_AUDIO) != 0) {
            permit = ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);

            if (permit != PackageManager.PERMISSION_GRANTED) {
                permissions[i++] = Manifest.permission.RECORD_AUDIO;
                apply = true;
            }
        }

        /* 需要申请写外部存储器权限 */
        if ((mask & MASK_PERMISSION_EXTERNAL_STORAGE_WRITE) != 0) {
            permit = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permit != PackageManager.PERMISSION_GRANTED) {
                permissions[i++] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                apply = true;
            }
        }

        if (apply) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_MY_PERMISSIONS);
        }
    }

    public static void verifyCameraPermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "permission = " + permission);
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_TAKE_CAMERA);
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "permission = " + permission);
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * Function: verifyPhonebookPermissions()
     * 请求手机电话本读取的权限
     **/
    public static void verifyPhonebookPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) !=
                    PackageManager.PERMISSION_GRANTED) {

                //请求获取联系人权限
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS},
                        REQUEST_CONTACTS);
            }
        }
    }

    public static void verifyPhoneStatePermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) !=
                    PackageManager.PERMISSION_GRANTED) {

                //请求获取手机设备信息权限
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE},
                        REQUEST_PHONE_STATE);
            }
        }
    }

    public static void verifyRecordAudioPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) !=
                    PackageManager.PERMISSION_GRANTED) {

                //请求获取摄像头权限
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_RECORD_AUDIO);
            }
        }
    }

    public static boolean isAllowed(Context context, int permit) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }

        String permission = null;
        switch (permit) {
            case PERMISSION_CAMERA:
                permission = Manifest.permission.CAMERA;
                break;
            case PERMISSION_PHONE_STATE:
                permission = Manifest.permission.READ_PHONE_STATE;
                break;
            case PERMISSION_CONTACTS:
                permission = Manifest.permission.READ_CONTACTS;
                break;
            case PERMISSION_RECORD_AUDIO:
                permission = Manifest.permission.RECORD_AUDIO;
                break;
            case PERMISSION_EXTERNAL_STORAGE_WRITE:
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                break;
        }

        if (permission == null) {
            return false;
        } else {
            return (ActivityCompat.checkSelfPermission(context, permission) ==
                    PackageManager.PERMISSION_GRANTED);
        }
    }
}

