package com.example.demo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * author:     xumin
 * date:       2018/4/25
 * email:      xumin2@evergrande.cn
 */
public class BitmapUtils {
    private static final String TAG = "BitmapUtils";

    private static BitmapCache sBitmapCache;

    static {
        int bitmapCacheSize = (int) (Runtime.getRuntime().maxMemory() / 1024 / 8); // 单位KB
        sBitmapCache = new BitmapCache(bitmapCacheSize);
    }

    public static BitmapCache getBitmapCache() {
        return sBitmapCache;
    }

    /**
     * 从缓冲中删除指定Bitmap
     *
     * @param srcPath Bitmap原文件路径
     */
    public static void deleteBitmapFromCache(String srcPath) {
        if (TextUtils.isEmpty(srcPath)) {
            Log.i(TAG, "deleteBitmapFromCache: srcPath is null or empty");
            return;
        }

        deleteBitmapFromCacheInner(srcPath);
    }

    /**
     * 批量删除Bitmap
     *
     * @param srcPaths Bitmap原文件路径列表
     */
    public static void deleteBitmapFromCache(final String[] srcPaths) {
        if (srcPaths == null || srcPaths.length <= 0) {
            Log.i(TAG, "deleteBitmapFromCache: srcPaths is null or empty");
            return;
        }

        Log.i(TAG, "deleteBitmapFromCache: removing bitmap batchly");

        for (String srcPath : srcPaths) {
            deleteBitmapFromCacheInner(srcPath);
        }
    }

    /**
     * 从缓冲中删除指定Bitmap
     *
     * @param resId Bitmap的R.drawable.XXX
     */
    public static void deleteBitmapFromCache(int resId) {
        if (resId <= 0) {
            Log.i(TAG, String.format("deleteBitmapFromCache: resId(%d) is illegal", resId));
            return;
        }

        String keyId = String.format("R.drawable.%d", resId);
        deleteBitmapFromCacheInner(keyId);
    }

