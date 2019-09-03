package com.example.demo.mvp;

public interface IBaseView extends IView {
    void hideLoadingDialog();

    void showLoadingDialog();

    void showToast(String message);

}
