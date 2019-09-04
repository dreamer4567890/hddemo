package com.example.demo.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.demo.adapter.MovieAdapter;
import com.example.demo.bean.Movie;
import com.example.demo.presenter.MovieInfoPresenter;
import com.example.demo.R;
import com.example.demo.widget.RefreshLoadMoreRecycleView;

import java.util.ArrayList;
import java.util.List;

public class MovieFragment extends BasePresenterFragment<MovieInfoPresenter,MovieInfoPresenter.View> implements MovieInfoPresenter.View,RefreshLoadMoreRecycleView.IOnScrollListener {

    private View view;
    private List<Movie> mMovieList;
    private RefreshLoadMoreRecycleView recycleView;
    private MovieAdapter movieAdapter;
    private ProgressDialog dialog;//提示框
    private static final int REFRESH_LOAD = 0;//下拉刷新
    private static final int MORE_LOAD = 1;//加载更多

    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESH_LOAD:
                    recycleView.setLoadMoreEnable(true);
                    hideLoadingDialog();
                    if(mMovieList!=null){
                        mMovieList.clear();
                    }
                    loadData();
                    movieAdapter.notifyDataSetChanged();
                    break;
                case MORE_LOAD:
                    recycleView.setLoadMoreEnable(false);
                    hideLoadingDialog();
                    loadData();
                    movieAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected MovieInfoPresenter initPresenter(){
        return new MovieInfoPresenter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMovieList = new ArrayList<>();
        initData();
        recycleView = (RefreshLoadMoreRecycleView) getActivity().findViewById(R.id.recyclerView);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),4);
        recycleView.setLayoutManager(layoutManager);
        movieAdapter = new MovieAdapter(mMovieList);
        recycleView.setAdapter(movieAdapter);
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
        dialog = new ProgressDialog(getContext());
        Movie mike = new Movie("Mike",R.mipmap.ic_launcher);
        Movie john = new Movie("John",R.mipmap.ic_launcher);
        Movie jason = new Movie("Jason",R.mipmap.ic_launcher);
        for(int i = 0;i < 5; i++){
            mMovieList.add(mike);
            mMovieList.add(john);
            mMovieList.add(jason);
        }

        mPresenter.putMovieInfo(getContext(),mMovieList);
    }

    @Override
    public void onMovieSucess(){
        showToast("加载成功");
    }

    @Override
    public void onMovieFailed(String errorMsg){
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

    /**
     * 加载数据
     */
    public void loadData(){
        mMovieList = mPresenter.getMovieInfo(getContext());
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
        Toast.makeText(getActivity(),"Loaded all", Toast.LENGTH_SHORT).show();
    }
}
