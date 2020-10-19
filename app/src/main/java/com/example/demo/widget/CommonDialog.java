package com.example.demo.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.demo.R;

/**
 * Created by Weng on 2017/3/10.
 */

public class CommonDialog extends BaseDialog implements View.OnClickListener {
    protected MyBlurView mBlurView;
    public final static String TAG = "CommonDialog";

    private TextView mTitle, mMessage;
    private GradientTextView mEnsure, mCancel;
    private LinearLayout mCommonLayout;
    private ViewGroup mContentRoot;
    private Info mInfo = new Info();
    private OnClickListener mOnClickListener;
    private DIALOG_STATE state = DIALOG_STATE.NORMAL;
    private int innerIds = -1;
    private View innerView = null;
    private boolean enable = true;
    private boolean isInited = false;
    private TextView tvErrorTips;
    private int mLayoutResID = -1;

    public enum DIALOG_STATE {
        NORMAL,
        GUIDE,
        ALERT
    }

    public CommonDialog(Context context) {
        super(context, R.style.MyAlertDialogTheme);
    }

    public CommonDialog setDialogTitle(String title) {
        mInfo.mTitle = title;
        return this;
    }

    public CommonDialog setDialogTitle(int titleId) {
        mInfo.mTitle = getContext().getString(titleId);
        return this;
    }

    public CommonDialog setDialogMessage(String msg) {
        mInfo.mMessage = msg;
        return this;
    }

    public CommonDialog setDialogMessage(int msgId) {
        mInfo.mMessage = getContext().getString(msgId);
        return this;
    }

    public CommonDialog setDialogEnsure(String ensure) {
        mInfo.mEnsure = ensure;
        if (mEnsure != null) {
            mEnsure.setText(ensure);
        }
        return this;
    }

    public CommonDialog setDialogEnsureAble(boolean ensureAble) {
        if (mEnsure != null) {
            mEnsure.setEnabled(ensureAble);
        }
        return this;
    }

    public CommonDialog setDialogEnsure(int ensureId) {
        mInfo.mEnsure = getContext().getString(ensureId);
        return this;
    }

    public CommonDialog setBlur(boolean blur) {
        mInfo.blur = blur;
        return this;
    }

    public CommonDialog setBgResId(int resId) {
        mInfo.bgResId = resId;
        return this;
    }


    public CommonDialog setDialogCancel(String cancel) {
        mInfo.mCancel = cancel;
        return this;
    }

    public CommonDialog setDialogCancel(int cancelId) {
        mInfo.mCancel = getContext().getString(cancelId);
        return this;
    }

    public CommonDialog setDialogListener(OnClickListener listener) {
        mOnClickListener = listener;
        return this;
    }

    public CommonDialog setDialogState(DIALOG_STATE state) {
        this.state = state;
        return this;
    }

    @Override
    public void show() {
        if (!isInited) {
            init(mInfo.mTitle, mInfo.mMessage, mInfo.mEnsure, mInfo.mCancel, state, mOnClickListener);
            if (mEnsure != null) {
                mEnsure.setEnabled(enable);
            }
            if (innerIds != -1) {
                setInnerLayout(innerIds);
            } else if (innerView != null) {
                setInnerLayout(innerView);
            }
        }
        super.show();
    }

    public CommonDialog setEnsureEnable(boolean enable) {
        this.enable = enable;
        if (mEnsure != null) {
            mEnsure.setEnabled(enable);
        }
        return this;
    }

    //自己写view
    public CommonDialog(Context context, View view) {
        super(context, R.style.MyAlertDialogTheme);
        isInited = true;
        addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setCancelable(false);
        setDialogStyle(context, this, 0, 0.83f);
    }

    public CommonDialog(Context context, int layoutResID) {
        super(context, R.style.MyAlertDialogTheme);
        mLayoutResID = layoutResID;
    }

    public CommonDialog(Context context, int titleId, int messageId, OnClickListener listener) {
        super(context, R.style.MyAlertDialogTheme);
        init(context.getString(titleId), context.getString(messageId), context.getString(R.string.okay_action), context.getString(R.string.cancel_action), state, listener);
    }

    public CommonDialog(Context context, String title, String message, OnClickListener listener) {
        super(context, R.style.MyAlertDialogTheme);
        init(title, message, context.getString(R.string.okay_action), context.getString(R.string.cancel_action), state, listener);
    }

    public CommonDialog(Context context, int titleId, int messageId, int ensureId, int cancelId, OnClickListener listener) {
        super(context, R.style.MyAlertDialogTheme);
        init(context.getString(titleId), context.getString(messageId), context.getString(ensureId), context.getString(cancelId), state, listener);
    }

    public CommonDialog(Context context, String title, String message, String ensure, String cancel, OnClickListener listener) {
        super(context, R.style.MyAlertDialogTheme);
        init(title, message, ensure, cancel, state, listener);
    }

    public CommonDialog(Context context, String title, String message, String ensure, OnClickListener listener) {
        super(context, R.style.MyAlertDialogTheme);
        init(title, message, ensure, null, state, listener);
    }

    private void init(String title, String message, String ensure, String cancel, DIALOG_STATE state, OnClickListener listener) {
        isInited = true;
        mInfo.mTitle = title;
        mInfo.mMessage = message;
        mInfo.mEnsure = ensure;
        mInfo.mCancel = cancel;
        mOnClickListener = listener;
        this.state = state;
        if (mLayoutResID != -1) {
            setContentView(mLayoutResID);
        } else {
            setContentView(R.layout.common_dialog_phone);
        }

        if (mInfo.blur) {
            Activity activity = getActivityFromContext(getContext());
            if (activity == null) {
                Log.e("BlurDialog", "context is not a Activity Context......");
                return;
            }
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            if (mBlurView == null) {
                mBlurView = new MyBlurView(activity);
                mBlurView.setId(R.id.blur_dialog_bg);
                mBlurView.setAlpha(0f);
                decorView.addView(mBlurView, new ViewGroup.LayoutParams(-1, -1));
            }
        }
        getWindow().setLayout(-2, -2);
        getWindow().setGravity(Gravity.CENTER);
        initView();
    }

