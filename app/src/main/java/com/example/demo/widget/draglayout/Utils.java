package com.example.demo.widget.draglayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo.utils.ImageUtils;


/**
 * Created by Lisen.Liu on 2018/11/2.
 */

public class Utils {

    private static Canvas sCanvas = new Canvas();

    public static Bitmap getViewSnapshot(View view, int alpha) {
        if (view == null) {
            return null;
        }
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        //return Bitmap.createBitmap(bitmap);

        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        sCanvas.setBitmap(result);
        Paint paint = new Paint();
        paint.setAlpha(alpha);
        sCanvas.drawBitmap(bitmap, 0, 0, paint);
        return result;
    }

    public static Bitmap getViewSnapshot(View view, int alpha, float scale) {
        if (view == null) {
            return null;
        }
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        //return Bitmap.createBitmap(bitmap);

        //Bitmap result = Bitmap.createBitmap((int) (bitmap.getWidth()*scale), (int) (bitmap.getHeight()*scale), Bitmap.Config.ARGB_8888);
        //Bitmap src, float scaleWidth, float scaleHeight, boolean recycle
        Bitmap result = ImageUtils.scale(bitmap, scale, scale, false);
        sCanvas.setBitmap(result);
        Paint paint = new Paint();
        paint.setAlpha(alpha);
        sCanvas.drawBitmap(result, 0, 0, paint);
        return result;
    }

    public static void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{30, 30}, -1);
    }


    public static void setAllChildrenLongClickListener(View view, View.OnLongClickListener listener) {
        view.setOnLongClickListener(listener);
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0, N = vp.getChildCount(); i < N; i++) {
                View child = vp.getChildAt(i);
                if (child.getVisibility() == View.VISIBLE) {
                    setAllChildrenLongClickListener(child, listener);
                }
            }
        }
    }

}
