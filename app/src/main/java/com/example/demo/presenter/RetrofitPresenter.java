package com.example.demo.presenter;

import com.example.demo.bean.RetrofitBean;
import com.example.demo.http.ExceptionHandle;
import com.example.demo.http.SubscriptionManager;
import com.example.demo.model.Observer;
import com.example.demo.model.RetrofitModel;
import com.example.demo.mvp.BasePresenter;
import com.example.demo.mvp.IBaseView;

import io.reactivex.disposables.Disposable;

public class RetrofitPresenter extends BasePresenter<IBaseView> {

    private RetrofitModel testModel;
    /**
     * 绑定M层
     */
    public RetrofitPresenter (){
        testModel=new RetrofitModel();
    }

    public void test(String time){
        //网络请求，创建观察者
        testModel.getTestInfo(time, new Observer<RetrofitBean>() {
            @Override
            protected void OnDisposable(Disposable d) {
                SubscriptionManager.getInstance().add(d);
            }

            @Override
            protected void OnSuccess(RetrofitBean testBean) {
                mView.onSuccess(testBean);
            }

            @Override
            protected void OnFail(ExceptionHandle.ResponseException e) {
                mView.onFail(e);
            }

            @Override
            protected void OnCompleted() {
                mView.onCompleted();
            }
        });
    }
}