    private void initView() {
        mTitle = findViewById(R.id.title);
        mMessage = findViewById(R.id.message);
        mEnsure = findViewById(R.id.ensure);
        mCancel = findViewById(R.id.cancel);
        mCommonLayout = findViewById(R.id.common_layout);
        mContentRoot = findViewById(R.id.content_root);
        ViewGroup mLayoutCancel = findViewById(R.id.layout_cancel);
        ViewGroup mLayoutEnsure = findViewById(R.id.layout_ensure);
        View line = findViewById(R.id.line);
        if (mInfo.clickNoDissmiss) {
            tvErrorTips = findViewById(R.id.errorTips);
            tvErrorTips.setVisibility(View.INVISIBLE);

        }

        if (mInfo.mTitle != null) {
            mTitle.setText(mInfo.mTitle);
        } else {
            mTitle.setVisibility(View.GONE);
        }
        if (mInfo.mEnsure == null) {
            mLayoutEnsure.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        } else {
            mEnsure.setText(mInfo.mEnsure);
        }
        if (mInfo.mCancel == null) {
            mLayoutCancel.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        } else {
            mCancel.setText(mInfo.mCancel);
        }
        if (state == DIALOG_STATE.GUIDE) {
            mEnsure.setBackgroundResource(R.color.colorGray);
        } else if (state == DIALOG_STATE.ALERT) {
            mEnsure.setTextColor(getContext().getResources().getColorStateList(R.color.dialog_alert_color, null));
            mEnsure.setBackgroundResource(R.color.colorGray);
        }
        if (TextUtils.isEmpty(mInfo.mMessage)) {
            mMessage.setVisibility(View.GONE);
        } else {
            mMessage.setText(mInfo.mMessage);
        }
        if (mInfo.bgResId > 0 && mContentRoot != null) {
            mContentRoot.setBackgroundResource(mInfo.bgResId);
        }
//        mEnsure.setOnClickListener(this);
        mLayoutCancel.setOnClickListener(this);
        mLayoutEnsure.setOnClickListener(this);
    }

    public TextView getEnsure() {
        return mEnsure;
    }

    public CommonDialog setInnerLayout(int resId) {
        innerIds = resId;
        if (isInited) {
            View view = LayoutInflater.from(getContext()).inflate(resId, null, false);
            setInnerLayout(view);
        }
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mBlurView != null) {
            mBlurView.blur();
        }
    }

    public CommonDialog setInnerLayout(View view) {
        innerView = view;
        if (isInited) {
            if (view != null && mCommonLayout != null) {
                mCommonLayout.addView(view);
                mCommonLayout.setVisibility(View.VISIBLE);
            }
        }
        return this;
    }

    public CommonDialog setEnsureClickNoDismiss(boolean enable) {
        mInfo.clickNoDissmiss = enable;
        return this;
    }

    public void setErrorTips(int tipsResId, int visibility) {
        if (tvErrorTips != null) {
            tvErrorTips.setText(getContext().getString(tipsResId));
            tvErrorTips.setVisibility(visibility);
        }
    }

    public void setErrorTips(String text, int visibility) {
        if (tvErrorTips != null) {
            tvErrorTips.setText(text);
            tvErrorTips.setVisibility(visibility);
        }
    }

    public void clearErrorTips() {
        if (tvErrorTips != null && tvErrorTips.getVisibility() == View.VISIBLE) {
            setErrorTips("", View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.layout_ensure) {
            if (!mInfo.clickNoDissmiss) {
                dismiss();
            }
            if (mOnClickListener != null) {
                //清空
                clearErrorTips();
                if (mCommonLayout != null) {
                    mOnClickListener.onEnsureClick(mCommonLayout);
                } else {
                    mOnClickListener.onEnsureClick(v);
                }
            }

        } else if (i == R.id.layout_cancel) {
            dismiss();
            if (mOnClickListener != null) {
                if (mCommonLayout != null) {
                    mOnClickListener.onCancelClick(mCommonLayout);
                } else {
                    mOnClickListener.onCancelClick(v);
                }
            }
        }
    }

    private class Info {
        private String mTitle;
        private String mMessage;
        private String mEnsure;
        private String mCancel;
        private boolean clickNoDissmiss;
        private boolean blur;
        private int bgResId;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mBlurView != null) {
            mBlurView.show();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mBlurView != null) {
            mBlurView.hide();
        }
    }

    public interface OnClickListener {
        void onEnsureClick(View view);

        void onCancelClick(View view);
    }

    public void setDialogStyle(Context activity, Dialog dialog, int measuredHeight, float widthRatio) {
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wl = window.getAttributes();
        // 以下这两句是为了保证按钮可以水平满屏
        int width = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        int height = (int) (((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() * 0.9);
        wl.width = (int) (width * widthRatio);  // widthRatio=1为全屏
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;  //  一般情况下为wrapcontent,最大值为height*0.9
        if (measuredHeight > height) {
            wl.height = height;
        }
        // 设置显示位置
        if (!(activity instanceof Activity)) {
            wl.type = WindowManager.LayoutParams.TYPE_TOAST;// keycode to improve window level
        }
        dialog.onWindowAttributesChanged(wl);
    }

    private Activity getActivityFromContext(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
