package com.example.demo.presenter;

import android.widget.Toast;

import com.example.demo.adapter.SlideAdapter;
import com.example.demo.mvp.BasePresenter;
import com.example.demo.mvp.IBaseView;

import java.util.List;

public class SlidePresenter extends BasePresenter<IBaseView> {

    public void addItem(SlideAdapter mAdapter, String position){
        mAdapter.getmDatas().add(position);
    }

    public void deleteItem(SlideAdapter mAdapter, int position){
        mAdapter.removeData(position);
    }
}
