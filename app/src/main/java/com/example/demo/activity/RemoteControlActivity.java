package com.example.demo.activity;


import com.example.demo.R;
import com.example.demo.widget.RemoteControl;
import com.example.demo.widget.RemoteControl1;

public class RemoteControlActivity extends BaseUiActivity {

    private RemoteControl1 newRemoteControl;

    @Override
    protected int getLayoutId(){
        return R.layout.activity_remote_control;
    }

    @Override
    protected void initData(){
        newRemoteControl = findViewById(R.id.my);
    }
}
