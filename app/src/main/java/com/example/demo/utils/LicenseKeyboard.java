package com.example.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.demo.R;

import java.lang.reflect.Method;

/**
 * Created by liangzs on 2018/1/16.
 */
public class LicenseKeyboard {
    private static final String TAG = "LicenseKeyboard";
    private KeyboardView keyboardView;
    private Keyboard k1;// 省份简称键盘
    private Keyboard k2;// 数字字母键盘

    private String provinceShort[];
    private String letterAndDigit[];

    private TextView edits[];
    private int currentTextView = 0;//默认当前光标在第一个TextView
    private Activity ctx;

    public LicenseKeyboard(KeyboardView keyboard_view, LinearLayout boxesContainer, Activity ctx, TextView edits[]) {
        this.edits = edits;
        this.ctx = ctx;
        k1 = new Keyboard(ctx, R.xml.province_short_keyboard);
        k2 = new Keyboard(ctx, R.xml.lettersanddigit_keyboard);
        keyboardView = keyboard_view;
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        //设置为true时,当按下一个按键时会有一个popup来显示<key>元素设置的android:popupCharacters=""
        keyboardView.setPreviewEnabled(true);
        //设置键盘按键监听器
        provinceShort = new String[]{"京", "沪", "粤", "津", "冀", "晋", "蒙", "辽", "吉","黑"
                                   , "苏", "浙", "皖", "闽", "赣", "鲁", "豫", "鄂", "湘","桂"
                                   , "琼", "渝", "川", "贵", "云", "藏", "陕", "甘", "青","宁"
                                   , "新", "港", "澳", "学"};

        letterAndDigit = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9","0"
                                    , "A", "B", "C", "D", "E", "F", "G", "H", "J","K"
                                    , "L", "M", "N", "P", "Q", "R", "S", "T", "U","V"
                                    , "W", "X", "Y", "Z", "港","澳","学"};

        keyboardView.setOnKeyboardActionListener(listener);
    }

    private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
            Log.i(TAG,"onPress primaryCode=" + primaryCode);
            if (primaryCode == 112) {
                keyboardView.setPreviewEnabled(false);
            } else {
                keyboardView.setPreviewEnabled(true);
            }
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            if (primaryCode == 112) {  //xml中定义的删除键值为112
                if (currentTextView < 1) {
                    currentTextView = 0;
                    edits[currentTextView].setText("");

                } else {
                    edits[currentTextView].setText("");
                    currentTextView--;
                    if (currentTextView == 0) {
                        keyboardView.setKeyboard(k1);
                    }
                }
                edits[currentTextView].requestFocus();
            } else if (primaryCode == 66) { //xml中定义的完成键值为66
                if (isFinishInput()) {
                    hideKeyboard();
                }
            } else {//键盘内容
                if (currentTextView == 0) {
                    edits[0].setText(provinceShort[primaryCode]);
                    if (edits[currentTextView] instanceof EditText) {
                        ((EditText) edits[currentTextView]).setSelection(1);
                    }
                    keyboardView.setKeyboard(k2);
                    currentTextView++;
                    edits[currentTextView].requestFocus();
                } else {//省份后面需要填入字母不能是数字
                    keyboardView.setKeyboard(k2);
                    if (currentTextView == 1 && !letterAndDigit[primaryCode].matches("[A-Z]{1}")) {
                        return;
                    }
                    edits[currentTextView].setText(letterAndDigit[primaryCode]);
                    if (edits[currentTextView] instanceof EditText) {
                        ((EditText) edits[currentTextView]).setSelection(1);
                    }
                    currentTextView++;
                    if (currentTextView > 7) {
                        currentTextView = 7;
                    }
                    edits[currentTextView].requestFocus();

                }
            }
        }
    };

    /**
     * 设置键盘
     */
    public void setKeyboardViewPosition(EditText t) {
        for (int i = 0; i < edits.length; i++) {
            if (t == edits[i]) {
                currentTextView = i;
                if (i == 0) {
                    keyboardView.setKeyboard(k1);
                } else {
                    keyboardView.setKeyboard(k2);
                }
            }
        }
        if (t instanceof EditText) {
//            ((InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(t.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            ((InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(t.getWindowToken(), 0);
//            ctx.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            ctx.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus;
                setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(t, false);
            } catch (Exception e) {
                Log.e("cgh", e.getMessage());
            }
        }

    }


    /**
     * 显示键盘
     */
    public void showKeyboard() {
        if (!ObjectUtils.isEmpty(ctx.getCurrentFocus()) && !ObjectUtils.isEmpty(ctx.getCurrentFocus().getWindowToken())) {
            ((InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ctx.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.INVISIBLE);
        }
    }

    public boolean isShow() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }


    public boolean isFinishInput() {
        for (int i = 0; i < 7; i++) {
            String nowEdit = edits[i].getText().toString();
            if (TextUtils.isEmpty(nowEdit)) {
                return false;
            }
        }
        return true;
    }

    public String getCarPlateNum() {
        String license = "";
        for (int i = 0; i < 8; i++) {
            license += edits[i].getText().toString();
        }
        return license;
    }

    public void setValue(String value) {
        if (!ObjectUtils.isEmpty(value)) {
            for (int i = 0; i < value.length(); i++) {
                edits[i].setText(value.charAt(i) + "");
            }
        }
    }

    public void clear() {
        for (int i = 0; i < 8; i++) {
            edits[i].setText("");
        }
        keyboardView.setKeyboard(k1);
    }
}
