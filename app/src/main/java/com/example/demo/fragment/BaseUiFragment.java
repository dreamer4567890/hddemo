package com.example.demo.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.http.ExceptionHandle;
import com.example.demo.mvp.IBaseView;
import com.google.gson.Gson;

public abstract class BaseUiFragment extends BaseFragment implements IBaseView {

    public String TAG="cgh";
    protected ViewGroup containerView;
    private ProgressDialog dialog;

    @Override
    protected View getBaseView() {
        containerView = (ViewGroup) View.inflate(getActivity(), R.layout.base_fragment_layout, null);
        View contentView = View.inflate(getActivity(), getLayoutId(), null);
        containerView.addView(contentView);
        return containerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dialog = new ProgressDialog(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showLoadingDialog() {
        if (dialog!=null&&!dialog.isShowing()){
            dialog.show();
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }

    protected abstract int getLayoutId();

    @Override
    public void showToast(final String message) {
        final FragmentActivity activity = getActivity();
        if (activity != null) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            } else {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    public String beanToJson(Object bean) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(bean);
        return jsonStr;
    }

    @Override
    public void onSuccess(Object object) {
        Log.d(TAG, "onSuccess: "+beanToJson(object));
    }

    @Override
    public void onFail(ExceptionHandle.ResponseException t) {
        Log.d(TAG, t.message.toString());
    }

    @Override
    public void onCompleted() {
        Log.d(TAG, "onCompleted");
    }
}
