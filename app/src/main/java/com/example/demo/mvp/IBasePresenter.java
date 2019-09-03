package com.example.demo.mvp;

public interface IBasePresenter<V extends IView> {

    void onAttachView(V view);

    void onDetachView();
}
