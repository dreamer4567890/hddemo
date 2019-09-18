package com.example.demo.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.bean.RetrofitBean;
import com.example.demo.http.ExceptionHandle;
import com.example.demo.mvp.IBaseView;
import com.google.gson.Gson;


public abstract class BaseUiActivity extends BaseActivity implements IBaseView {

    public String TAG="cgh";
    public Context context;
    private TextView tvTitle;
    private ImageView ivBack;
    private ImageView ivSearch;
    private View actionBar;
    private ComponentName launchComponentName;
    private ComponentName componentName;
    private ProgressDialog dialog;

    private long exitTime = 0;

    protected abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=this;
        setContentView(getLayoutId());
        super.onCreate(savedInstanceState);
        init();
        initData();
    }

    protected void initData() {
    }

    private void init() {
        dialog = new ProgressDialog(this);
        actionBar = findViewById(R.id.action_bar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivSearch = (ImageView) findViewById(R.id.iv_search);

        PackageManager packageManager = this.getApplication().getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(this.getPackageName());
        launchComponentName = intent.getComponent();
        componentName = this.getComponentName();
        if(componentName.toString().equals(launchComponentName.toString())){

        }else {

        }
    }

    public void setMyActionBar(String strTitle,boolean isSearch) {
        if (!TextUtils.isEmpty(strTitle)) {
            tvTitle.setText(strTitle);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        if(!isSearch){
            ivSearch.setVisibility(View.INVISIBLE);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (componentName.toString().equals(launchComponentName.toString())){
                    exit();
                } else {
                    finish();
                }
            }
        });
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BaseUiActivity.this,"搜索功能暂未开放",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    public String beanToJson(Object bean) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(bean);
        return jsonStr;
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
