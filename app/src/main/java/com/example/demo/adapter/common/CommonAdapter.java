package com.example.demo.adapter.common;

import android.content.Context;

import com.example.demo.adapter.common.base.ItemViewDelegate;
import com.example.demo.adapter.common.base.ViewHolder;

import java.util.List;


public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {

    public CommonAdapter(final Context context, final int layoutId) {
        this(context, layoutId, null);
    }

    public CommonAdapter(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);
}
