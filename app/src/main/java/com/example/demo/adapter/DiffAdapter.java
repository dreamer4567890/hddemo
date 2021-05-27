package com.example.demo.adapter;

import android.content.Context;

import com.example.demo.R;
import com.example.demo.adapter.common.MultiItemTypeAdapter;
import com.example.demo.adapter.delegate.HDelegateImpl;
import com.example.demo.adapter.delegate.VDelegateImpl;
import com.example.demo.bean.DiffBean;

import java.util.List;

public class DiffAdapter extends MultiItemTypeAdapter<DiffBean> {
    public static final int VIEW_TYPE_H = 1;
    public static final int VIEW_TYPE_V = 2;

    protected Context mContext;

    public DiffAdapter(Context context, List<DiffBean> data) {
        super(context, data);
        mContext = context;
        addItemViewDelegate(VIEW_TYPE_H, new HDelegateImpl());
        addItemViewDelegate(VIEW_TYPE_V, new VDelegateImpl());
        addChildClickViewIds(R.id.point_item);
    }
}