    private static void deleteBitmapFromCacheInner(String keyId) {
        String md5Name = FileUtils.getFileMD5ToString(keyId);
        Log.i(TAG, String.format("deleteBitmapFromCacheInner: removing %s(md5:%s) from cache", keyId, md5Name));

        Bitmap bitmap = sBitmapCache.remove(md5Name);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    /**
     * 从缓存中加载Bitmap
     *
     * @param srcPath
     * @return
     */
    public static Bitmap getBitmapFromCache(String srcPath) {
        if (TextUtils.isEmpty(srcPath)) {
            Log.i(TAG, "getBitmapFromCache: srcPath is null or empty");
            return null;
        }

        String md5Name = FileUtils.getFileMD5ToString(srcPath);
        Bitmap cachedBitmap = sBitmapCache.getBitmap(md5Name);
        if (cachedBitmap != null && !cachedBitmap.isRecycled()) { // 从缓存命中Bitmap
            return cachedBitmap;
        }

        return null;
    }

    /**
     * Bitmap加载采用三级缓存策略：
     * 1) 一级缓存为内存缓存
     * 2) 二级缓存为磁盘缓存
     * 其中一级缓存基于LruCache实现，二级缓存由OSS实现
     *
     * @param srcPath 待加载图片的路径
     * @param hh      待加载图片的高度
     * @param ww      待加载图片的宽度
     * @return 待加载图片的Bitmap
     */
    public static Bitmap getimage(String srcPath, float hh, float ww) {
        Bitmap bitmap = getBitmapFromCache(srcPath);
        if (bitmap != null) { // 从缓存命中Bitmap
            return bitmap;
        }

        // 缓存未命中Bitmap, 从磁盘加载Bitmap
        return getimage(srcPath, 0, hh, ww);
    }

    /**
     * @param srcPath 本地图片路径
     * @param size    想要压缩到多大（kb）
     * @param hh      图片压缩高度
     * @param ww      图片压缩宽度
     * @return 位图
     */
    public static Bitmap getimage(String srcPath, int size, float hh, float ww) {
        // 该对象为图片缩放操作对象
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，该参数为true时，只会在读取图片的宽高 而不产生bitmap 无论图片大小 都用他读取。
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
        //读取后一定要 设置成false;
        newOpts.inJustDecodeBounds = false;
        //获取 原图图片的长和宽
        int realw = newOpts.outWidth;
        int realh = newOpts.outHeight;
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int options = 1;//be=1表示不缩放
        if (realw > hh && realw > ww) {
            //如果宽度大的话根据宽度固定大小缩放
            options = (int) (newOpts.outWidth / ww);
        } else if (realw < realh && realh > hh) {
            //如果高度高的话根据宽度固定大小缩放
            options = (int) (newOpts.outHeight / hh);
        }
        if (options <= 0)
            options = 1;

        newOpts.inSampleSize = options;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //如果是一张超大图的话  经过以上处理 图片应该就变成了一个800x480的图片了
        //以上是尺寸 缩放，一般这样处理后就不会oom了，如果还要oom 就进行质量压缩
        if (size > 0) {
            bitmap = compressImage(bitmap, size);
        }
        // 将bitmap放入缓存, 以备下次使用
        String md5Name = FileUtils.getFileMD5ToString(srcPath);
        sBitmapCache.updateBitmap(md5Name, bitmap, true);

        return bitmap;
    }


    /**
     * @param image 要压缩的bitmap
     * @param size  要控制的大小（单位为kb）
     * @return
     */
    private static Bitmap compressImage(Bitmap image, int size) {
        if (image == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        Log.i(TAG, "image size before=" + baos.toByteArray().length);
        while (baos.toByteArray().length > size) {  //循环判断如果压缩后图片是否大于size kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Log.i(TAG, "image size after=" + baos.toByteArray().length);
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        // 释放无用资源
        image.recycle();

        return bitmap;
    }

    /**
     * Bitmap缓存，支持线程安全
     */
    public static class BitmapCache extends LruCache<String, Bitmap> {

        public final static String TAG = "BitmapCache";

        public BitmapCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            // 返回cache unit大小，单位：KB
            return value.getRowBytes() * value.getHeight() / 1024;
        }

        public Bitmap getBitmap(String key) {
            if (TextUtils.isEmpty(key)) {
                Log.i(TAG, "getBitmap: key is null or empty");
                return null;
            }

            Bitmap bitmap;
            synchronized (this) {
                bitmap = get(key);
            }


            if (bitmap == null) {
                Log.i(TAG, "getBitmap: bitmap is null");
                return null;
            }

            if (bitmap.isRecycled()) {
                Log.i(TAG, String.format("getBitmap: key(%s)'s Bitmap has already recycled", key));

                remove(key);
                return null;
            }

            return bitmap;
        }

        /**
         * 添加Bitmap到缓存
         *
         * @param key
         * @param value 待添加的Bitmap
         * @return null - 添加Bitmap失败
         * Bitmap - 参数value引用的Bitmap，表示添加成功
         */
        public synchronized Bitmap addBitmap(String key, Bitmap value) {
            if (TextUtils.isEmpty(key) || value == null) {
                Log.i(TAG, "addBitmap: Illegal params");
                return null;
            }

            if (get(key) != null) {
                Log.i(TAG, String.format("addBitmap: BitmapCache already contains (%s,%s)", key, value));
                return null;
            }

            put(key, value);

            return value;
        }

        /**
         * 更新缓存中的Bitmap
         *
         * @param key
         * @param value
         * @param recycleOld false - 不回收被替换的Bitmap
         *                   true - 回收被替换的Bitmap
         * @return 旧的Bitmap或null(旧的Bitmap被回收)
         */
        public synchronized Bitmap updateBitmap(String key, Bitmap value, boolean recycleOld) {
            Bitmap oldBitmap = updateBitmap(key, value);

            if (oldBitmap == null || !recycleOld) { // 无需回收旧的Bitmap, 直接返回旧的Bitmap
                Log.i(TAG, String.format("updateBitmap: no need recycling oldBitmap, and then return oldBitmap(%s)", oldBitmap));
                return oldBitmap;
            }

            if (!oldBitmap.isRecycled()) { // 释放Bitmap
                Log.i(TAG, String.format("updateBitmap: recycle oldBitmap(%s)", oldBitmap));
                oldBitmap.recycle();
            }
            // 旧的Bitmap被回收， 直接返回null
            return null;
        }

        public synchronized Bitmap updateBitmap(String key, Bitmap value) {
            if (TextUtils.isEmpty(key)) {
                Log.i(TAG, "updateBitmap: key is null or empty");
                return null;
            }

            Bitmap oldBitmap = get(key);
            if (value == null) {
                Log.i(TAG, String.format("updateBitmap: value is null, removing key(%s) mapping", key));
                // 传入空值，则删除key对应的Bitmap
                remove(key);
                return oldBitmap;
            }

            return put(key, value);
        }

        public synchronized void deleteBitmap(String key) {
            if (TextUtils.isEmpty(key)) {
                Log.i(TAG, "deleteBitmap: key is null or empty");
                return;
            }

            remove(key);
        }
    }

    /**
     * 生成水印文字图片
     */
    public static BitmapDrawable drawTextToBitmap(Context gContext, String gText, int gCanvasColor, int paintColor) {

        if (CacheUtils.get(gContext).getAsBitmap(gText) != null) {
            BitmapDrawable drawable = new BitmapDrawable(CacheUtils.get(gContext).getAsBitmap(gText));
            drawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            drawable.setDither(true);
            Log.i(TAG, "111 drawTextToBitmap  drawable=" + drawable);

            return drawable;
        }
        try {
            Bitmap bitmap = Bitmap.createBitmap(550, 350, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(gCanvasColor);
            Paint paint = new Paint();
            paint.setColor(paintColor);
            paint.setAlpha(80);
            paint.setAntiAlias(true);
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTextSize(30);
            Path path = new Path();
            path.moveTo(0, 320);
            path.lineTo(450, 0);
            canvas.drawTextOnPath(gText, path, 0, 20, paint);
           // canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
            CacheUtils.get(gContext).put(gText, bitmap);
            ImageUtils.save(bitmap, FileUtils.getFileByPath(StorageUtils.getAPPStoragePath("img/watermark.png")), Bitmap.CompressFormat.PNG);
            BitmapDrawable drawable = new BitmapDrawable(bitmap);
            drawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            drawable.setDither(true);
            bitmap.recycle();
            Log.i(TAG, "drawTextToBitmap  drawable=" + drawable);
            return drawable;
        } catch (Exception e) {

        }
        return null;

    }

    /**
     * 部分手机图片旋转情况处理
     *
     * @param path
     */
    public static void tryRotate(String path) {
        OutputStream outputStream = null;
        try {
            Matrix mat = new Matrix();
            ExifInterface ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    mat.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    mat.postRotate(180);
                    break;
                default:
                    return;
            }
            Bitmap bitmap = BitmapUtils.getimage(path, 2000, 2000);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
            File file = new File(path);
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (Exception e) {
            Log.e("", e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 得到bitmap的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }
}
