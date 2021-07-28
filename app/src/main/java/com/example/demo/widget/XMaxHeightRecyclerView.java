package com.example.demo.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.example.demo.R;

public class XMaxHeightRecyclerView extends RecyclerView {
    private int mMaxHeight;
    private int mScreenHeight;
    private int mScreenWidth;

    public XMaxHeightRecyclerView(Context context) {
        this(context,null);
    }

    public XMaxHeightRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XMaxHeightRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOverScrollMode(OVER_SCROLL_NEVER);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        Point outSize = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getRealSize(outSize);
        mScreenWidth = outSize.x;
        mScreenHeight = outSize.y;

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightRecyclerView);
        mMaxHeight = arr.getLayoutDimension(R.styleable.MaxHeightRecyclerView_maxHeight, mMaxHeight);
        arr.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mScreenWidth, mScreenHeight);
    }
}
