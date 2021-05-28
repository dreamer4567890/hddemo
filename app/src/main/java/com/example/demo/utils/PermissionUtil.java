package com.example.demo.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * 跳转到相应品牌手机系统权限设置页
 */

public class PermissionUtil {
    private static final String MANUFACTURER_HUAWEI = "huawei";//华为
    private static final String MANUFACTURER_MEIZU = "meizu";//魅族
    private static final String MANUFACTURER_XIAOMI = "xiaomi";//小米
    private static final String MANUFACTURER_OPPO = "oppo";
    public static boolean isAppSettingOpen = false;
    public static final String PACKAGE_NAME = "com.evergrande.iot";

    public static void goToSetting(Activity activity) {
        if (Build.MANUFACTURER.equalsIgnoreCase(MANUFACTURER_MEIZU)) {
            meizu(activity);
        } else if (Build.MANUFACTURER.equalsIgnoreCase(MANUFACTURER_HUAWEI)) {
            huawei(activity);
        } else if (Build.MANUFACTURER.equalsIgnoreCase(MANUFACTURER_XIAOMI)) {
            xiaomi(activity);
        } else if (Build.MANUFACTURER.equalsIgnoreCase(MANUFACTURER_OPPO)) {
            oppo(activity);
        } else {
            //其他
            try {//防止应用详情页也找不到，捕获异常后跳转到设置
                openAppDetailSetting(activity);
            } catch (Exception e) {
                systemConfig(activity);
            }
        }
    }

    public static void huawei(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", PACKAGE_NAME);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            activity.startActivity(intent);
            isAppSettingOpen = false;
        } catch (Exception e) {
            openAppDetailSetting(activity);
        }
    }

    public static void meizu(Activity activity) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", PACKAGE_NAME);
            activity.startActivity(intent);
            isAppSettingOpen = false;
        } catch (Exception e) {
            openAppDetailSetting(activity);
        }
    }

    public static void xiaomi(Activity activity) {
        try { // MIUI 8 9
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", activity.getPackageName());
            activity.startActivity(localIntent);
            isAppSettingOpen = false;
        } catch (Exception e) {
            try { // MIUI 5/6/7
                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", activity.getPackageName());
                activity.startActivity(localIntent);
                isAppSettingOpen = false;
            } catch (Exception e1) { // 否则跳转到应用详情
                openAppDetailSetting(activity);
            }
        }
    }

    public static void oppo(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", PACKAGE_NAME);
            ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
            intent.setComponent(comp);
            activity.startActivity(intent);
            isAppSettingOpen = false;
        } catch (Exception e) {
            openAppDetailSetting(activity);
        }
    }

    public static void systemConfig(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        activity.startActivity(intent);
    }

    /**
     * 获取应用详情页面
     *
     * @return
     */
    private static Intent getAppDetailSettingIntent(Activity activity) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        return localIntent;
    }

    public static void openAppDetailSetting(Activity activity) {
        activity.startActivity(getAppDetailSettingIntent(activity));
        isAppSettingOpen = true;
    }
}
