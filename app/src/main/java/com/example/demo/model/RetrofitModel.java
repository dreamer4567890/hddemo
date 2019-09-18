package com.example.demo.model;

import com.example.demo.bean.RetrofitBean;
import com.example.demo.http.RetrofitManager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RetrofitModel {

    public void getTestInfo(String time, Observer<RetrofitBean> observer){
        RetrofitManager.getInstance().service().getData(time)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }
}
