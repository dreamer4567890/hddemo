package com.example.demo.widget.draglayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.demo.widget.draglayout.viewanimator.AnimationListener;
import com.example.demo.widget.draglayout.viewanimator.ViewAnimator;


public class NewLauncherView<T> extends RelativeLayout {
    private static final String TAG = NewLauncherView.class.getSimpleName();


    public interface IDragActionCallback<T> {

        void onDrop(View dragView, int startPosition, int lastPosition, int currentPosition, boolean out);

        boolean prepareInsert(int startPos, int targetPost);

        void unPrepareInsert();

        boolean onSwap(View dragView, int startPosition, int lastPosition, int currentPosition, boolean out);

        /**
         * 合并文件夹
         *
         * @param dragView
         * @param startPosition
         * @param lastPosition
         * @param currentPosition
         */
        boolean onInsert(View dragView, int startPosition, int lastPosition, int currentPosition, boolean needOpenPack);

        void unPack(View dragView, int startPosition, boolean hidePack);
    }


    public NewLauncherView(@NonNull Context context) {
        this(context, null);
    }

    public NewLauncherView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewLauncherView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Point outSize = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getRealSize(outSize);
        mScreenWidth = outSize.x;
        mScreenHeight = outSize.y;
    }

    private IDragActionCallback<T> mDragCallback;
    // private int mStartDragPageIndex = -1;
    private RectF mDrawRegion = new RectF();
    private static final int DRAG_STATE_IDLE = 0;
    private static final int DRAG_STATE_START = 1;
    private static final int DRAG_STATE_DRAG = 2;
    private static final int DRAG_STATE_ANIMATION = 3;
    private static final int PACK_VIEW_TOP_OFFSET = 120;

    private int mDragState = -1;
    private PointF mLastPoint = new PointF();
    private Bitmap mDragSnapShot;
    private Bitmap mDragSnapShotEdge;
    private View mDragView;
    private RecyclerView mRecyclerView;
    //private ViewPager mViewPager;
    //  private T mData;
    private int TYPE_HOME = 1;
    private int TYPE_PACKAGE = 2;
    private int mItemStartPosition;
    private float mTopOffset;
    private float mLeftOffset;
    private float mBottomOffset;
    private int mItemLastPosition;
    private boolean mDoDropAnimation;
    private int mDragPageRangeStart = -1;
    private int mDragPageRangeEnd = -1;
    private volatile boolean mIsTouch = false;
    private int type = TYPE_HOME;
    private FROMTYPE fromType = FROMTYPE.UNDEFINE;
    private boolean packed = false;
    private long SWAP_TIME = 300;
    private long HIDE_TIME = 300;
    // private long INSET_TIME = 600;
    private long INSET_AND_OPEN_PACK_TIME = 400;
    private long INSERT_GAP = 100;
    private float scale = 0.9f;
    private float mDrawRegionLeft, mDrawRegionRight, mDrawRegionTop, mDrawRegionBottom;
    private boolean isIn = false;
    private int mScreenHeight;
    private int mScreenWidth;
    private XNestedScrollView xNestedScrollView;

    public void setDragPosition(IDragActionCallback<T> dragPosition) {
        mDragCallback = dragPosition;
    }

    public void startDrag(View childView, int startPosition, boolean doDropAnimation, int headerHeight, RecyclerView recyclerView) {
        Log.i(TAG, "dragLayout, startPosition=" + startPosition + ", doDropAnimation=" + doDropAnimation + "   headerHeight=" + headerHeight);
        if (!mIsTouch) {
            Log.e(TAG, "startDrag because touch event has release, do nothing.");
            return;
        }
        mDragView = childView;
        mRecyclerView = recyclerView;
        mItemStartPosition = startPosition;
        mItemLastPosition = startPosition;
        mDoDropAnimation = doDropAnimation;
        mRecyclerViewScrollState = RECYCLER_VIEW_SCROLL_IDLE;

        float offsetX = 0;
        float offsetY = 0;

        ViewParent vp = childView.getParent();
        while (vp instanceof View && vp != this) {
            if (!(vp.getParent() instanceof MyRelativeLayout)) {
                offsetX += ((View) vp).getX();
                offsetY += ((View) vp).getY();
            }
            vp = vp.getParent();
        }

        mTopOffset = offsetY - headerHeight;

        if (mDragSnapShot != null) {
            mDragSnapShot.recycle();
        }


        mDragSnapShotEdge = Utils.getViewSnapshot(childView, 0xFF);
        mDragSnapShot = Utils.getViewSnapshot(childView, 0xFF, scale);
        mDrawRegionLeft = mLastX - childView.getWidth() / 2;
        mDrawRegionRight = mLastX + childView.getWidth() / 2;
        mDrawRegionTop = mLastY - childView.getHeight() / 2 - mTopOffset;
        mDrawRegionBottom = mLastY + childView.getHeight() / 2 - mTopOffset;
        mDrawRegion.set(mDrawRegionLeft, mDrawRegionTop, mDrawRegionRight, mDrawRegionBottom);
        mDrawRegion.offset(offsetX, offsetY);
        mDragState = DRAG_STATE_START;
        Utils.vibrate(getContext());
        mDragView.setVisibility(INVISIBLE);
        postInvalidate();
    }

    public void setBottomOffset(float bottomOffset) {
        this.mBottomOffset = bottomOffset;
    }

    private void endDrag() {
        resetHandler();
        if (mDoDropAnimation) {
            mDragState = DRAG_STATE_ANIMATION;
            doAnimationOnDrop();
        } else {
            mDragState = DRAG_STATE_IDLE;
            postInvalidate();
        }
        mDragView = null;
    }


    private void onDrop() {
        if (mDragState == DRAG_STATE_IDLE) return;
        if (mDragCallback != null) {
            int tmp = -1;
            boolean out = isOut(mLastX, mLastY);
            if (type == TYPE_PACKAGE) {
                if (!isIn) {
                    isIn = isInSlide(mLastX, mLastY);
                }

                if (out && isIn && !packed) {
                    mDragCallback.unPack(mDragView, mItemStartPosition, false);
                    return;
                }
            }


            boolean canInsert = false;
//            if (type == TYPE_HOME) {
                Object[] values = findInsertOrSwapPosition(mLastX, mLastY);
                tmp = (int) values[0];
                canInsert = (boolean) values[1];
//            } else {
//                tmp = findInsertPosition(mLastX, mLastY);
//            }

            if (canInsert && mItemStartPosition != tmp && prepared) {
                final int animTmp = tmp;
                final View endView = mDragView;
                // 合入的动画，做完再合入文件夹
                final ImageView temiv = new ImageView(getContext());
                temiv.setImageBitmap(mDragSnapShot);
                addView(temiv);
                temiv.setVisibility(View.INVISIBLE);
                temiv.post(new Runnable() {
                    @Override
                    public void run() {
                        ViewAnimator.animate(temiv).onStart(new AnimationListener.Start() {
                            @Override
                            public void onStart() {
                                temiv.setTranslationX(mDrawRegion.left);
                                temiv.setTranslationY(mDrawRegion.top);
                                temiv.setVisibility(View.VISIBLE);
                            }
                        }).scale(1f, 0.2f).alpha(1f, 0f).duration(150).onStop(new AnimationListener.Stop() {
                            @Override
                            public void onStop() {
                                mDragCallback.onInsert(endView, mItemStartPosition, mItemLastPosition, animTmp, false);
                                temiv.setImageBitmap(null);
                                removeView(temiv);
                            }
                        }).start();
                    }
                });
            } else {
                mDragCallback.onDrop(mDragView, mItemStartPosition, mItemLastPosition, tmp, out && fromType != FROMTYPE.FROM_HOME);
            }
        }
    }

    private boolean isOut(float mLastX, float mLastY) {
        if (mDragView == null) return false;
        RectF rectF = new RectF();
        rectF.set(mRecyclerView.getLeft(),
                mRecyclerView.getTop() - (mDragView.getHeight() / 2),
                mRecyclerView.getRight(),
                mRecyclerView.getBottom() + (mDragView.getHeight() / 2));
        return !rectF.contains(mLastX, mLastY);
    }

    private boolean isInSlide(float mLastX, float mLastY) {
        if (mRecyclerView == null) return false;
        RectF rectF = new RectF();
        rectF.set(mRecyclerView.getLeft(), mRecyclerView.getTop() - mDragView.getHeight(),
                mRecyclerView.getRight(), mRecyclerView.getBottom() + mDragView.getHeight());
        return rectF.contains(mLastX, mLastY);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = (mDragState >= DRAG_STATE_START) || super.onInterceptTouchEvent(ev);
        int action = ev.getAction();
        Log.i(TAG, "onInterceptTouchEvent, mDragState=" + mDragState + ", intercept=" + intercept + ", action=" + action);
        mIsTouch = !((action == MotionEvent.ACTION_UP) || (action == MotionEvent.ACTION_CANCEL));
        mLastX = ev.getX();
        mLastY = ev.getY();
        if (intercept && (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL)) {
            Log.e(TAG, "onInterceptTouchEvent, error handler");
            //if finger up too quickly, won't call onTouchEvent, so must handle up event here.
            doOnTouchEventUp();
        }
        return intercept;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Log.i(TAG, "onTouchEvent, mDragState=" + mDragState + ", action=" + action);
        if (mDragState < 0) {
            return false;
        }
        mIsTouch = !((action == MotionEvent.ACTION_UP) || (action == MotionEvent.ACTION_CANCEL));
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                mLastX = event.getX();
                mLastY = event.getY();
                if (xNestedScrollView != null) {
                    checkNestedScrollViewScroll(event.getRawY());
                } else {
                    checkRecyclerViewScroll(mLastY);
                }
                //  checkSlideAndMovePosition(mLastX, mLastY);
                if (mDragState == DRAG_STATE_START) {
                    mDragState = DRAG_STATE_DRAG;
                } else if (mDragState == DRAG_STATE_DRAG) {
                    float dx = mLastX - mLastPoint.x;
                    float dy = mLastY - mLastPoint.y;
                    mDrawRegion.offset(dx, dy);
                    postInvalidate();
                }
                mLastPoint.set(mLastX, mLastY);

                doOnTouchEventMove();

                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                doOnTouchEventUp();
                break;
            }

        }
        return true;//super.onTouchEvent(event);
    }

    private void doOnTouchEventUp() {
        onDrop();
        resetSlideState();
        endDrag();
    }

    private boolean onSwap = true;
    private InsertRunnable onInsert;
    private boolean prepared = false;

    private void doOnTouchEventMove() {

        if (mDragState == DRAG_STATE_IDLE) return;
        if (mDragCallback != null) {
            int tmp;
            boolean canInsert = false;
//            if (type == TYPE_HOME) {
                Object[] values = findInsertOrSwapPosition(mLastX, mLastY);
                tmp = (int) values[0];
                canInsert = (boolean) values[1];
//            } else {
//                tmp = findInsertPosition(mLastX, mLastY);
//            }

            if (!canInsert) {
                mHandler.removeCallbacks(onInsert);
                if (prepared) {
                    mDragCallback.unPrepareInsert();
                    doPreparedAnimation(false);
                    mHandler.removeMessages(UPDATE_SWAP_FLAG);
                    prepared = false;
                    onSwap = true;
                }
            }
//            if (fromType != FROMTYPE.FROM_PACKAGE) {//不是从文件夹进来的
            if (!isIn) {
                isIn = isInSlide(mLastX, mLastY);
            }
            boolean out = isOut(mLastX, mLastY);

            if (out && isIn) {
                if (!packed) {
                    updateHideFlag();
                    return;
                }
                mHandler.removeMessages(UPDATE_HIDE_FLAG);
                packed = false;
                mDragCallback.unPack(mDragView, mItemStartPosition, true);
                mHandler.removeMessages(UPDATE_SWAP_FLAG);

                onSwap = true;
                isIn = false;
                return;
            }
            //  }

            if (tmp == -1 || tmp == mItemStartPosition || mRecyclerViewScrollState != RECYCLER_VIEW_SCROLL_IDLE) {
                mHandler.removeMessages(UPDATE_SWAP_FLAG);
                return;
            }
            if (onSwap) {
                updateSwapFlag();
                return;
            }
            mHandler.removeMessages(UPDATE_SWAP_FLAG);
            onSwap = true;

            Log.i(TAG, "tmp=" + tmp + "  mItemStartPosition=" + mItemStartPosition + " onSwap=" + onSwap);
            boolean swaped = false;//是否交换成功
            if (canInsert && type == TYPE_HOME /*&& fromType != FROMTYPE.FROM_PACKAGE*/) {
                if (!prepared) {
                    //TODO:prepared可以改成Int类型
                    prepared = mDragCallback.prepareInsert(mItemStartPosition, tmp);
                    if (prepared) {
                        doPreparedAnimation(true);
                        mHandler.removeCallbacks(onInsert);
                        onInsert = new InsertRunnable(tmp);
                        mHandler.postDelayed(onInsert, INSET_AND_OPEN_PACK_TIME);
                    } else {
                        //交换
                        onSwap = true;
                        mHandler.removeCallbacks(onInsert);
                        swaped = mDragCallback.onSwap(mDragView, mItemStartPosition, mItemLastPosition, tmp, false);
                    }
                }


            } else {
                mHandler.removeCallbacks(onInsert);
                onSwap = true;
                swaped = mDragCallback.onSwap(mDragView, mItemStartPosition, mItemLastPosition, tmp, false);

            }
            if (swaped) {
                //交换成功才能更新位置
                mItemStartPosition = tmp;
            }
        }

    }

    private void doPreparedAnimation(boolean start) {
        if (start) {
            // mDragSnapShot = Utils.getViewSnapshot(mDragView, 0xFF, 0.8f);
        } else {
            mDragSnapShot = Utils.getViewSnapshot(mDragView, 0xFF, 0.9f);
        }
        postInvalidate();
    }

    class InsertRunnable implements Runnable {
        private int tmp;

        public InsertRunnable(int tmp) {
            this.tmp = tmp;
        }

        @Override
        public void run() {
            onSwap = true;
            boolean swaped = false;
            boolean inserted = mDragCallback.onInsert(mDragView, mItemStartPosition, mItemLastPosition, tmp, true);
            if (!inserted) {
                swaped = mDragCallback.onSwap(mDragView, mItemStartPosition, mItemLastPosition, tmp, false);
            }
            mDragCallback.unPrepareInsert();
            prepared = false;
            if (swaped) {
                mItemStartPosition = tmp;
            }
        }
    }

    private boolean canInsert(View view, float x, float y) {
        int centerX = view.getRight() - view.getWidth() / 2;
        int centerY = view.getBottom() - view.getHeight() / 2;
        int dx = (int) Math.abs(x - centerX);
        int dy = (int) Math.abs(y - centerY);
        if (dx <= INSERT_GAP && dy <= INSERT_GAP) {
            return true;
        }
        return false;
    }

    private void updateSwapFlag() {
        Message message = Message.obtain();
        message.what = UPDATE_SWAP_FLAG;
        message.obj = false;

        mHandler.sendMessageDelayed(message, SWAP_TIME);
    }

    private void updateHideFlag() {
        Message message = Message.obtain();
        message.what = UPDATE_HIDE_FLAG;
        message.obj = true;
        mHandler.removeMessages(UPDATE_HIDE_FLAG);
        mHandler.sendMessageDelayed(message, HIDE_TIME);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mDragState >= DRAG_STATE_START && mDragSnapShot != null && !mDragSnapShot.isRecycled()) {
            canvas.drawBitmap(mDragSnapShot, mDrawRegion.left, mDrawRegion.top, null);
        }
    }

    private static final int SLIDE_EDGE = 200;
    private static final int SLIDE_CRITICAL = 80;
    private static final int SLIDE_IDLE = 0;
    private static final int SLIDE_CAN_LEFT = 1;
    private static final int SLIDE_CAN_RIGHT = 2;
    private static final int SLIDE_WAIT_TO_AUTO_SLIDE_LEFT = 3;
    private static final int SLIDE_WAIT_TO_AUTO_SLIDE_RIGHT = 4;
    private int mSlideState = SLIDE_IDLE;
    private float mSlideX;
    private float mCurrentX = SLIDE_EDGE + SLIDE_CRITICAL;
    private float mLastX;
    private float mLastY;

    private void resetSlideState() {
        mSlideState = SLIDE_IDLE;
        mCurrentX = SLIDE_EDGE + SLIDE_CRITICAL;
        this.fromType = FROMTYPE.UNDEFINE;
        this.packed = false;
        this.isIn = false;
        if (prepared) {
            mDragCallback.unPrepareInsert();
            prepared = false;
        }
        mHandler.removeMessages(UPDATE_SWAP_FLAG);
        mHandler.removeMessages(UPDATE_INSERT_FLAG);
        mHandler.removeMessages(UPDATE_HIDE_FLAG);
        onSwap = true;
        mHandler.removeCallbacks(onInsert);
    }

    private void resetHandler() {
        mHandler.removeMessages(0);
        mHandler.removeMessages(RECYCLER_VIEW_SCROLL_DOWN);
        mHandler.removeMessages(RECYCLER_VIEW_SCROLL_UP);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    switch (mSlideState) {
                        case SLIDE_WAIT_TO_AUTO_SLIDE_LEFT: {
                            if (mSlideX - mCurrentX >= SLIDE_CRITICAL) {
                                // slide2Page(false);
                            }
                            break;
                        }
                        case SLIDE_WAIT_TO_AUTO_SLIDE_RIGHT: {
                            if (mCurrentX - mSlideX >= SLIDE_CRITICAL) {
                                //slide2Page(true);
                            }
                            break;
                        }
                    }

                    break;
                }

                case RECYCLER_VIEW_SCROLL_UP: {
                    if (autoScrollRecyclerView(20)) {
                        mHandler.sendEmptyMessageDelayed(RECYCLER_VIEW_SCROLL_UP, 10);
                    }
                    break;
                }
                case RECYCLER_VIEW_SCROLL_DOWN: {
                    if (autoScrollRecyclerView(-20)) {
                        mHandler.sendEmptyMessageDelayed(RECYCLER_VIEW_SCROLL_DOWN, 10);
                    }
                    break;
                }
                case UPDATE_SWAP_FLAG:
                    onSwap = (boolean) msg.obj;
                    break;
                case UPDATE_HIDE_FLAG:
                    packed = (boolean) msg.obj;
                    break;
                case UPDATE_INSERT_FLAG:
                    //     onInsert = (boolean) msg.obj;
                    break;
            }

        }
    };

    private Object[] findInsertOrSwapPosition(float x, float y) {
        y = y - mTopOffset;
//        if (type == TYPE_PACKAGE) {
//            x = x - ScreenUtils.dpToPx(20);
//        }
        int result = -1;
        boolean canInsert = false;
        Object[] valus = new Object[]{result, canInsert};
        RecyclerView recyclerView = findCurrentRecyclerView();
        if (recyclerView != null) {
            int[] pos = getRvFirstAndEndPos();
            int first = pos[0];
            int last = pos[1];
            for (int i = first; i <= last; i++) {
                View child = findViewByPosition(i);
                Log.i("mtest", "x=" + x + "  y=" + y + "index=" + i + " left=" + child.getLeft() + " right=" + child.getRight() + "  top=" + child.getTop() + "  bottom=" + child.getBottom());
                final float translationX = child.getTranslationX();
                final float translationY = child.getTranslationY();
                if (x >= child.getLeft() + SWAP_EDGE + translationX
                        && x <= child.getRight() - SWAP_EDGE + translationX
                        && y >= child.getTop() + SWAP_EDGE + translationY
                        && y <= child.getBottom() - SWAP_EDGE + translationY) {
                    valus[0] = i;
                    valus[1] = canInsert(child, x, y);
                    return valus;
                }
            }
        }
        return valus;
    }

    private int SWAP_EDGE = 0;


    private RecyclerView findCurrentRecyclerView() {
        return mRecyclerView;
    }

    public void bindNestedScrollView(XNestedScrollView xNestedScrollView) {
        this.xNestedScrollView = xNestedScrollView;
    }

    private XNestedScrollView findNestedScrollView() {
        return xNestedScrollView;
    }

    @Override
    protected void onDetachedFromWindow() {
        resetSlideState();
        resetHandler();
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        super.onDetachedFromWindow();
    }

    private ValueAnimator mAnimator;

    private void doAnimationOnDrop() {
        View view = findDropView();
        if (view != null) {
            PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("X", mDrawRegion.left, view.getLeft());
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("Y", mDrawRegion.top, view.getTop() + mTopOffset);
            mAnimator = ObjectAnimator.ofPropertyValuesHolder(holder1, holder2);

            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float x = (Float) animation.getAnimatedValue("X");
                    Float y = (Float) animation.getAnimatedValue("Y");
                    float dx = x - mDrawRegion.left;
                    float dy = y - mDrawRegion.top;
                    mDrawRegion.offset(dx, dy);
                    postInvalidate();
                }

            });

            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mAnimator = null;
                    mDragState = DRAG_STATE_IDLE;
                    postInvalidate();
                }
            });

            mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mAnimator.setDuration(200);
            mAnimator.start();
        } else {
            mDragState = DRAG_STATE_IDLE;
            postInvalidate();
        }
    }

    private View findDropView() {
        RecyclerView recyclerView = findCurrentRecyclerView();
        if (recyclerView != null) {
            int[] pos = getRvFirstAndEndPos();
            int first = pos[0];
            int last = pos[1];
            if (mItemLastPosition >= first && mItemLastPosition <= last) {
                return findViewByPosition(mItemLastPosition);
            }
        }
        return null;
    }

    private static final int RECYCLER_VIEW_AUTO_SCROLL_EDGE = 80;
    private static final int RECYCLER_VIEW_SCROLL_IDLE = 0;
    private static final int RECYCLER_VIEW_SCROLL_UP = 1;
    private static final int RECYCLER_VIEW_SCROLL_DOWN = 2;
    private static final int UPDATE_SWAP_FLAG = 3;
    private static final int UPDATE_INSERT_FLAG = 4;
    private static final int UPDATE_HIDE_FLAG = 5;

    private int mRecyclerViewScrollState = RECYCLER_VIEW_SCROLL_IDLE;

    private void checkRecyclerViewScroll(float y) {
        if (y < mTopOffset + RECYCLER_VIEW_AUTO_SCROLL_EDGE) {
            if (mRecyclerViewScrollState != RECYCLER_VIEW_SCROLL_DOWN) {
                mRecyclerViewScrollState = RECYCLER_VIEW_SCROLL_DOWN;
                mHandler.sendEmptyMessage(RECYCLER_VIEW_SCROLL_DOWN);
            }
        } else if (y > getHeight() - mBottomOffset - RECYCLER_VIEW_AUTO_SCROLL_EDGE) {
            if (mRecyclerViewScrollState != RECYCLER_VIEW_SCROLL_UP) {
                mRecyclerViewScrollState = RECYCLER_VIEW_SCROLL_UP;
                mHandler.sendEmptyMessage(RECYCLER_VIEW_SCROLL_UP);
            }
        } else {
            removeScrollMessages();
        }
    }

    //首页ui特殊结构，自定义，其它页面的NestedScrollView不适用
    private void checkNestedScrollViewScroll(float rawY) {
        RecyclerView recyclerView = findCurrentRecyclerView();
        int[] location = new int[2];
        recyclerView.getLocationOnScreen(location);
        if (rawY < 80) {
            if (location[1] <= 300) {
                if (mRecyclerViewScrollState != RECYCLER_VIEW_SCROLL_DOWN) {
                    mRecyclerViewScrollState = RECYCLER_VIEW_SCROLL_DOWN;
                    mHandler.sendEmptyMessage(RECYCLER_VIEW_SCROLL_DOWN);
                }
            } else {
                removeScrollMessages();
            }

        } else {
            if (rawY > mScreenHeight - 80) {
                if (mRecyclerViewScrollState != RECYCLER_VIEW_SCROLL_UP) {
                    mRecyclerViewScrollState = RECYCLER_VIEW_SCROLL_UP;
                    mHandler.sendEmptyMessage(RECYCLER_VIEW_SCROLL_UP);
                }
            } else {
                removeScrollMessages();
            }
        }
    }

    private void removeScrollMessages() {
        if (mRecyclerViewScrollState != RECYCLER_VIEW_SCROLL_IDLE) {
            mRecyclerViewScrollState = RECYCLER_VIEW_SCROLL_IDLE;
            mHandler.removeMessages(RECYCLER_VIEW_SCROLL_UP);
            mHandler.removeMessages(RECYCLER_VIEW_SCROLL_DOWN);
        }
    }

    private boolean autoScrollRecyclerView(int dy) {
        if (isOut(mLastX, mLastY)) {
            return false;
        }

        RecyclerView recyclerView = findCurrentRecyclerView();
        if (recyclerView != null) {
            int[] pos = getRvFirstAndEndPos();
            int first = pos[0];
            int last = pos[1];
            int count = recyclerView.getAdapter().getItemCount();
            if (recyclerView.getLayoutManager().canScrollVertically()) {
                if ((first >= 0 && dy < 0)
                        || (last <= count + 1 && dy > 0)) {
                    Log.i(TAG, "dragLayout  autoScrollRecyclerView===>last=" + last + "   count=" + count + "  dy=" + dy);
                    XNestedScrollView xNestedScrollView = findNestedScrollView();
                    if (xNestedScrollView != null) {
                        if (xNestedScrollView.canScrollVertically(dy)) {
                            int[] location = new int[2];
                            recyclerView.getLocationOnScreen(location);
                            if (dy < 0 && location[1] > 300) {
                                //上滑到常用设备在屏幕顶部就停止
                                return false;
                            }
                            try {
                                xNestedScrollView.scrollBy(0, dy);
                            } catch (Exception e) {
                                return false;
                            }
                        }
                    } else {
                        if (recyclerView.canScrollVertically(dy)) {
                            try {
                                recyclerView.scrollBy(0, dy);
                            } catch (Exception e) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return true;
    }

    public void updateRecyclerView(RecyclerView currentRv, int newStartPost, FROMTYPE fromType) {
        Log.i(TAG, "updateRecyclerView==========> newStartPost=" + newStartPost + " fromType=" + fromType + "  type=" + type);
        mRecyclerView = currentRv;
        if (newStartPost < 0) {
            return;
        }
        packed = false;
        isIn = false;
        mItemStartPosition = newStartPost;
        mItemLastPosition = newStartPost;
        float offsetX = 0;
        float offsetY = 0;
        View childView = mRecyclerView.getLayoutManager().findViewByPosition(newStartPost);
        if (childView == null || mDragView == null) return;
        mDragView.setVisibility(VISIBLE);
        mDragView = childView;
        ViewParent vp = childView.getParent();
        while (vp instanceof View && vp != this) {
            if (!(vp.getParent() instanceof MyRelativeLayout)) {
                offsetX += ((View) vp).getX();
                offsetY += ((View) vp).getY();
            }
            vp = vp.getParent();
        }
        this.fromType = fromType;
        mTopOffset = offsetY;

        if (mDragSnapShot != null) {
            mDragSnapShot.recycle();
        }
        if (type == TYPE_HOME) {
            mDragSnapShot = Utils.getViewSnapshot(childView, 0xFF, scale);
        } else {
//            mTopOffset = ScreenUtil.dip2px(getContext(), PACK_VIEW_TOP_OFFSET);//TODO:暂时这么写
            mDragSnapShot = Utils.getViewSnapshot(childView, 0xFF);
        }
        mDragSnapShotEdge = Utils.getViewSnapshot(childView, 0xFF);
        int dGap = (fromType == FROMTYPE.FROM_HOME ? 100 : 0);

        mDrawRegionLeft = mLastX - childView.getWidth() / 2;
        mDrawRegionRight = mLastX + childView.getWidth() / 2;
        mDrawRegionTop = mLastY - childView.getHeight() / 2 - dGap;
        mDrawRegionBottom = mLastY + childView.getHeight() / 2 - dGap;
        mDrawRegion.set(mDrawRegionLeft, mDrawRegionTop, mDrawRegionRight, mDrawRegionBottom);

        childView.setVisibility(INVISIBLE);
        childView.clearAnimation();
        postInvalidate();

    }


    public void updateType(int type) {
        this.type = type;
    }


    //获取RecyclerView第一个和最后一个view索引
    private int[] getRvFirstAndEndPos() {
        int firstPos = 0;
        int endPos = 0;
        int childViewCount = 0;
        if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager glm = (StaggeredGridLayoutManager) mRecyclerView.getLayoutManager();
            firstPos = glm.findFirstVisibleItemPositions(null)[0];
            endPos = glm.findLastVisibleItemPositions(null)[0];
            childViewCount = glm.getChildCount();
        } else if (mRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager glm = (GridLayoutManager) mRecyclerView.getLayoutManager();
            firstPos = glm.findFirstVisibleItemPosition();
            endPos = glm.findLastVisibleItemPosition();
            childViewCount = glm.getChildCount();
        }
        return new int[]{firstPos, endPos, childViewCount};
    }

    private View findViewByPosition(int pos) {
        View childView = null;
        if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager glm = (StaggeredGridLayoutManager) mRecyclerView.getLayoutManager();
            childView = glm.findViewByPosition(pos);
        } else if (mRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager glm = (GridLayoutManager) mRecyclerView.getLayoutManager();
            childView = glm.findViewByPosition(pos);
        }
        return childView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mScreenWidth, mScreenHeight);
    }
}
