package com.example.demo.widget.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.demo.R;

import static com.example.demo.widget.colorpicker.ThrottledTouchEventHandler.COLOR_PICK_RADIUS_F;


public class ColorWheelPalette extends View {

    private float radius;
    private float centerX;
    private float centerY;

    private Paint huePaint;
    private Paint saturationPaint;
    private Bitmap mBitmap;
    private Bitmap mBitmap2;

    private Matrix mMatrix;
    private boolean mIsShowBitmap;

    public ColorWheelPalette(Context context) {
        this(context, null);
    }

    public ColorWheelPalette(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorWheelPalette(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //将背景图片大小设置为属性设置的直径;
        mBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_color_pickter_off);
        mBitmap2 = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_color_pickter_bg);
        //获取想要缩放的matrix
        mMatrix = new Matrix();
        huePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        saturationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    public void setBitmap(boolean isShowBitmap) {
        this.mIsShowBitmap = isShowBitmap;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int netWidth = w - getPaddingLeft() - getPaddingRight();
        int netHeight = h - getPaddingTop() - getPaddingBottom();
        radius = Math.min(netWidth, netHeight) * COLOR_PICK_RADIUS_F;
        if (radius < 0) return;
        centerX = w * 0.5f;
        centerY = h * 0.5f;
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        //计算压缩的比率
        float scaleWidth = ((float) radius * 2) / width;
        float scaleHeight = ((float) radius * 2) / height;

        mMatrix.postScale(scaleWidth, scaleHeight);
        //获取新的bitmap
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, mMatrix, true);
        mBitmap2 = Bitmap.createBitmap(mBitmap2, 0, 0, width, height, mMatrix, true);
        Shader hueShader = new SweepGradient(centerX, centerY,
                new int[]{Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED},
                null);
        huePaint.setShader(hueShader);

//        int[] colorS = new int[]{0xFFFFFFFF, 0x00FFFFFF};
//        float[] posS = new float[]{0.25f, 1f};
        Shader saturationShader = new RadialGradient(centerX, centerY, radius,
                0xffffffff, 0x00FFFFFF, Shader.TileMode.REPEAT);
        saturationPaint.setShader(saturationShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mIsShowBitmap){
            canvas.drawCircle(centerX, centerY, radius, huePaint);
            canvas.drawCircle(centerX, centerY, radius, saturationPaint);
        }
        // 画背景图片
        int padding = (getWidth() - mBitmap.getWidth()) / 2;
        canvas.drawBitmap(mIsShowBitmap ? mBitmap : mBitmap2, padding, padding, huePaint);

    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
