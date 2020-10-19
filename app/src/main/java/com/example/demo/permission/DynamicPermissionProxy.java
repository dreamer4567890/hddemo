package com.example.demo.permission;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by hefeng on 2019/7/18.
 */

public class DynamicPermissionProxy {
    private static final String TAG = "DynamicPermissionProxy";

    int mPosition = 0;
    private List<PermissionBean> mPermitList = new ArrayList<>();
    private PermissionBean mCurrentPermission;

    private static class PermissionBean{
        private int requestCode;
        private String permission;
    }

    public static DynamicPermissionProxy newInstance(Activity activity, Observer observer){
        return new DynamicPermissionProxy(activity, observer);
    }

    private DynamicPermissionProxy(Activity target, Observer observer){
        mTarget = target;
        mObserver = observer;
    }

    private Activity mTarget;
    private Observer mObserver;

    public void addPermission(int requestCode, String permission){
        PermissionBean bean = new PermissionBean();
        bean.requestCode = requestCode;
        bean.permission = permission;
        mPermitList.add(bean);
    }

    public void checkFirstPermission(){
        mPosition = 0;
        checkNextPermission();
    }

    public void checkNextPermission(){
        PermissionBean permissionBean = getPermission();
        if(permissionBean == null){
            return;
        }
        mCurrentPermission = permissionBean;
        mPosition++;

        if(ActivityCompat.checkSelfPermission(mTarget, permissionBean.permission) == PERMISSION_GRANTED){
            checkNextPermission();
        } else {
            ActivityCompat.requestPermissions(mTarget, new String[]{permissionBean.permission}, permissionBean.requestCode);
        }
    }

    private PermissionBean getPermission(){
        if(mPosition < 0){
            return null;
        }

        if(mPermitList.size() <= mPosition){
            return null;
        } else {
            return mPermitList.get(mPosition);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        BHLog.d(TAG, "onRequestPermissionsResult(), requestCode = " + requestCode + ", permissions[0] = "
//                + permissions[0] + ", grantResults[0] = " + grantResults[0]);

        if(mCurrentPermission == null ){
            return;
        }

        boolean result = true;
        if(requestCode == mCurrentPermission.requestCode) {
            if (grantResults[0] != PERMISSION_GRANTED) {
                if(ActivityCompat.shouldShowRequestPermissionRationale(mTarget, mCurrentPermission.permission)){
                    if(mObserver != null) {
                        result = mObserver.onNeverAskAgain(requestCode, mCurrentPermission.permission, grantResults[0]);
                    }
                } else {
                    if(mObserver != null) {
                        result = mObserver.onPermissionDenied(requestCode, mCurrentPermission.permission, grantResults[0]);
                    }
                }
            } else {
                if(mObserver != null){
                    mObserver.onPermissionGranted(requestCode, mCurrentPermission.permission, grantResults[0]);
                }
            }
        }

        if(result) {
            checkNextPermission();
        }
    }

    public static interface Observer{
        // 返回值，true，表示继续检查下一权限；false，表示中止权限的检查
        public boolean onPermissionDenied(int requestCode, String permission, int grantResult);
        public void onPermissionGranted(int requestCode, String permission, int grantResult);
        public boolean onNeverAskAgain(int requestCode, String permission, int grantResult);
    }
}
