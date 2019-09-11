package com.example.demo.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.demo.activity.MainActivity;
import com.example.demo.adapter.MusicAdapter;
import com.example.demo.bean.Music;
import com.example.demo.presenter.MusicInfoPresenter;
import com.example.demo.R;
import com.example.demo.widget.RefreshLoadMoreRecycleView;

import java.util.ArrayList;
import java.util.List;

public class MusicFragment extends BasePresenterFragment<MusicInfoPresenter,MusicInfoPresenter.View> implements MusicInfoPresenter.View,RefreshLoadMoreRecycleView.IOnScrollListener {

    private View view;
    private List<Music> mMusicList;
    private RefreshLoadMoreRecycleView recycleView;
    private MusicAdapter musicAdapter;
    private ProgressDialog dialog;//提示框
    private static final int REFRESH_LOAD = 0;//下拉刷新
    private static final int MORE_LOAD = 1;//加载更多

    String wade = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1567573436014&di=8b7e693380bb766caff8a8038eb37594&imgtype=0&src=http%3A%2F%2F02.imgmini.eastday.com%2Fmobile%2F20180613%2F1254485964e3efed7cf49afa51349d49_wmk.jpeg";

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESH_LOAD:
                    recycleView.setLoadMoreEnable(true);
                    hideLoadingDialog();
                    if(mMusicList!=null){
                        mMusicList.clear();
                    }
                    initData();
                    musicAdapter.notifyDataSetChanged();
                    break;
                case MORE_LOAD:
                    recycleView.setLoadMoreEnable(false);
                    hideLoadingDialog();
                    loadData();
                    musicAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected MusicInfoPresenter initPresenter(){
        return new MusicInfoPresenter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMusicList = new ArrayList<>();
        dialog = new ProgressDialog(getContext());
        initData();
        storage();
        recycleView = (RefreshLoadMoreRecycleView) getActivity().findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(layoutManager);
        musicAdapter = new MusicAdapter(mMusicList);
        recycleView.setAdapter(musicAdapter);
        recycleView.setListener(this);
        recycleView.setRefreshEnable(true);
        recycleView.setLoadMoreEnable(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_music,container,false);
        return view;
    }

    private void initData(){
        Music mike = new Music("Mike",wade);
        Music john = new Music("John",wade);
        Music jason = new Music("Jason",wade);
        for(int i = 0;i < 5; i++){
            mMusicList.add(mike);
            mMusicList.add(john);
            mMusicList.add(jason);
        }

    }

    @Override
    public void onMusicSuccess(){
        showToast("加载成功");
    }

    @Override
    public void onMusicFailed(String errorMsg){
        showToast((TextUtils.isEmpty(errorMsg) ? "加载失败" : errorMsg));
    }

    @Override
    public void showLoadingDialog() {
        if (dialog!=null&&!dialog.isShowing()){
            dialog.show();
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    public void storage(){
        mPresenter.putMusicInfo(getContext(),mMusicList);
    }

    public void loadData(){
        mMusicList = mPresenter.getMusicInfo(getContext());
    }

    @Override
    public void onRefresh() {
        showLoadingDialog();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(5000);
                    handler.sendEmptyMessage(REFRESH_LOAD);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onLoadMore() {
        showLoadingDialog();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(5000);
                    handler.sendEmptyMessage(MORE_LOAD);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onLoaded() {
        Toast.makeText(getActivity(),"加载完毕", Toast.LENGTH_SHORT).show();
    }

}
