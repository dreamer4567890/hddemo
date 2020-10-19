package com.example.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * 手势下滑退出页面
 * Activity和Fragment都适用
 */
public class TopToBottomFinishLayout extends RelativeLayout {
    public static long FAST_SLIDE_FINISH_TIME = 300;//快速下滑收起临界时间值
    public static int FAST_SLIDE_FINISH_DISTANCE = 300;//快速下滑收起距离临界值
    private ViewGroup mParentView;//TopToBottomFinishLayout布局的父布局
    private int mTouchSlop;//滑动的最小距离
    private int downX;//按下点的X坐标
    private int downY;//按下点的Y坐标
    private int tempY;//临时存储Y坐标
    private Scroller mScroller;
    private int viewHeight;
    private boolean isSliding;
    private OnFinishListener onFinishListener;
    private OnFragmentFinishListener onFragmentFinishListener;
    private boolean isFinish;
    private long time;//快速下滑收起
    private int distance;//快速下滑收起距离
    private long timeDown;
    private long timeUp;
    private int distanceDown;
    private int distanceUp;

    public TopToBottomFinishLayout(Context context) {
        this(context, null);
    }

    public TopToBottomFinishLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopToBottomFinishLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
    }

   /* @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getRawX();
                downY = tempY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop && Math.abs((int) ev.getRawX() - downX) < mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getRawX();
                downY = tempY = (int) event.getRawY();
                timeDown = System.currentTimeMillis();
                distanceDown = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) event.getRawY();
                int deltaY = tempY - moveY;
                tempY = moveY;
                if (Math.abs(moveY - downY) > mTouchSlop && Math.abs((int) event.getRawX() - downX) < mTouchSlop) {
                    isSliding = true;
                }
                if (moveY - downY >= 0 && isSliding) {
                    mParentView.scrollBy(0, deltaY);
                    if (onFragmentFinishListener != null) {
                        onFragmentFinishListener.onFragmentFinish(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                timeUp = System.currentTimeMillis();
                time = timeUp - timeDown;
                distanceUp = (int) event.getRawY();
                distance = distanceUp - distanceDown;
                isSliding = false;
                if (mParentView.getScrollY() <= -viewHeight / 3
                        || (time <= FAST_SLIDE_FINISH_TIME && distance >= FAST_SLIDE_FINISH_DISTANCE)) {
                    isFinish = true;
                    scrollBottom();
                } else {
                    scrollOrigin();
                    isFinish = false;
                }
                break;
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            mParentView = (ViewGroup) this.getParent();
            viewHeight = this.getHeight();
        }
    }

    public void setOnFinishListener(OnFinishListener onSlidingFinishListener) {
        this.onFinishListener = onSlidingFinishListener;
    }

    public void setOnFragmentFinishListener(OnFragmentFinishListener onFragmentFinishListener) {
        this.onFragmentFinishListener = onFragmentFinishListener;
    }

    /**
     * 滚动出界面
     */
    private void scrollBottom() {
        int delta = (viewHeight + mParentView.getScrollY());
        mScroller.startScroll(0, mParentView.getScrollY(), 0, -delta + 1, 300);
        postInvalidate();
        if (onFragmentFinishListener != null) {
            onFragmentFinishListener.onFragmentFinish(true);
        }
    }

    /**
     * 滚动到起始位置
     */
    public void scrollOrigin() {
        int delta = mParentView.getScrollY();
        mScroller.startScroll(0, mParentView.getScrollY(), 0, -delta, /*Math.abs(delta)*/300);
        postInvalidate();
        if (onFragmentFinishListener != null) {
            onFragmentFinishListener.onFragmentFinish(false);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mParentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();

            if (mScroller.isFinished() && isFinish) {
                if (onFinishListener != null) {
                    onFinishListener.onFinish();
                } else {
                    scrollOrigin();
                    isFinish = false;
                }
            }
        }
    }

    //(x,y)是否在view的区域内
    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }

    public interface OnFinishListener {
        void onFinish();
    }

    //专用于Fragment的接口，获取上个Fragment实例并将其改为可见
    public interface OnFragmentFinishListener {
        void onFragmentFinish(boolean visibility);
    }

}
