/*
package com.example.demo.init;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

public class HDBaseInitializer implements Initializer<Boolean> {

    private final static String TAG = "HDNetInitializer";


    @NonNull
    @Override
    public Boolean create(@NonNull Context context) {
        BHLog.init(context);
        MMKV.initialize(context);
        initExceptionCatcher();
        HDQuery.init();
        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }

    private void initExceptionCatcher() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                BHLog.d(TAG, "throw test");
            }
        });
    }
}
*/
