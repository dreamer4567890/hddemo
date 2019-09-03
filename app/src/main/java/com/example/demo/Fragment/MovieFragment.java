package com.example.demo.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo.Adapter.MovieAdapter;
import com.example.demo.Bean.Movie;
import com.example.demo.Presenter.MovieInfoPresenter;
import com.example.demo.R;

import java.util.ArrayList;
import java.util.List;

public class MovieFragment extends BasePresenterFragment<MovieInfoPresenter,MovieInfoPresenter.View> implements MovieInfoPresenter.View {

    private View view;
    private List<Movie> mMovieList;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    @Override
    protected MovieInfoPresenter initPresenter(){
        return new MovieInfoPresenter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMovieList = new ArrayList<>();
        initData();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),4);
        recyclerView.setLayoutManager(layoutManager);
        movieAdapter = new MovieAdapter(mMovieList);
        recyclerView.setAdapter(movieAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_movie,container,false);
        return view;
    }

    private void initData(){
        Movie mike = new Movie("Mike",R.mipmap.ic_launcher);
        Movie john = new Movie("John",R.mipmap.ic_launcher);
        Movie jason = new Movie("Jason",R.mipmap.ic_launcher);
        mMovieList.add(mike);
        mMovieList.add(john);
        mMovieList.add(jason);
        mMovieList.add(mike);
        mMovieList.add(john);
        mMovieList.add(jason);
    }

    @Override
    public void onMovieSucess(){

    }

    @Override
    public void onMovieFailed(String errorMsg){

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
