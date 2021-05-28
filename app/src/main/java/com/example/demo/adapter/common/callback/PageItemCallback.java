package com.example.demo.adapter.common.callback;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import io.reactivex.annotations.NonNull;

/**
 * 第一个不给移动呗
 */
public class PageItemCallback extends ItemTouchCallback {

    public PageItemCallback(OnItemDragListener mOnItemDragListener) {
        super(mOnItemDragListener);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getAdapterPosition() == 0) {
            return 0;
        }

        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        if (target.getAdapterPosition() == 0) {
            return false;
        }

        return super.onMove(recyclerView, viewHolder, target);
    }
}
