package com.example.demo.widget.draglayout;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 触发down事件直接开启拖拽
 */

public abstract class OnRecyclerItemTouchListener implements RecyclerView.OnItemTouchListener {
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView mRecyclerView;
    private boolean mIsEditModel;

    public void setEditModel(boolean editModel) {
        mIsEditModel = editModel;
    }

    public OnRecyclerItemTouchListener(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        mGestureDetector = new GestureDetectorCompat(mRecyclerView.getContext(), new ItemTouchHelperGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (mIsEditModel) {
            mGestureDetector.onTouchEvent(e);
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public void onShowPress(MotionEvent e) {
            if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) {
                // recyclerview 滑动过程中就不给响应长按了，因为滑动过程中，item一直在变化啊，最后确定下来的item都可能不是你想要点击的那个
                return;
            }

            View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder vh = mRecyclerView.getChildViewHolder(child);
                OnRecyclerItemTouchListener.this.onShowPress(vh, vh.getAdapterPosition());
            }
        }

    }

    public abstract void onShowPress(RecyclerView.ViewHolder vh, int pos);

}