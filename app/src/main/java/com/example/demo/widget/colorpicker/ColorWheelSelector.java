package com.example.demo.widget.colorpicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static com.example.demo.widget.colorpicker.ThrottledTouchEventHandler.SELECTOR_RADIUS_DP;

public class ColorWheelSelector extends View {
    public static final int PAINT_STROKE_WIDTH = 10;    //外心圆画笔宽度
    private Paint selectorPaint;
    private Paint changePaint;
    private float selectorRadiusPx = SELECTOR_RADIUS_DP * 3;
    private PointF currentPoint;

    public ColorWheelSelector(Context context) {
        this(context, null);
    }

    public ColorWheelSelector(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorWheelSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //滑动的外圆保持不变
        selectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectorPaint.setColor(Color.WHITE);
        selectorPaint.setStyle(Paint.Style.STROKE);
        selectorPaint.setStrokeWidth(PAINT_STROKE_WIDTH);

        //里面一直变化颜色的圆
        changePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        changePaint.setColor(Color.BLACK);
        changePaint.setStyle(Paint.Style.FILL);
    }
    @SuppressLint("NewApi")
    @Override
    protected void onDraw(Canvas canvas) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        selectorPaint.setShadowLayer(15f, 0f, 0f, Color.GRAY);
        canvas.drawCircle(currentPoint.x, currentPoint.y, selectorRadiusPx, selectorPaint);
        canvas.drawCircle(currentPoint.x, currentPoint.y, selectorRadiusPx - PAINT_STROKE_WIDTH / 2, changePaint);

    }

    public void setSelectorRadiusPx(float selectorRadiusPx) {
        this.selectorRadiusPx = selectorRadiusPx;
    }

    public void setCurrentPoint(PointF currentPoint, int color, boolean isTouchUpEvent) {
        this.currentPoint = currentPoint;
        changePaint.setColor(color);
        selectorRadiusPx = isTouchUpEvent ? SELECTOR_RADIUS_DP * 3f : SELECTOR_RADIUS_DP * 3.5f;
        invalidate();
    }
}
