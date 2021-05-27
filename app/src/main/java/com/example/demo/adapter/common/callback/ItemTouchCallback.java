package com.example.demo.adapter.common.callback;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.demo.R;

import io.reactivex.annotations.NonNull;

public class ItemTouchCallback extends ItemTouchHelper.Callback {
    public static final int KEY_DRAG = R.id.support_item_drag;
    private boolean mIsEnabledDrag;
    private OnItemDragListener mOnItemDragListener;

    public ItemTouchCallback(OnItemDragListener mOnItemDragListener) {
        this.mOnItemDragListener = mOnItemDragListener;
    }

    public void setEnabledDrag(boolean isEnabledDrag) {
        mIsEnabledDrag = isEnabledDrag;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        //位置发生改变
        mOnItemDragListener.onItemDragMoving(viewHolder, viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }


    @Override
    public boolean isLongPressDragEnabled() {
        return mIsEnabledDrag;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            mOnItemDragListener.onItemDragStart(viewHolder, viewHolder.getAdapterPosition());
            viewHolder.itemView.setTag(KEY_DRAG, true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder.itemView.getTag(KEY_DRAG) != null && (Boolean) viewHolder.itemView.getTag(KEY_DRAG)) {
            mOnItemDragListener.onItemDragEnd(viewHolder);
            viewHolder.itemView.setTag(KEY_DRAG, false);
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }
}