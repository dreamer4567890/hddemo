package com.example.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class RemoteControl1 extends View {


    private Paint mPaint;

    private Paint mClickPaint;
    private Paint mClickArrowPaint;
    private Paint mDefaultArrowPaint;

    private Paint mCenterCirclePaint;
    private Paint mBgCirclePaint;

    private RectF mRectFBig;

    private RectF mRectFLittle;

    private Path mPathLeft;
    private Path mPathTop;
    private Path mPathRight;
    private Path mPathBottom;
    private Path mPathCenter;

    private float mInitSweepAngle = 0;
    private float mBigSweepAngle = 90;
    private float mLittleSweepAngle = 90;

    private float mBigMarginAngle;
    private float mLittleMarginAngle;

    private List<Region> mList;

    private Region mAllRegion;

    private Region mRegionTop;
    private Region mRegionRight;
    private Region mRegionLeft;
    private Region mRegionBottom;
    private Region mRegionCenter;

    private int mRadius;
    private int mBgRadius;

    private static final int LEFT = 0;
    private static final int TOP = 1;
    private static final int RIGHT = 2;
    private static final int BOTTOM = 3;
    private static final int CENTER = 4;

    private int mClickFlag = -1;

    private int mWidth;
    private float mStokeWidth = 3.0f;

    private int mTriangleVHHeight = 7;//三角形水平或者垂直高度
    private int mTrianglePoint;

    private int mCurX, mCurY;

    private RegionViewClickListener mListener;

    public RemoteControl1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public RemoteControl1(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RemoteControl1(Context context) {
        super(context);
        initView();
    }

    public void setListener(RegionViewClickListener mListener) {
        this.mListener = mListener;
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#FFFFFFFF")); //原始颜色

        mClickPaint = new Paint(mPaint);
        mClickPaint.setColor(Color.parseColor("#FFEEEEEE")); //点击后颜色

        mClickArrowPaint = new Paint(mPaint);
        mClickArrowPaint.setColor(Color.parseColor("#E1B96E"));//黄色三角形

        mDefaultArrowPaint = new Paint(mPaint);//黑色三角形
        mDefaultArrowPaint.setColor(Color.parseColor("#75787a"));

        mCenterCirclePaint = new Paint(mPaint);//中间空心圆
        mCenterCirclePaint.setColor(Color.parseColor("#dbdbdb"));
        mCenterCirclePaint.setAntiAlias(true); //消除锯齿
        mCenterCirclePaint.setStrokeWidth(mStokeWidth);
        mCenterCirclePaint.setStyle(Paint.Style.STROKE);  //绘制空心圆

        mBgCirclePaint = new Paint(mPaint);//背景圆
        mBgCirclePaint.setColor(Color.parseColor("#FFF0F0F0"));
        mBgCirclePaint.setAntiAlias(true); //消除锯齿
        mBgCirclePaint.setStrokeWidth(mStokeWidth);
        mBgCirclePaint.setStyle(Paint.Style.STROKE);  //绘制实心圆

        mPathLeft = new Path();
        mPathTop = new Path();
        mPathRight = new Path();
        mPathBottom = new Path();
        mPathCenter = new Path();

        mList = new ArrayList<>();

        mRegionLeft = new Region();
        mRegionTop = new Region();
        mRegionRight = new Region();
        mRegionBottom = new Region();
        mRegionCenter = new Region();

        mBigMarginAngle = 90 - mBigSweepAngle;
        mLittleMarginAngle = 90 - mLittleSweepAngle;
    }

    private void initPath() {
        mList.clear();
        // 初始化right路径
        mPathRight.addArc(mRectFBig, mInitSweepAngle - mBigSweepAngle / 2,
                mBigSweepAngle);
        mPathRight.arcTo(mRectFLittle, mInitSweepAngle + mLittleSweepAngle / 2,
                -mLittleSweepAngle);
        mPathRight.close();

        // 计算right的区域
        mRegionRight.setPath(mPathRight, mAllRegion);
        mList.add(mRegionRight);

        // 初始化bottom路径
        mPathBottom.addArc(mRectFBig, mInitSweepAngle - mBigSweepAngle / 2
                + mBigMarginAngle + mBigSweepAngle, mBigSweepAngle);
        mPathBottom.arcTo(mRectFLittle, mInitSweepAngle + mLittleSweepAngle / 2
                + mLittleMarginAngle + mLittleSweepAngle, -mLittleSweepAngle);
        mPathBottom.close();

        // 计算bottom的区域
        mRegionBottom.setPath(mPathBottom, mAllRegion);
        mList.add(mRegionBottom);

        // 初始化left路径
        mPathLeft.addArc(mRectFBig, mInitSweepAngle - mBigSweepAngle / 2 + 2
                * (mBigMarginAngle + mBigSweepAngle), mBigSweepAngle);
        mPathLeft.arcTo(mRectFLittle, mInitSweepAngle + mLittleSweepAngle / 2
                        + 2 * (mLittleMarginAngle + mLittleSweepAngle),
                -mLittleSweepAngle);
        mPathLeft.close();

        // 计算left的区域
        mRegionLeft.setPath(mPathLeft, mAllRegion);
        mList.add(mRegionLeft);

        // 初始化top路径
        mPathTop.addArc(mRectFBig, mInitSweepAngle - mBigSweepAngle / 2 + 3
                * (mBigMarginAngle + mBigSweepAngle), mBigSweepAngle);
        mPathTop.arcTo(mRectFLittle, mInitSweepAngle + mLittleSweepAngle / 2
                        + 3 * (mLittleMarginAngle + mLittleSweepAngle),
                -mLittleSweepAngle);
        mPathTop.close();

        // 计算top的区域
        mRegionTop.setPath(mPathTop, mAllRegion);
        mList.add(mRegionTop);

        // 初始化center路径
//        mPathCenter.addCircle(0, 0, mRadius, Path.Direction.CW);
//        mPathCenter.close();

        // 计算center的区域
//        mRegionCenter.setPath(mPathCenter, mAllRegion);
//        mList.add(mRegionCenter);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //   mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);

        //canvas.drawCircle(0, 0, mRadius, mCenterCirclePaint);

        canvas.drawPath(mPathRight, mPaint);
        canvas.drawPath(mPathBottom, mPaint);
        canvas.drawPath(mPathLeft, mPaint);
        canvas.drawPath(mPathTop, mPaint);
        canvas.drawPath(mPathCenter, mPaint);

        //画黑色三角形
        drawBottomTriangle(canvas, mDefaultArrowPaint);
        drawLeftTriangle(canvas, mDefaultArrowPaint);
        drawTopTriangle(canvas, mDefaultArrowPaint);
        drawRightTriangle(canvas, mDefaultArrowPaint);

        switch (mClickFlag) {
            case RIGHT:
                canvas.drawPath(mPathRight, mClickPaint);
                drawRightTriangle(canvas, mClickArrowPaint);
                break;
            case BOTTOM:
                canvas.drawPath(mPathBottom, mClickPaint);
                drawBottomTriangle(canvas, mClickArrowPaint);
                break;
            case LEFT:
                canvas.drawPath(mPathLeft, mClickPaint);
                drawLeftTriangle(canvas, mClickArrowPaint);
                break;
            case TOP:
                canvas.drawPath(mPathTop, mClickPaint);
                drawTopTriangle(canvas, mClickArrowPaint);
                break;
            case CENTER:
                canvas.drawPath(mPathCenter, mClickPaint);
                break;
            default:
                break;
        }

        //canvas.drawCircle(0, 0, mBgRadius - mStokeWidth, mBgCirclePaint);
        canvas.rotate(45);
        for (int i = 0; i < 4; i++) {
            canvas.drawLine(0.5F * mRectFLittle.width(), 0, mBgRadius * 13 / 15, 0, mBgCirclePaint);
            canvas.rotate(90);
        }
        canvas.restore();
    }

    //画右侧三角形
    private void drawRightTriangle(Canvas canvas, Paint paint) {
        final int vhSize = dip2px(getContext(), mTriangleVHHeight);
        final int vhSize1 = dip2px(getContext(), mTriangleVHHeight / 7 * 5);

        int x1 = mTrianglePoint - vhSize;
        int y1 = -vhSize1;
        int x2 = mTrianglePoint - vhSize;
        int y2 = vhSize1;
        int x3 = mTrianglePoint;
        int y3 = 0;
        Path path2 = new Path();
        path2.moveTo(x1, y1);
        path2.lineTo(x2, y2);
        path2.lineTo(x3, y3);
        path2.close();
        canvas.drawPath(path2, paint);
    }

    //画底部三角形
    private void drawBottomTriangle(Canvas canvas, Paint paint) {
        final int vhSize = dip2px(getContext(), mTriangleVHHeight);
        final int vhSize1 = dip2px(getContext(), mTriangleVHHeight / 7 * 5);

        int xB1 = 0;
        int yB1 = mTrianglePoint;
        int xB2 = vhSize1;
        int yB2 = mTrianglePoint - vhSize;
        int xB3 = -vhSize1;
        int yB3 = mTrianglePoint - vhSize;
        Path pathB = new Path();
        pathB.moveTo(xB1, yB1);
        pathB.lineTo(xB2, yB2);
        pathB.lineTo(xB3, yB3);
        canvas.drawPath(pathB, paint);
    }

    //画左侧三角形
    private void drawLeftTriangle(Canvas canvas, Paint paint) {
        final int vhSize = dip2px(getContext(), mTriangleVHHeight);
        final int vhSize1 = dip2px(getContext(), mTriangleVHHeight / 7 * 5);

        int xL1 = vhSize - mTrianglePoint;
        int yL1 = -vhSize1;
        int xL2 = vhSize - mTrianglePoint;
        int yL2 = vhSize1;
        int xL3 = -mTrianglePoint;
        int yL3 = 0;
        Path pathL = new Path();
        pathL.moveTo(xL1, yL1);
        pathL.lineTo(xL2, yL2);
        pathL.lineTo(xL3, yL3);
        canvas.drawPath(pathL, paint);
    }

    //画顶部三角形
    private void drawTopTriangle(Canvas canvas, Paint paint) {
        final int vhSize = dip2px(getContext(), mTriangleVHHeight);
        final int vhSize1 = dip2px(getContext(), mTriangleVHHeight / 7 * 5);

        int xT1 = 0;
        int yT1 = -mTrianglePoint;
        int xT2 = -vhSize1;
        int yT2 = vhSize - mTrianglePoint;
        int xT3 = vhSize1;
        int yT3 = vhSize - mTrianglePoint;
        Path pathT = new Path();
        pathT.moveTo(xT1, yT1);
        pathT.lineTo(xT2, yT2);
        pathT.lineTo(xT3, yT3);
        canvas.drawPath(pathT, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = Math.min(w,h);
        mAllRegion = new Region(-mWidth, -mWidth, mWidth, mWidth);

        mRectFBig = new RectF(-mWidth / 2, -mWidth / 2, mWidth / 2, mWidth / 2);

        mRectFLittle = new RectF(-mWidth / 10, -mWidth / 10, mWidth / 10,
                mWidth / 10);

        mRadius = mWidth / 6;

        mBgRadius = mWidth / 2;

        mTrianglePoint = mBgRadius * 4 / 5;

        initPath();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 减去移除 的位置
        mCurX = (int) event.getX() - getMeasuredWidth() / 2;
        mCurY = (int) event.getY() - getMeasuredHeight() / 2;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                containRect(mCurX, mCurY);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mClickFlag != -1) {
                    containRect(mCurX, mCurY);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (mClickFlag != -1) {
                    switch (mClickFlag) {
                        case RIGHT:
                            if (mListener != null) {
                                mListener.clickRight();
                            }
                            break;
                        case BOTTOM:
                            if (mListener != null) {
                                mListener.clickBottom();
                            }
                            break;
                        case LEFT:
                            if (mListener != null) {
                                mListener.clickLeft();
                            }
                            break;
                        case TOP:
                            if (mListener != null) {
                                mListener.clickTop();
                            }
                            break;
                        case CENTER:
                            if (mListener != null) {
                                mListener.clickCenter();
                            }
                            break;
                    }

                    mClickFlag = -1;
                }

                invalidate();
                break;
            default:
                break;
        }

        return true;
    }

    public void containRect(int x, int y) {
        int index = -1;
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).contains(x, y)) {
                mClickFlag = switchRect(i);
                index = i;
                break;
            }
        }

        if (index == -1) {
            mClickFlag = -1;
        }
    }

    public int switchRect(int i) {
        switch (i) {
            case 0:
                return RIGHT;
            case 1:
                return BOTTOM;
            case 2:
                return LEFT;
            case 3:
                return TOP;
            case 4:
                return CENTER;
            default:
                return -1;
        }
    }

    public interface RegionViewClickListener {
        void clickLeft();

        void clickTop();

        void clickRight();

        void clickBottom();

        void clickCenter();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
