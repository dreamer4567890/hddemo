package com.example.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.example.demo.http.SubscriptionManager;
import com.example.demo.mvp.BasePresenter;
import com.example.demo.mvp.IView;

public abstract class BasePresenterActivity<P extends BasePresenter<V>, V extends IView> extends BaseUiActivity {

    protected P mPresenter;

    protected abstract P initPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPresenter = initPresenter();
        if (mPresenter != null) {
            mPresenter.onAttachView((V) this);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mPresenter.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mPresenter.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDetachView();
            mPresenter = null;
            SubscriptionManager.getInstance().cancelAll();
        }
        super.onDestroy();
    }
}
