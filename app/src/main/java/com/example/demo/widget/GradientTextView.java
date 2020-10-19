package com.example.demo.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.demo.R;

/**
 * Created by wuzhen
 * on 2020/3/17 11:15 AM
 * 渐变色TextView
 * 注意：渐变是根据TextView的宽度，所以 layout_width="wrap_content"
 */

@SuppressLint("AppCompatCustomView")
public class GradientTextView extends TextView {
    private boolean isGradient;
    private int startColor;
    private int endColor;
    private int defaultColor;
    private boolean mIsChangePath; //是否改变渐变方向

    public GradientTextView(Context context) {
        this(context, null);
    }

    public GradientTextView(Context context, boolean isChangePath) {
        this(context, null);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView);
        setStartColor(array.getColor(R.styleable.GradientTextView_startColor, getResources().
                getColor(R.color.color_CAA381, null)));
        setEndColor(array.getColor(R.styleable.GradientTextView_endColor, getResources().
                getColor(R.color.color_FFEBD7, null)));
        setDefaultColor(array.getColor(R.styleable.GradientTextView_defaultColor, getResources().
                getColor(R.color.common_text_white_gray, null)));
        isGradient = array.getBoolean(R.styleable.GradientTextView_isGradient, true);
        setChangePath(array.getBoolean(R.styleable.GradientTextView_isChangePath, false));
        array.recycle();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isGradient) {
            LinearGradient gradient;
            if (mIsChangePath) {
                gradient = new LinearGradient(getWidth(), getHeight(), 0, 0,
                        startColor, endColor, Shader.TileMode.CLAMP);
            } else {
                gradient = new LinearGradient(0, 0, getWidth(), getHeight(),
                        startColor, endColor, Shader.TileMode.CLAMP);
            }
            getPaint().setShader(gradient);
        } else {
            getPaint().setShader(new LinearGradient(
                    0, 0, getWidth(), 0,
                    defaultColor,
                    defaultColor,
                    Shader.TileMode.CLAMP));
        }
    }

    /**
     * 单个TextView还需调用invalidate()
     *
     * @param isGradient
     */
    public void setIsGradient(boolean isGradient) {
        this.isGradient = isGradient;
        requestLayout();
    }

    public void setStartColor(int startColor) {
        if (startColor != 0) {
            this.startColor = startColor;
        }
        requestLayout();
    }

    public void setEndColor(int endColor) {
        if (endColor != 0) {
            this.endColor = endColor;
        }
        requestLayout();
    }

    public void setDefaultColor(int defaultColor) {
        if (defaultColor != 0) {
            this.defaultColor = defaultColor;
        }
        requestLayout();
    }

    public void setStartAndEndColor(int startColor, int endColor) {
        if (startColor != 0 && endColor != 0) {
            this.startColor = startColor;
            this.endColor = endColor;
        }
        requestLayout();
    }

    public void setChangePath(boolean isChangePath) {
        this.mIsChangePath = isChangePath;
        requestLayout();
    }

}
