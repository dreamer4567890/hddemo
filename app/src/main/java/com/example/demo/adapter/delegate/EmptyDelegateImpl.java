package com.example.demo.adapter.delegate;

import android.view.View;

import com.example.demo.R;
import com.example.demo.adapter.common.base.ViewHolder;
import com.example.demo.adapter.common.base.XItemViewDelegate;
import com.example.demo.bean.DiffBean;

import static com.example.demo.adapter.DiffAdapter.VIEW_TYPE_EMPTY;

public class EmptyDelegateImpl extends XItemViewDelegate<DiffBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.empty_item;
    }

    @Override
    public boolean isForViewType(DiffBean item, int position) {
        return item != null && item.getType() == VIEW_TYPE_EMPTY;
    }

    @Override
    public void convert(final ViewHolder holder, DiffBean diffBean, int position) {

    }
}
