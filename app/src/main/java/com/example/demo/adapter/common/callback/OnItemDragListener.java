package com.example.demo.adapter.common.callback;

import android.support.v7.widget.RecyclerView;

public interface OnItemDragListener {
    void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos);


    void onItemDragMoving(RecyclerView.ViewHolder source, int from, int to);


    void onItemDragEnd(RecyclerView.ViewHolder viewHolder);
}