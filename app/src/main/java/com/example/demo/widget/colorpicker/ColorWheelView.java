
package com.example.demo.widget.colorpicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import static com.example.demo.widget.colorpicker.ThrottledTouchEventHandler.COLOR_PICK_RADIUS_F;
import static com.example.demo.widget.colorpicker.ThrottledTouchEventHandler.SELECTOR_RADIUS_DP;


/**
 * HSV color wheel
 */
public class ColorWheelView extends FrameLayout implements ColorObservable, Updatable {

    private float radius;
    private float centerX;
    private float centerY;

    private float selectorRadiusPx = SELECTOR_RADIUS_DP * 3;

    private PointF currentPoint = new PointF();
    private int currentColor = Color.MAGENTA;
    private boolean onlyUpdateOnTouchEventUp;

    private ColorWheelSelector selector;

    private ColorObservableEmitter emitter = new ColorObservableEmitter();
    private ThrottledTouchEventHandler handler = new ThrottledTouchEventHandler(this);
    private ColorWheelPalette palette;
    private boolean mOnTouch;
    private boolean mIsShowBitmap;
    private PointF centerPoint = new PointF();

    public ColorWheelView(Context context) {
        this(context, null);
    }

    public ColorWheelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorWheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        selectorRadiusPx = SELECTOR_RADIUS_DP * getResources().getDisplayMetrics().density;

        {
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            palette = new ColorWheelPalette(context);
            int padding = (int) selectorRadiusPx;
            palette.setPadding(padding, padding, padding, padding);
            addView(palette, layoutParams);
        }

        {
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            selector = new ColorWheelSelector(context);
            selector.setSelectorRadiusPx(selectorRadiusPx);
            addView(selector, layoutParams);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;
        width = height = Math.min(maxWidth, maxHeight);
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int netWidth = w - getPaddingLeft() - getPaddingRight();
        int netHeight = h - getPaddingTop() - getPaddingBottom();
        radius = Math.min(netWidth, netHeight) * COLOR_PICK_RADIUS_F - selectorRadiusPx;
        if (radius < 0) return;
        centerX = netWidth * 0.5f;
        centerY = netHeight * 0.5f;
        centerPoint.x = centerX;
        centerPoint.y = centerY;
        setColor(currentColor);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (mIsShowBitmap) {
            return super.onTouchEvent(event);
        } else {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    if (mIsShowBitmap) return super.onTouchEvent(event);
                    handler.onTouchEvent(event);
                    return true;
                case MotionEvent.ACTION_UP:
                    update(event);
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void update(MotionEvent event) {
        boolean isTouchUpEvent = event.getActionMasked() == MotionEvent.ACTION_UP;
        updateSelector(event.getX(), event.getY(), isTouchUpEvent, true);
    }

    private int getColorAtPoint(float eventX, float eventY) {
        float x = eventX - centerX;
        float y = eventY - centerY;
        double r = Math.sqrt(x * x + y * y);
        float[] hsv = {0, 0, 1};
        hsv[0] = (float) (Math.atan2(y, -x) / Math.PI * 180f) + 180;
        hsv[1] = whiteRect(Math.max(0f, Math.min(1f, (float) (r / radius))));
        return Color.HSVToColor(hsv);
    }

    /**
     * @param saturation 饱和度
     * @return
     */
    private float whiteRect(float saturation) {
        return saturation <= 0.4f ? 0f : saturation;
    }

    public void setOnlyUpdateOnTouchEventUp(boolean onlyUpdateOnTouchEventUp) {
        this.onlyUpdateOnTouchEventUp = onlyUpdateOnTouchEventUp;
    }

    public void setColor(int r, int g, int b) {
        int color = Color.rgb(r, g, b);
        setColor(color);
    }

    public void setColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        float r = hsv[1] * radius;
        float radian = (float) (hsv[0] / 180f * Math.PI);
        updateSelector((float) (r * Math.cos(radian) + centerX), (float) (-r * Math.sin(radian) + centerY), true, false);
        currentColor = color;
    }

    private void updateSelector(float eventX, float eventY, boolean isTouchUpEvent, boolean isUser) {
        float x = eventX - centerX;
        float y = eventY - centerY;
        double r = Math.sqrt(x * x + y * y);
        if (r > radius) {
            x *= radius / r;
            y *= radius / r;
        }
        currentPoint.x = x + centerX;
        currentPoint.y = y + centerY;
        int color = 0;
        if (!onlyUpdateOnTouchEventUp || isTouchUpEvent) {
            color = getColorAtPoint(currentPoint.x, currentPoint.y);
            emitter.onColor(color, isUser, isTouchUpEvent);
        }
//        selector.setCurrentPoint(currentPoint, color, isTouchUpEvent);
//        BHLog.d("color,", "color:" + color + ",isTouchUpEvent:" + isTouchUpEvent);
        if (mIsShowBitmap) {
            selector.setCurrentPoint(centerPoint, 0xFFFFFFFF, false);
        } else {
            selector.setCurrentPoint(currentPoint, color, isTouchUpEvent);
        }
    }

    public void setShowBitmap(boolean isShowBitmap) {
        this.mIsShowBitmap = isShowBitmap;
        palette.setBitmap(isShowBitmap);
        if (mIsShowBitmap){
            selector.setCurrentPoint(centerPoint, 0xFFFFFFFF, false);
        }
    }

    @Override
    public void subscribe(ColorObserver observer) {
        emitter.subscribe(observer);
    }

    @Override
    public void unsubscribe(ColorObserver observer) {
        emitter.unsubscribe(observer);
    }

    @Override
    public int getColor() {
        return emitter.getColor();
    }
}
