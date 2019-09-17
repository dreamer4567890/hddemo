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

    private String TAG="cgh";
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    public String beanToJson(Object bean) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(bean);
        return jsonStr;
    }

    private void initView(){
        dialog = new ProgressDialog(this);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
    }

    public void initData() {
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

    /**
     * 失败回调
     * @param t 异常
     */
    @Override
    public void onFail(ExceptionHandle.ResponseException t) {
        Log.d(TAG, t.message.toString());
    }

    @Override
    public void onCompleted() {

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

    @Override
    public void showToast(String message){
        Toast.makeText(RetrofitActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
