package com.example.demo.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;


public class BaseActionBarActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ImageView ivBack;
    private ImageView ivSearch;
    private View actionBar;
    private ComponentName launchComponentName;
    private ComponentName componentName;

    private long exitTime = 0;

    private void init() {
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
        init();
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
                Toast.makeText(BaseActionBarActivity.this,"搜索功能暂未开放",Toast.LENGTH_SHORT).show();
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
}
