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
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//设置布局管理器
        mRecyclerView.setAdapter(mAdapter = new SlideAdapter(this));//设置适配器
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置控制Item增删的动画
    }

    /**
     * item正文的点击事件
     *
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        //点击item正文的代码逻辑
    }


    /**
     * item的左滑设置
     *
     * @param view
     * @param position
     */
    @Override
    public void onSetBtnCilck(View view, int position) {

        //“设置”点击事件的代码逻辑
        Toast.makeText(SlideActivity.this, "请设置", Toast.LENGTH_LONG).show();
    }


    /**
     * item的左滑删除
     *
     * @param view
     * @param position
     */
    @Override
    public void onDeleteBtnCilck(View view, int position) {
        mAdapter.removeData(position);
    }

}
