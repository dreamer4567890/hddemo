package com.example.demo.diff;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.text.TextUtils;

import com.example.demo.bean.DiffBean;
import com.example.demo.bean.DiffInfoBean;

import java.util.ArrayList;
import java.util.List;

public class DiffBeanDiffCallBack extends DiffUtil.ItemCallback<DiffBean> {
    public static final int DIFF_DEFAULT = 0;       //item 默认刷新
    public static final int DIFF_NAME = 1;
    public static final int DIFF_VALUE = 2;
    public static final int DIFF_TITLE = 3;

    private List<Integer> mChangeList;

    public DiffBeanDiffCallBack() {
        mChangeList = new ArrayList<>();
    }

    /**
     * 判断两个条目是否是一致的
     */
    @Override
    public boolean areItemsTheSame(@NonNull DiffBean oldItem, @NonNull DiffBean newItem) {
        return oldItem.getId() == newItem.getId();
    }

    /**
     * 这个需要areItemsTheSame 返回true时才调用
     * 对比新旧数据里面的具体字段
     */
    @Override
    public boolean areContentsTheSame(@NonNull DiffBean oldItem, @NonNull DiffBean newItem) {
        return TextUtils.equals(oldItem.getName(), newItem.getName()) &&
                TextUtils.equals(oldItem.getValue(), newItem.getValue()) &&
                TextUtils.equals(oldItem.getTitle(), newItem.getTitle());
    }

    /**
     * 需要areItemsTheSame()返回true，areContentsTheSame()返回false，
     * 虽然是同一条数据，但是我们也有不同的
     */
    @Nullable
    @Override
    public DiffInfoBean getChangePayload(@NonNull DiffBean oldItem, @NonNull DiffBean newItem) {
        mChangeList.clear();
        if (!TextUtils.equals(oldItem.getName(), newItem.getName())) {
            mChangeList.add(DIFF_NAME);
        }
        if (!TextUtils.equals(oldItem.getValue(), newItem.getValue())) {
            mChangeList.add(DIFF_VALUE);
        }
        if (!TextUtils.equals(oldItem.getTitle(), newItem.getTitle())) {
            mChangeList.add(DIFF_TITLE);
        }
        if (mChangeList.size() == 1) {
            return new DiffInfoBean(mChangeList.get(0));
        }
        return new DiffInfoBean(DIFF_DEFAULT);
    }
}
