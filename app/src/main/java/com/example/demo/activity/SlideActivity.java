package com.example.demo.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.adapter.SlideAdapter;
import com.example.demo.mvp.IBaseView;
import com.example.demo.presenter.SlidePresenter;

public class SlideActivity extends BasePresenterActivity<SlidePresenter, IBaseView> implements IBaseView, SlideAdapter.IonSlidingViewClickListener {

    private RecyclerView mRecyclerView;
    private SlideAdapter mAdapter;


    @Override
    protected int getLayoutId(){
        return R.layout.activity_contacts;
    }

    @Override
    protected SlidePresenter initPresenter(){
        return new SlidePresenter();
    }

    @Override
    protected void initData(){
        setMyActionBar("SlideLayout",false);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new SlideAdapter(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置控制Item增删的动画
    }

    @Override
    public void onItemClick(View view, int position) {
        showToast(mAdapter.getData(position));
    }


    @Override
    public void onSetAddClick(View view, int position) {
        mPresenter.addItem(mAdapter,position);
        if(mAdapter.getItemCount()>0){
            showToast("成功添加第" + (position + 1) + "条数据");
        }
    }

    @Override
    public void onDeleteBtnClick(View view, int position) {
        mPresenter.deleteItem(mAdapter, position);
        if(mAdapter.getItemCount()>0){
            showToast("成功删除第" + (position + 1) + "条数据");
        }
    }

}
