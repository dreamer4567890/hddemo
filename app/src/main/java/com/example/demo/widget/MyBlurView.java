package com.example.demo.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.example.demo.utils.BlurUtils;

public class MyBlurView extends AppCompatImageView {
    private Activity activity;
    private int tag = 0;

    public MyBlurView(Context context) {
        super(context);
    }

    public MyBlurView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void show() {
        if (tag++ <= 0) {
            animate().alpha(1f).setDuration(300).start();
        }
    }

    public void hide() {
        if (--tag <= 0) {
            animate().alpha(0f).setDuration(300).start();
        }
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void blur() {
        if (this.activity == null) {
            activity = (Activity) getContext();
        }
        if (tag <= 0) {
            View decorView1 = activity.getWindow().getDecorView();
            Bitmap bitmap = Bitmap.createBitmap(decorView1.getWidth(), decorView1.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            decorView1.draw(canvas);
            setBackground(new BitmapDrawable(getResources(), BlurUtils.blur(activity, bitmap, 14, 0.2f)));
            tag = 0;
        }
    }
}
