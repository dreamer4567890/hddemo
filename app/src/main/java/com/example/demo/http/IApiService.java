package com.example.demo.http;

import com.example.demo.bean.RetrofitBean;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IApiService {
    @POST("/lay-eggs/androidtest.php")
    Observable<RetrofitBean> getData(@Query("time") String time);
}
