package com.example.demo.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo.Adapter.MusicAdapter;
import com.example.demo.Bean.Music;
import com.example.demo.Presenter.MusicInfoPresenter;
import com.example.demo.R;

import java.util.ArrayList;
import java.util.List;

public class MusicFragment extends BasePresenterFragment<MusicInfoPresenter,MusicInfoPresenter.View> implements MusicInfoPresenter.View {

    private View view;
    private List<Music> mMusicList;
    private RecyclerView recyclerView;
    private MusicAdapter musicAdapter;

    @Override
    protected MusicInfoPresenter initPresenter(){
        return new MusicInfoPresenter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMusicList = new ArrayList<>();
        initData();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        musicAdapter = new MusicAdapter(mMusicList);
        recyclerView.setAdapter(musicAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_music,container,false);
        return view;
    }

    private void initData(){
        Music mike = new Music("Mike",R.mipmap.ic_launcher);
        Music john = new Music("John",R.mipmap.ic_launcher);
        Music jason = new Music("Jason",R.mipmap.ic_launcher);
        mMusicList.add(mike);
        mMusicList.add(john);
        mMusicList.add(jason);

        //SharedPreferences.Editor editor = getContext().getSharedPreferences("data", Context.MODE_PRIVATE).edit();
    }

    @Override
    public void onMusicSucess(){

    }

    @Override
    public void onMusicFailed(String errorMsg){

    }

    @Override
    public void showLoadingDialog() {
        //appDialog.showLoadingDialog();
    }

    @Override
    public void hideLoadingDialog() {
        //appDialog.dismiss();
    }

    @Override
    public void showToast(String message){

    }
}
