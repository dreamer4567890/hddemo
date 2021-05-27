package com.example.demo.adapter.delegate;

import android.view.View;

import com.example.demo.R;
import com.example.demo.adapter.common.base.ViewHolder;
import com.example.demo.adapter.common.base.XItemViewDelegate;
import com.example.demo.bean.DiffBean;

import static com.example.demo.adapter.DiffAdapter.VIEW_TYPE_H;
import static com.example.demo.adapter.DiffAdapter.VIEW_TYPE_V;

public class VDelegateImpl extends XItemViewDelegate<DiffBean> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.v_item;
    }

    @Override
    public boolean isForViewType(DiffBean item, int position) {
        return item.getType() == VIEW_TYPE_V;
    }

    @Override
    public void convert(final ViewHolder holder, DiffBean diffBean, int position) {
        if (diffBean==null){
            return;
        }
        holder.setText(R.id.name, diffBean.getName());
        holder.setText(R.id.value, diffBean.getValue());
        holder.setText(R.id.title, diffBean.getTitle());
        holder.setOnClickListener(R.id.item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.getView(R.id.point_item).performClick();
            }
        });
    }
}
