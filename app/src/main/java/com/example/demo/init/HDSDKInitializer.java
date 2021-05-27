/*
package com.example.demo.init;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import java.util.Collections;
import java.util.List;

import cn.evergrande.it.utils.LiveBus;
import kotlin.Unit;


public class HDSDKInitializer implements Initializer<Unit> {

    @NonNull
    @Override
    public Unit create(@NonNull Context context) {
        LiveBus.ready();
        //初始化服务
        Intent work = new Intent();
        InitSdkService.enqueueWork(context, work);
        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
*/
