package com.example.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.thread.ThreadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by leilei on 16/5/6.
 */
public class ToastUtil {
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_ERROR = -1;
    private static final int TYPE_SUCCESS = 1;
    private static long currentToastTime;
    private static String currentMessage;
    private static final int COLOR_DEFAULT = 0xFEFFFFFF;
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private static Toast sToast;
    private static WeakReference<View> sViewWeakReference;
    private static int sLayoutId = -1;
    private static int gravity = Gravity.CENTER_VERTICAL;
    private static int xOffset = 0;
    //    private static int yOffset = (int) (64 * AppTrace.getContext().getResources().getDisplayMetrics().density + 0.5);
    private static int bgColor = COLOR_DEFAULT;
    private static int yOffset = 0;
    private static int bgResource = -1;
    private static int msgColor = COLOR_DEFAULT;

    public static void toastNormal(String message) {
        toast(message, Toast.LENGTH_SHORT, TYPE_NORMAL);
    }

    public static void toastNormal(int message) {
        toast(message, Toast.LENGTH_SHORT, TYPE_NORMAL);
    }

    public static void toastError(String message) {
        toast(message, Toast.LENGTH_SHORT, TYPE_ERROR);
    }

    public static void toastErrorCodeTip(int errorCode) {
        toast(getErrorTips(errorCode), Toast.LENGTH_SHORT, TYPE_ERROR);
    }

    public static void toastError(int message) {
        toast(message, Toast.LENGTH_SHORT, TYPE_ERROR);
    }

    public static void toastSuccess(String message) {
        toast(message, Toast.LENGTH_LONG, TYPE_SUCCESS);
    }

    public static void toastSuccess(int message) {
        toast(message, Toast.LENGTH_LONG, TYPE_SUCCESS);
    }

    /**
     * @author wangyuhang
     * 主页管理页面右侧边栏特有的toast
     */
    public static void showHomePageManageLabelToast(Activity activity, int position, String message) {
        Context context = AppTrace.getContext();
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP, 675, 190 + position * 120);

        View toastRoot = activity.getLayoutInflater().inflate(R.layout.home_page_manage_lebel_toast_pad, null);

        TextView textView = (TextView) toastRoot.findViewById(R.id.home_page_manage_label_toast_tv);
        textView.setText(message);

        toast.setView(toastRoot);

