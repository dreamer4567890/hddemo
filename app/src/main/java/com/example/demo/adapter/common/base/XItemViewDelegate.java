package com.example.demo.adapter.common.base;


public abstract class XItemViewDelegate<T> implements ItemViewDelegate<T> {

    /**
     * 只初始化一次
     *
     * @param holder
     * @param viewType
     */
    public void onCreateViewHolder(ViewHolder holder, int viewType) {

    }

}
