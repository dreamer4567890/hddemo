package com.example.demo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.demo.activity.BaseFragmentActivity;

public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    protected boolean mResumed;
    protected boolean mCreate;
    protected boolean mStopped;
    protected boolean mDestroyed;
    protected boolean mHidden;

    private boolean mResumedForFirstTime;
    public Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDestroyed = false;
        mCreate = true;
        Log.d(TAG, "[" + getClass().getSimpleName() + "]: onCreate, Hidden = " + mHidden);
    }


    @Override
    public void onStart() {
        super.onStart();
        mStopped = false;
        Log.d(TAG, "[" + getClass().getSimpleName() + "]: onStart, Hidden = " + mHidden);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mHidden = hidden;
        Log.d(TAG, "[" + getClass().getSimpleName() + "]: onHiddenChanged " + hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
        mResumed = true;
        if (!mResumedForFirstTime) {
            mResumedForFirstTime = true;
        }
        Log.d(TAG, "[" + getClass().getSimpleName() + "]: onResume, Hidden = " + mHidden);
    }

    @Override
    public void onPause() {
        super.onPause();
        mResumed = false;
        Log.d(TAG, "[" + getClass().getSimpleName() + "]: onPause, Hidden = " + mHidden);
    }

    @Override
    public void onStop() {
        super.onStop();
        mStopped = true;
        Log.d(TAG, "[" + getClass().getSimpleName() + "]: onStop, Hidden = " + mHidden);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mDestroyed = true;
        mResumedForFirstTime = false;
        Log.d(TAG, "[" + getClass().getSimpleName() + "]: onDestroy, Hidden = " + mHidden);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
        Log.d(TAG, "[" + getClass().getSimpleName() + "]: onDetach");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        Log.d(TAG, "[" + getClass().getSimpleName() + "]: onAttach");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "[" + getClass().getSimpleName() + "]: onCreateView");
        View view = getBaseView();
        initView(view);
        initData();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "[" + getClass().getSimpleName() + "]: onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "[" + getClass().getSimpleName() + "]: onActivityCreated");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "[" + getClass().getSimpleName() + "]: setUserVisibleHint " + isVisibleToUser);
    }

    /**
     * replace 方式添加
     *
     * @param fragment
     */
    public void addFragment(BaseFragment fragment) {
        if (fragment != null && mActivity instanceof BaseFragmentActivity) {
            ((BaseFragmentActivity) mActivity).addFragment(fragment);
        }

    }

    /**
     * fragment不放入堆栈，替换
     *
     * @param fragment
     */
    public void addFragmentNoAdd(BaseFragment fragment) {
        if (fragment != null && mActivity instanceof BaseFragmentActivity) {
            ((BaseFragmentActivity) mActivity).addFragmentNoAdd(fragment);
        }

    }

    /**
     * 直接添加，不放入堆栈
     *
     * @param fragment
     */
    public void addFragmentAdd(BaseFragment fragment) {
        if (fragment != null && mActivity instanceof BaseFragmentActivity) {
            ((BaseFragmentActivity) mActivity).addFragmentAdd(fragment);
        }

    }

    /**
     * 添加fragment
     * 用add和hide主要是不替换fragment， 可以用接口形式进行数据交互
     * 多页面的长数据建议用单例存储
     *
     * @param fragment
     * @param hideFragment
     */
    public void addFragmentAddHide(BaseFragment fragment, BaseFragment hideFragment) {
        if (fragment != null && mActivity instanceof BaseFragmentActivity) {
            ((BaseFragmentActivity) mActivity).addFragmentAddHide(fragment, hideFragment);
        }

    }

    /**
     * 清空fragment前的所有堆栈
     * type 0 弹出fragment 之上所有
     * type 1弹出  fragment和之上所有（包括本身）
     */
    public void popBackStackImmediate(BaseFragment fragment, int type) {
        if (fragment != null && mActivity instanceof BaseFragmentActivity) {
            ((BaseFragmentActivity) mActivity).popBackStackImmediate(fragment, type);
        }
    }

    /**
     * 清空fragment前的所有堆栈
     * type 0 弹出fragment 之上所有
     * type 1弹出  fragment和之上所有（包括本身）
     */
    public void clearTopFragment(BaseFragment fragment) {
        if (fragment != null && mActivity instanceof BaseFragmentActivity) {
            ((BaseFragmentActivity) mActivity).clearTopFragment(fragment);
        }
    }

    protected BaseFragmentActivity getHoldActivity() {
        if (mActivity instanceof BaseFragmentActivity) {
            return ((BaseFragmentActivity) mActivity);
        } else {
            return null;
        }
    }


    public boolean onBackAction() {
        return true;
    }

    // 在UI线程弹出toast
    public void showToast(final String msg) {
        final FragmentActivity activity = getActivity();
        if (activity != null) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            } else {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }


    // 隐藏软键盘
    public void hideSoftInput() {
        Activity context = getActivity();
        if (context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            View decorView = context.getWindow().getDecorView();
            imm.hideSoftInputFromWindow(decorView.getWindowToken(), 0);
        }
    }

    // 当前Fragment是否显示，或者需要被显示
    // 当执行onResume方法后，就使用mHidden变量来替代
    public boolean isShow() {
        if (mResumedForFirstTime) {
            return !mHidden;
        } else {
            return mCreate;
        }
    }

    protected abstract View getBaseView();

    protected void initView(View content) {

    }

    protected void initData() {

    }
}
