package com.example.demo.mvp;

import com.example.demo.http.ExceptionHandle;

public interface IBaseView extends IView {
    void hideLoadingDialog();

    void showLoadingDialog();

    void showToast(String message);

    /**
     * 成功回调
     * @param object
     */
    void onSuccess(Object object);

    /**
     * 失败回调
     * @param t 异常
     */
    void onFail(ExceptionHandle.ResponseException t);

    /**
     * 发射结束
     */
    void onCompleted();

}
