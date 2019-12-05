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

/**
 * 遥控器控件
 * 不规则点击区域使用图片不方便实现
 * 注：背景圆不需要画
 * Created by ch on 2017/7/13 0013.
 */

public class RemoteControl extends View {

    private Paint mPaint;

    private Paint mClickPaint;
    private Paint mClickArrowPaint;
    private Paint mDefaultArrowPaint;

    private Paint mCenterCirclePaint;
    private Paint mBgCirclePaint;
    private Paint mLittleCirclePaint;

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

    private int mCircleVHHeight = 10;//方向圆直径
    private int mCirclePoint;

    private int mCurX, mCurY;

    private RegionViewClickListener mListener;

    public RemoteControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public RemoteControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RemoteControl(Context context) {
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
        mPaint.setColor(Color.parseColor("#14000000")); //原始颜色

        mClickPaint = new Paint();
        mClickPaint.setColor(Color.parseColor("#FFEEEEEE")); //点击后颜色

        mClickArrowPaint = new Paint(mPaint);
        mClickArrowPaint.setColor(Color.parseColor("#7FFFFFFF"));//方向圆点击

        mDefaultArrowPaint = new Paint(mPaint);//方向圆
        mDefaultArrowPaint.setColor(Color.parseColor("#7FFFFFFF"));

        mCenterCirclePaint = new Paint();//中间空心圆
        mCenterCirclePaint.setColor(Color.parseColor("#FFFFFFFF"));
        mCenterCirclePaint.setAntiAlias(true); //消除锯齿
        mCenterCirclePaint.setStrokeWidth(mStokeWidth);
        mCenterCirclePaint.setStyle(Paint.Style.FILL);  //绘制空心圆

        mBgCirclePaint = new Paint(mPaint);//背景圆
        mBgCirclePaint.setColor(Color.parseColor("#14000000"));
        mBgCirclePaint.setAntiAlias(true); //消除锯齿
        mBgCirclePaint.setStrokeWidth(mStokeWidth);
        mBgCirclePaint.setStyle(Paint.Style.STROKE);  //绘制实心圆

        mLittleCirclePaint = new Paint(mPaint);//方向圆
        mLittleCirclePaint.setColor(Color.parseColor("#FFFFFFFF"));
        mLittleCirclePaint.setAntiAlias(true); //消除锯齿
        mLittleCirclePaint.setStyle(Paint.Style.FILL);  //绘制空心圆

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
        drawBottomCircle(canvas, mLittleCirclePaint);
        drawLeftCircle(canvas, mLittleCirclePaint);
        drawTopCircle(canvas, mLittleCirclePaint);
        drawRightCircle(canvas, mLittleCirclePaint);

        switch (mClickFlag) {
            case RIGHT:
                canvas.drawPath(mPathRight, mClickPaint);
                drawRightCircle(canvas, mClickArrowPaint);
                break;
            case BOTTOM:
                canvas.drawPath(mPathBottom, mClickPaint);
                drawBottomCircle(canvas, mClickArrowPaint);
                break;
            case LEFT:
                canvas.drawPath(mPathLeft, mClickPaint);
                drawLeftCircle(canvas, mClickArrowPaint);
                break;
            case TOP:
                canvas.drawPath(mPathTop, mClickPaint);
                drawTopCircle(canvas, mClickArrowPaint);
                break;
            case CENTER:
                canvas.drawPath(mPathCenter, mClickPaint);
                break;
            default:
                //BHLog.e("RemoteC", "onDraw default");
                break;
        }

        //canvas.drawCircle(0, 0, mBgRadius - mStokeWidth, mBgCirclePaint);
        /*canvas.rotate(45);
        for (int i = 0; i < 4; i++) {
            canvas.drawLine(0.5F * mRectFLittle.width(), 0, mBgRadius, 0, mBgCirclePaint);
            canvas.rotate(90);
        }
        canvas.restore();*/
    }

    //画右侧圆
    private void drawRightCircle(Canvas canvas, Paint paint) {
        final int vhSize = dip2px(getContext(), mCircleVHHeight);

        /*int x1 = mTrianglePoint - vhSize;
        int y1 = -vhSize;
        int x2 = mTrianglePoint - vhSize;
        int y2 = vhSize;
        int x3 = mTrianglePoint;
        int y3 = 0;
        Path path2 = new Path();
        path2.moveTo(x1, y1);
        path2.lineTo(x2, y2);
        path2.lineTo(x3, y3);
        path2.close();
        canvas.drawPath(path2, paint);*/

        canvas.drawCircle(mCirclePoint - vhSize / 2, 0, vhSize / 2, paint);
    }

    //画底部圆
    private void drawBottomCircle(Canvas canvas, Paint paint) {
        final int vhSize = dip2px(getContext(), mCircleVHHeight);

        /*int xB1 = 0;
        int yB1 = mTrianglePoint;
        int xB2 = vhSize;
        int yB2 = mTrianglePoint - vhSize;
        int xB3 = -vhSize;
        int yB3 = mTrianglePoint - vhSize;
        Path pathB = new Path();
        pathB.moveTo(xB1, yB1);
        pathB.lineTo(xB2, yB2);
        pathB.lineTo(xB3, yB3);
        canvas.drawPath(pathB, paint);*/

        canvas.drawCircle(0,  mCirclePoint - vhSize / 2, vhSize / 2, paint);
    }

    //画左侧圆
    private void drawLeftCircle(Canvas canvas, Paint paint) {
        final int vhSize = dip2px(getContext(), mCircleVHHeight);

        /*int xL1 = vhSize - mTrianglePoint;
        int yL1 = -vhSize;
        int xL2 = vhSize - mTrianglePoint;
        int yL2 = vhSize;
        int xL3 = -mTrianglePoint;
        int yL3 = 0;
        Path pathL = new Path();
        pathL.moveTo(xL1, yL1);
        pathL.lineTo(xL2, yL2);
        pathL.lineTo(xL3, yL3);
        canvas.drawPath(pathL, paint);*/

        canvas.drawCircle(vhSize / 2 - mCirclePoint, 0, vhSize / 2, paint);
    }

    //画顶部圆
    private void drawTopCircle(Canvas canvas, Paint paint) {
        final int vhSize = dip2px(getContext(), mCircleVHHeight);

        /*int xT1 = 0;
        int yT1 = -mTrianglePoint;
        int xT2 = -vhSize;
        int yT2 = vhSize - mTrianglePoint;
        int xT3 = vhSize;
        int yT3 = vhSize - mTrianglePoint;
        Path pathT = new Path();
        pathT.moveTo(xT1, yT1);
        pathT.lineTo(xT2, yT2);
        pathT.lineTo(xT3, yT3);
        canvas.drawPath(pathT, paint);*/

        canvas.drawCircle(0, vhSize / 2 - mCirclePoint, vhSize / 2, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = Math.min(w,h);
        mAllRegion = new Region(-mWidth, -mWidth, mWidth, mWidth);

        mRectFBig = new RectF(-mWidth / 2, -mWidth / 2, mWidth / 2, mWidth / 2);

        mRectFLittle = new RectF(-mWidth / 4, -mWidth / 4, mWidth / 4,
                mWidth / 4);

        mRadius = mWidth / 6;

        mBgRadius = mWidth / 2;

        mCirclePoint = mBgRadius * 4 / 5;

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