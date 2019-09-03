package com.example.demo.mvp;

import android.os.Bundle;
import android.os.PersistableBundle;

public class BasePresenter<V extends IView> implements IBasePresenter<V> {

    protected V mView;

    @Override
    public void onAttachView(V view) {

        this.mView = view;
    }

    @Override
    public void onDetachView() {
        this.mView = null;
        //RXManager.get().onUnsubscribe();

    }

    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {


    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }
}