package com.example.demo.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.demo.mvp.BasePresenter;
import com.example.demo.mvp.IView;

public abstract class BasePresenterFragment<P extends BasePresenter<V>, V extends IView> extends BaseUiFragment {

    protected P mPresenter;


    protected abstract P initPresenter();


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mPresenter = initPresenter();

        if (mPresenter != null) {
            mPresenter.onAttachView((V) this);
        }
    }

    @Override
    public void onDetach() {
        if (mPresenter != null) {
            mPresenter.onDetachView();
            mPresenter = null;
        }
        super.onDetach();
    }
}
