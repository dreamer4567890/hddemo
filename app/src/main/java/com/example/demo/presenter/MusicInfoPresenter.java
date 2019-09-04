package com.example.demo.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.demo.bean.Music;
import com.example.demo.mvp.BasePresenter;
import com.example.demo.mvp.IBaseView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MusicInfoPresenter extends BasePresenter<MusicInfoPresenter.View> {

    private static SharedPreferences sp;

    public static void putMusicInfo(Context context, List<Music> musicList){
        if(sp == null){
            sp = context.getSharedPreferences("music",Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(musicList);
        editor.putString("musicInfo",json);
        editor.commit();
    }

    public static List<Music> getMusicInfo(Context context){
        if(sp == null){
            sp = context.getSharedPreferences("music",Context.MODE_PRIVATE);
        }
        Gson gson = new Gson();
        String json = sp.getString("musicInfo",null);
        Type type = new TypeToken<List<Music>>(){}.getType();
        List<Music> arrayList = gson.fromJson(json,type);
        return arrayList;
    }

    public interface View extends IBaseView {
        void onMusicSucess();

        void onMusicFailed(String errorMsg);
    }
}
