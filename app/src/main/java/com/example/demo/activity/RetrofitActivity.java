package com.example.demo.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.bean.RetrofitBean;
import com.example.demo.http.ExceptionHandle;
import com.example.demo.mvp.IBaseView;
import com.example.demo.presenter.RetrofitPresenter;
import com.google.gson.Gson;

public class RetrofitActivity extends BasePresenterActivity<RetrofitPresenter,IBaseView> implements IBaseView {

    private TextView textView;
    private Button button;
    private ProgressDialog dialog;

    @Override
    protected RetrofitPresenter initPresenter(){
        return new RetrofitPresenter();
    }

    @Override
    protected int getLayoutId(){
        return R.layout.activity_retrofit;
    }

    @Override
    protected void initData() {
        setMyActionBar("Retrofit+Rxjava+okhttp3", false);
        dialog = new ProgressDialog(this);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.test("123");
            }
        });
    }

    @Override
    public void onSuccess(Object object) {
        Log.d(TAG, "onSuccess: "+beanToJson(object));
        RetrofitBean retrofitBean= (RetrofitBean) object;
        textView.setText(retrofitBean.getData().getContent());
    }

}
