package com.example.demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo.R;
import com.example.demo.adapter.common.base.ViewHolder;
import com.example.demo.bean.DiffBean;
import com.example.demo.bean.DiffInfoBean;
import com.example.demo.diff.DiffBeanDiffCallBack;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.diff.DiffBeanDiffCallBack.DIFF_DEFAULT;

public class DiffAsyncAdapter extends DiffAdapter {

    private List<DiffBean> mList;
    private AsyncListDiffer<DiffBean> mDiffer;

    public DiffAsyncAdapter(Context context, List<DiffBean> data) {
        super(context, data);
        mDiffer = new AsyncListDiffer<>(this, new DiffBeanDiffCallBack());
        mDiffer.submitList(data);
        setNewData(data);
    }

    /**
     * 异步刷新时，该方法返回的不是最新的数据
     * 需使用DeviceGroupsHelper.getNewDatas()
     *
     * @return UnmodifiableList 只可读，不可修改。若要修改调用getNewData()
     */
    @Override
    public List<DiffBean> getData() {
        if (mDiffer == null) {
            return super.getData();
        }
        return mDiffer.getCurrentList();
    }

    public void submitList(List<DiffBean> data) {
        mDiffer.submitList(data);
        setNewData(data);
    }

    @Override
    public void setNewData(@Nullable List<DiffBean> data) {
        this.mList = data;
    }

    /**
     * 异步刷新时，该方法返回最新的数据
     * 可修改，且不会触发差量对比
     *
     * @return
     */
    public List<DiffBean> getNewData() {
        return mList;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            DiffInfoBean diffInfoBean = (DiffInfoBean) payloads.get(0);
            DiffBean diffBean = getItem(position - getHeadersCount());
            if (diffBean == null) {
                Log.e(TAG, "onBindViewHolder diffBean is null");
                return;
            }
            ViewHolder viewHolder = (ViewHolder) holder;
            switch (diffInfoBean.getDiffTag()) {
                case DIFF_DEFAULT:
                    //默认刷新整个item
                    super.onBindViewHolder(viewHolder, position);
                    break;
                case DiffBeanDiffCallBack.DIFF_NAME:
                    viewHolder.setText(R.id.name, diffBean.getName());
                    break;
                case DiffBeanDiffCallBack.DIFF_VALUE:
                    viewHolder.setText(R.id.value, diffBean.getValue());
                    break;
                case DiffBeanDiffCallBack.DIFF_TITLE:
                    viewHolder.setText(R.id.title, diffBean.getTitle());
                    break;
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public DiffBean remove(int position) {
        DiffBean bean = null;
        if (position >= 0 && position < mList.size()) {
            bean = mList.remove(position);
            int internalPosition = position + getHeadersCount();
            notifyItemRemoved(internalPosition);
            notifyItemRangeChanged(internalPosition, mList.size() - internalPosition);
        }
        return bean;
    }

    @Override
    public void addData(@NonNull List<DiffBean> newData) {
        List<DiffBean> list = new ArrayList<>(getData());
        list.addAll(newData);
        submitList(list);
    }

    @Override
    public void addData(int position, @NonNull List<DiffBean> newData) {
        List<DiffBean> list = new ArrayList<>(getData());
        list.addAll(position, newData);
        submitList(list);
    }

    /**
     * 手动差量刷新，不会触发差量对比
     * 且避免重复创建ViewHolder
     *
     * @param position
     */
    public void notifyItemDiffChanged(int position, int diffType) {
        notifyItemChanged(position, new DiffInfoBean(diffType));
    }

    public void notifyItemDiffChanged(int position) {
        notifyItemDiffChanged(position, DIFF_DEFAULT);
    }

}
