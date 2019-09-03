package com.example.demo.Presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.demo.Bean.Movie;
import com.example.demo.mvp.BasePresenter;
import com.example.demo.mvp.IBaseView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MovieInfoPresenter extends BasePresenter<MovieInfoPresenter.View> {

    private static SharedPreferences sp;

    public static void putMovieInfo(Context context, List<Movie> movieList){
        if(sp == null){
            sp = context.getSharedPreferences("movie",Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(movieList);
        editor.putString("movieInfo",json);
        editor.commit();
    }

    public static List<Movie> getMovieInfo(Context context){
        if(sp == null){
            sp = context.getSharedPreferences("movie",Context.MODE_PRIVATE);
        }
        Gson gson = new Gson();
        String json = sp.getString("movieInfo",null);
        Type type = new TypeToken<List<Movie>>(){}.getType();
        List<Movie> arrayList = gson.fromJson(json,type);
        return arrayList;
    }

    public interface View extends IBaseView {
        void onMovieSucess();

        void onMovieFailed(String errorMsg);
    }
}
