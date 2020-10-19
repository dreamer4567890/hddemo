package com.example.demo.utils;

import static com.example.demo.permission.DynamicPermission.MASK_PERMISSION_CAMERA;
import static com.example.demo.permission.DynamicPermission.MASK_PERMISSION_CONTACTS;
import static com.example.demo.permission.DynamicPermission.MASK_PERMISSION_EXTERNAL_STORAGE_WRITE;
import static com.example.demo.permission.DynamicPermission.MASK_PERMISSION_PHONE_STATE;
import static com.example.demo.permission.DynamicPermission.MASK_PERMISSION_RECORD_AUDIO;

/**
 * @author guotingzhu
 * @date 2019/4/16.
 * 简述：
 * 项目工程的通用全局变量或常量放在这里
 *
 */
public class CommonConfig {

    /**
     * 是否显示水印
     */
    public static boolean ENABLE_WATERMARK = true;
    /**
     * 是手机还是Pad
     */
    public static String OUTPUT_TYPE = "";
    /**
     * 手机的appId
     */
    public static String APPLICATION_ID = "";

    public static final String FEEDBACK_URL="feedback_url";

    public static final String MAIN_TAB_SELECT_POS="main_tab_select_pos"; //首页tab选择pos

    public static final String MULTI_PIC_NUM="multi_pic_num";

    public static final String MULTI_PIC_RESULT="multi_pic_result";

    // 头像临时目录
    public static final String imgCacheTempDir = StorageUtils.getAPPCachePath()+"temp/";

    public static final String imgBaseDir = StorageUtils.getAPPStoragePath("img/");


    public static class DynamicPermission{
        public static int PERMISSION_REQUIREMENTS = MASK_PERMISSION_EXTERNAL_STORAGE_WRITE |
                MASK_PERMISSION_CAMERA | MASK_PERMISSION_PHONE_STATE | MASK_PERMISSION_CONTACTS |
                MASK_PERMISSION_RECORD_AUDIO;
    }
}