        toast.show();
    }

    /**
     * @author liulei
     * 主页管理页面总开关特有的toast
     */
    public static void showHomePageManageTotalSwitchToast(Activity activity, int position, String message) {
        Context context = AppTrace.getContext();
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP, 675, position);

        View toastRoot = activity.getLayoutInflater().inflate(R.layout.home_page_manage_lebel_toast_pad, null);

        TextView textView = (TextView) toastRoot.findViewById(R.id.home_page_manage_label_toast_tv);
        textView.setText(message);

        toast.setView(toastRoot);

        toast.show();
    }

    public static void showHomePageManageLabelToastPhone(Activity activity, int position, String message) {
        Context context = AppTrace.getContext();
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP, 0, 190 + position * 120);

        View toastRoot = activity.getLayoutInflater().inflate(R.layout.home_page_manage_lebel_toast_phone, null);

        TextView textView = (TextView) toastRoot.findViewById(R.id.home_page_manage_label_toast_tv);
        textView.setText(message);

        toast.setView(toastRoot);

        toast.show();
    }

    public static void showHomePageManageTotalSwitchToastPhone(Activity activity, int position, String message) {
        Context context = AppTrace.getContext();
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, position);

        View toastRoot = activity.getLayoutInflater().inflate(R.layout.home_page_manage_lebel_toast_phone, null);

        TextView textView = (TextView) toastRoot.findViewById(R.id.home_page_manage_label_toast_tv);
        textView.setText(message);

        toast.setView(toastRoot);

        toast.show();
    }

    public static void showBottomPopHint(Activity activity, int position, String message) {
        Context context = AppTrace.getContext();
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, position);

        View toastRoot = activity.getLayoutInflater().inflate(R.layout.bottom_pop_toast_phone, null);

        TextView textView = (TextView) toastRoot.findViewById(R.id.bottom_pop_toast_tv);
        textView.setText(message);

        toast.setView(toastRoot);

        toast.show();
    }

    private static void toast(final String message, final int time, final int type) {
        toastCustom(message, time, type);
    }

    private static void toast(int message, int time, int type) {
        toastCustom(AppTrace.getContext().getString(message), time, type);
    }

    private static void toastCustom(final String message, final int time, final int type) {
        //TODO ch 防止弹出黑框 & 同一消息反复弹框 默认Toast的时间应该是2.5s
        if (TextUtils.isEmpty(message) ||
                ((System.currentTimeMillis() - currentToastTime < 2500)
                        && message.equalsIgnoreCase(currentMessage))) {
            return;
        } else {
            currentToastTime = System.currentTimeMillis();
            currentMessage = message;
            ThreadManager.getInstance()
                    .postUITask(new Runnable() {
                        @Override
                        public void run() {
                            cancel();
                            sToast = new Toast(AppTrace.getContext());
                            sToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            sToast.setDuration(time);
                            View view = null;
                            switch (type) {
                                case TYPE_NORMAL:
                                    view = getNormalToastView(message);
                                    break;
                                case TYPE_ERROR:
                                    view = getErrorToastView(message);
                                    break;
                                case TYPE_SUCCESS:
                                    view = getSuccessToastView(message);
                                    break;
                                default:
                                    view = getNormalToastView(message);
                                    break;

                            }
                            sToast.setView(view);
                            sToast.show();
                        }
                    });
        }
    }

    //h5的toast需要解析json
    public static void toastByH5(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            //JSONObject data = (JSONObject) jsonObject.get("data");

            String message = jsonObject.getString("message");
            if (!TextUtils.isEmpty(message)) {
                toastNormal(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 这个方法用来自定义错误情况下toast
     *
     * @param message
     * @return
     */
    private static View getErrorToastView(String message) {
        TextView textView = new TextView(AppTrace.getContext());
        setNormalAttr(textView);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ScreenUtils.dpToPx(200), -2);
        textView.setLayoutParams(layoutParams);
        textView.setText(message);
        return textView;
    }


    /**
     * 这个方法用来自定义成功情况下toast
     *
     * @param message
     * @return
     */
    private static View getSuccessToastView(String message) {
        TextView textView = new TextView(AppTrace.getContext());
        setNormalAttr(textView);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ScreenUtils.dpToPx(200), -2);
        textView.setLayoutParams(layoutParams);
        textView.setText(message);
        return textView;
    }


    /**
     * 这个方法用来自定义普通情况下toast
     *
     * @param message
     * @return
     */
    private static View getNormalToastView(String message) {
        TextView textView = new TextView(AppTrace.getContext());
        setNormalAttr(textView);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ScreenUtils.dpToPx(200), -2);
        textView.setLayoutParams(layoutParams);
        textView.setText(message);
        return textView;
    }

    private static void setNormalAttr(TextView textView) {
        textView.setBackgroundResource(R.drawable.shape_toast_bg);
        if (isTabletDevice(AppTrace.getContext())) {
            textView.setPadding(ScreenUtils.dpToPx(48),
                    ScreenUtils.dpToPx(28),
                    ScreenUtils.dpToPx(48),
                    ScreenUtils.dpToPx(28));
            textView.setTextSize(0, ScreenUtils.dpToPx(24));
        } else {
            textView.setPadding(ScreenUtils.dpToPx(20),
                    ScreenUtils.dpToPx(12),
                    ScreenUtils.dpToPx(20),
                    ScreenUtils.dpToPx(12));
            textView.setTextSize(0, ScreenUtils.dpToPx(14));
        }

        textView.setTextColor(Color.parseColor("#FFFFFF"));
    }

    /**
     * 判断是否平板设备
     *
     * @param context
     * @return true:平板,false:手机
     */
    public static boolean isTabletDevice(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    //ch tv提示tip; 前提设备未连接1.连接外网提示:暂不支持远程查看 2.提示网络异常tip
    public static void toastTvTips(String tips) {
        toast(tips, Toast.LENGTH_SHORT, TYPE_NORMAL);
    }


    /**
     * 通过errorCode获取errorTips
     */
    public static String getErrorTips(int errorCode) {
        if (errorCode < 0) {
            errorCode = 0 - errorCode;
        }

        String stringName = "error_code_tips_" + errorCode;
        int resId = AppTrace.getContext().getResources().getIdentifier(stringName, "string", AppTrace.getContext().getPackageName());

        if (resId != 0) {
            return AppTrace.getContext().getResources().getString(resId);
        } else {
            return null;
        }
    }

    /**
     * 设置吐司位置
     *
     * @param gravity 位置
     * @param xOffset x 偏移
     * @param yOffset y 偏移
     */
    public static void setGravity(final int gravity, final int xOffset, final int yOffset) {
        ToastUtil.gravity = gravity;
        ToastUtil.xOffset = xOffset;
        ToastUtil.yOffset = yOffset;
    }

    /*
     * 修复吐司里面的文字在部分机型左对齐显示，改为文字居中
     * */
    public static void showToastCenter(Context context, String toastStr) {
        Toast toast = Toast.makeText(context.getApplicationContext(), toastStr, Toast.LENGTH_SHORT);
        int tvToastId = Resources.getSystem().getIdentifier("message", "id", "android");
        TextView tvToast = ((TextView) toast.getView().findViewById(tvToastId));
        if (tvToast != null) {
            tvToast.setGravity(Gravity.CENTER);
        }
        toast.show();
    }

    /**
     * 设置背景颜色
     *
     * @param backgroundColor 背景色
     */
    public static void setBgColor(@ColorInt final int backgroundColor) {
        ToastUtil.bgColor = backgroundColor;
    }

    /**
     * 设置背景资源
     *
     * @param bgResource 背景资源
     */
    public static void setBgResource(@DrawableRes final int bgResource) {
        ToastUtil.bgResource = bgResource;
    }

    /**
     * 设置消息颜色
     *
     * @param msgColor 颜色
     */
    public static void setMsgColor(@ColorInt final int msgColor) {
        ToastUtil.msgColor = msgColor;
    }

    /**
     * 安全地显示短时吐司
     *
     * @param text 文本
     */
    public static void showShort(@NonNull final CharSequence text, boolean isVisiable) {
        if (isVisiable) {
            show(text, Toast.LENGTH_SHORT);
        }
    }

    public static void showShort(@NonNull final CharSequence text) {
        show(text, Toast.LENGTH_SHORT);
    }

    /**
     * 安全地显示短时吐司
     *
     * @param resId 资源 Id
     */
    public static void showShort(@StringRes final int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    /**
     * 安全地显示短时吐司
     *
     * @param resId 资源 Id
     * @param args  参数
     */
    public static void showShort(@StringRes final int resId, final Object... args) {
        show(resId, Toast.LENGTH_SHORT, args);
    }

    /**
     * 安全地显示短时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showShort(final String format, final Object... args) {
        show(format, Toast.LENGTH_SHORT, args);
    }

    /**
     * 安全地显示长时吐司
     *
     * @param text 文本
     */
    public static void showLong(@NonNull final CharSequence text) {
        show(text, Toast.LENGTH_LONG);
    }

    /**
     * 安全地显示长时吐司
     *
     * @param resId 资源 Id
     */
    public static void showLong(@StringRes final int resId) {
        show(resId, Toast.LENGTH_LONG);
    }

    /**
     * 安全地显示长时吐司
     *
     * @param resId 资源 Id
     * @param args  参数
     */
    public static void showLong(@StringRes final int resId, final Object... args) {
        show(resId, Toast.LENGTH_LONG, args);
    }

    /**
     * 安全地显示长时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showLong(final String format, final Object... args) {
        show(format, Toast.LENGTH_LONG, args);
    }

    /**
     * 安全地显示短时自定义吐司
     */
    public static View showCustomShort(@LayoutRes final int layoutId) {
        final View view = getView(layoutId);
        show(view, Toast.LENGTH_SHORT);
        return view;
    }

    /**
     * 安全地显示长时自定义吐司
     */
    public static View showCustomLong(@LayoutRes final int layoutId) {
        final View view = getView(layoutId);
        show(view, Toast.LENGTH_LONG);
        return view;
    }

    /**
     * 取消吐司显示
     */
    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }

    private static void show(@StringRes final int resId, final int duration) {
        show(AppTrace.getContext().getResources().getText(resId).toString(), duration);
    }

    private static void show(@StringRes final int resId, final int duration, final Object... args) {
        show(String.format(AppTrace.getContext().getResources().getString(resId), args), duration);
    }

    private static void show(final String format, final int duration, final Object... args) {
        show(String.format(format, args), duration);
    }

    private static void show(final CharSequence text, final int duration) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                cancel();
                /*sToast = Toast.makeText(AppTrace.getContext(), text, duration);
                // solve the font of toast
                TextView tvMessage = sToast.getView().findViewById(android.R.id.message);
                TextViewCompat.setTextAppearance(tvMessage, android.R.style.TextAppearance);
                tvMessage.setTextColor(msgColor);
                sToast.setGravity(gravity, xOffset, yOffset);
                setBg(tvMessage);
                sToast.show();*/

                sToast = new Toast(AppTrace.getContext());
                sToast.setGravity(gravity, xOffset, yOffset);
                sToast.setDuration(duration);
                if (null != text) {
                    View view = getNormalToastView(text.toString());
                    sToast.setView(view);
                    sToast.show();
                }
            }
        });
    }

    private static void show(final View view, final int duration) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                cancel();
                sToast = new Toast(AppTrace.getContext());
                sToast.setView(view);
                sToast.setDuration(duration);
                sToast.setGravity(gravity, xOffset, yOffset);
                setBg();
                sToast.show();
            }
        });
    }

    private static void setBg() {
        View toastView = sToast.getView();
        if (bgResource != -1) {
            toastView.setBackgroundResource(bgResource);
        } else if (bgColor != COLOR_DEFAULT) {
            Drawable background = toastView.getBackground();
            if (background != null) {
                background.setColorFilter(
                        new PorterDuffColorFilter(bgColor, PorterDuff.Mode.SRC_IN)
                );
            } else {
                ViewCompat.setBackground(toastView, new ColorDrawable(bgColor));
            }
        }
    }

    private static void setBg(final TextView tvMsg) {
        View toastView = sToast.getView();
        if (bgResource != -1) {
            toastView.setBackgroundResource(bgResource);
            tvMsg.setBackgroundColor(Color.TRANSPARENT);
        } else if (bgColor != COLOR_DEFAULT) {
            Drawable tvBg = toastView.getBackground();
            Drawable msgBg = tvMsg.getBackground();
            if (tvBg != null && msgBg != null) {
                tvBg.setColorFilter(new PorterDuffColorFilter(bgColor, PorterDuff.Mode.SRC_IN));
                tvMsg.setBackgroundColor(Color.TRANSPARENT);
            } else if (tvBg != null) {
                tvBg.setColorFilter(new PorterDuffColorFilter(bgColor, PorterDuff.Mode.SRC_IN));
            } else if (msgBg != null) {
                msgBg.setColorFilter(new PorterDuffColorFilter(bgColor, PorterDuff.Mode.SRC_IN));
            } else {
                toastView.setBackgroundColor(bgColor);
            }
        }
    }

    private static View getView(@LayoutRes final int layoutId) {
        if (sLayoutId == layoutId) {
            if (sViewWeakReference != null) {
                final View toastView = sViewWeakReference.get();
                if (toastView != null) {
                    return toastView;
                }
            }
        }
        LayoutInflater inflate =
                (LayoutInflater) AppTrace.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflate == null) return null;
        final View toastView = inflate.inflate(layoutId, null);
        sViewWeakReference = new WeakReference<>(toastView);
        sLayoutId = layoutId;
        return toastView;
    }
}
