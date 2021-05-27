package com.example.demo.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.demo.R;
import com.example.demo.adapter.DiffAdapter;
import com.example.demo.adapter.DiffAsyncAdapter;
import com.example.demo.adapter.common.MultiItemTypeAdapter;
import com.example.demo.adapter.common.base.ViewHolder;
import com.example.demo.bean.DiffBean;
import com.example.demo.diff.DiffBeanDiffCallBack;
import com.example.demo.utils.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class DiffActivity extends BaseUiActivity {
    private RecyclerView mRecyclerView;
    private DiffAsyncAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.diff_activity;
    }

    @Override
    protected void initData() {
        super.initData();
        setMyActionBar("DiffActivity");
        mRecyclerView = findViewById(R.id.recyclerView);
        mAdapter = new DiffAsyncAdapter(this, new ArrayList<DiffBean>());
        mRecyclerView.setFocusableInTouchMode(false);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter.bindRecyclerView(mRecyclerView);
        mAdapter.setOnItemChildClickListener(new MultiItemTypeAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(View view, ViewHolder holder, int position) {
                DiffBean diffBean = mAdapter.getItem(position);
                if (diffBean == null) {
                    return;
                }
                List<DiffBean> list = deepCopyList(mAdapter.getData());
                if (view.getId() == R.id.point_item) {
                    if (diffBean.getType() == DiffAdapter.VIEW_TYPE_H) {
                        diffBean.setName(Integer.parseInt(diffBean.getName()) * 2 + "");
                        list.set(position, diffBean);
                        mAdapter.submitList(list);
                        mAdapter.notifyItemDiffChanged(position, DiffBeanDiffCallBack.DIFF_NAME);
                    } else {
                        diffBean.setValue(Integer.parseInt(diffBean.getValue()) * 2 + "");
                        list.set(position, diffBean);
                        mAdapter.submitList(list);
                        mAdapter.notifyItemDiffChanged(position, DiffBeanDiffCallBack.DIFF_VALUE);
                    }
                }
            }
        });
        setData();
    }

    private void setData() {
        List<DiffBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new DiffBean(i, i + "", i + "", i + "",
                    i < 5 ? DiffAdapter.VIEW_TYPE_H : DiffAdapter.VIEW_TYPE_V));
        }
        mAdapter.submitList(list);
    }

    //深度复制list
    public List<DiffBean> deepCopyList(List<DiffBean> oldList) {
        String json = new Gson().toJson(oldList);
        TypeToken<List<DiffBean>> typeToken = new TypeToken<List<DiffBean>>() {
        };
        return GsonUtil.jsonToListObject(json, typeToken);
    }
}
