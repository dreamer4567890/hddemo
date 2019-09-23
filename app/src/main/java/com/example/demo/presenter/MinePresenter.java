package com.example.demo.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;

import com.example.demo.mvp.BasePresenter;
import com.example.demo.mvp.IBaseView;

import static android.content.Context.MODE_PRIVATE;

public class MinePresenter extends BasePresenter<IBaseView> {

    public String getMyInfo(Context view){
        SharedPreferences pref = view.getSharedPreferences("data", MODE_PRIVATE);
        if (!TextUtils.isEmpty(pref.getString("nickname",null))){
            return pref.getString("nickname",null);
        }
        return null;
    }
}
