package com.example.demo.adapter.common;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo.adapter.common.base.ItemViewDelegateManager;
import com.example.demo.adapter.common.base.ViewHolder;
import com.example.demo.adapter.common.callback.ItemTouchCallback;
import com.example.demo.adapter.common.base.ItemViewDelegate;
import com.example.demo.adapter.common.callback.OnItemDragListener;
import com.example.demo.adapter.common.base.XItemViewDelegate;
import com.example.demo.utils.FastClickUtil;
import com.example.demo.utils.OnMultiClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;


public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnItemDragListener {
    public static final String TAG = MultiItemTypeAdapter.class.getSimpleName();
    protected static final int TYPE_HEADER = 1001;
    protected static final int TYPE_FOOTER = 1002;
    protected Context mContext;
    protected List<T> mData;
    protected ItemViewDelegateManager mItemViewDelegateManager;
    protected OnItemClickListener mOnItemClickListener;
    private OnItemDragListener mOnItemDragListener;
    protected OnItemChildClickListener mOnItemChildClickListener;
    private ItemTouchCallback mItemTouchCallback;
    private RecyclerView mRecyclerView;
    private LinkedHashSet<Integer> mChildClickViewIds;
    private View mHeaderView;
    private View mFooterView;
    private boolean mEnableDrag;

    protected ItemTouchHelper mItemTouchHelper;

    public MultiItemTypeAdapter(Context context, List<T> data) {
        mContext = context;
        this.mData = data == null ? new ArrayList<T>() : data;
        mItemViewDelegateManager = new ItemViewDelegateManager();
        mChildClickViewIds = new LinkedHashSet<>();
    }

    @Override
    public int getItemViewType(int position) {
        //header
        if (isHeaderType(position)) {
            return TYPE_HEADER;
        }

        if (getFootersCount() != 0 && position == getItemCount() - 1) {
            //Footer类型
            return TYPE_FOOTER;
        }

        // 根据位置的索引，获取当前position的类型
        if (getItemCount() > 0 && position < getItemCount() && position >= 0) {
            return mItemViewDelegateManager.getItemViewType(getItem(position - getHeadersCount())
                    , position);
        }
        return super.getItemViewType(position);
    }

    public boolean isHeaderType(int position) {
        return position < getHeadersCount();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            //在重用mHeaderView视图前，先将mHeaderView从之前对父布局中释放出来
            ViewGroup headerParent = (ViewGroup) mHeaderView.getParent();
            if (headerParent != null) {
                headerParent.removeView(mHeaderView);
            }
            return ViewHolder.createViewHolder(mContext, mHeaderView);
        }

        if (viewType == TYPE_FOOTER) {
            ViewGroup footerParent = (ViewGroup) mFooterView.getParent();
            if (footerParent != null) {
                footerParent.removeView(mFooterView);
            }
            return ViewHolder.createViewHolder(mContext, mFooterView);
        }

        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder holder = ViewHolder.createViewHolder(mContext, parent, layoutId);
        if (itemViewDelegate instanceof XItemViewDelegate) {
            ((XItemViewDelegate) itemViewDelegate).onCreateViewHolder(holder, viewType);
        }
        setListener(parent, holder, viewType);
        return holder;
    }

    public void convert(ViewHolder holder, T t) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition());
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }


    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new OnMultiClickListener(500) {
            @Override
            public void onMultiClick(View view) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition() - getHeadersCount();
                    mOnItemClickListener.onItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition() - getHeadersCount();
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });

        for (int id : getChildClickViewIds()) {
            View childView = viewHolder.itemView.findViewById(id);
            if (childView != null) {
                childView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnItemChildClickListener != null) {
                            int position = viewHolder.getAdapterPosition() - getHeadersCount();
                            mOnItemChildClickListener.onItemChildClick(view, viewHolder, position);
                        }
                    }
                });
            }
        }
    }

    public void bindRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setItemTouchCallback(ItemTouchCallback itemTouchCallback) {
        mItemTouchCallback = itemTouchCallback;
    }

    public void enableDragItem(boolean isEnable) {
        if (mItemTouchCallback == null) {
            mItemTouchCallback = new ItemTouchCallback(this);
        }
        mItemTouchCallback.setEnabledDrag(isEnable);
        mEnableDrag = isEnable;
        if (isEnable) {
            if (mItemTouchHelper == null) {
                mItemTouchHelper = new ItemTouchHelper(mItemTouchCallback);
            }
            if (mRecyclerView != null) {
                mItemTouchHelper.attachToRecyclerView(mRecyclerView);
            } else {
                Log.e(TAG, "enableDrag() must bindRecyclerView() first!");
            }
        } else {
            if (mItemTouchHelper != null) {
                mItemTouchHelper.attachToRecyclerView(null);
            }
        }
    }

    public boolean isEnableDrag() {
        return mEnableDrag;
    }

    public void setOnItemDragListener(OnItemDragListener onItemDragListener) {
        mOnItemDragListener = onItemDragListener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isHeaderOrFooter(position)) {
            return;
        }
        convert((ViewHolder) holder, getItem(position - getHeadersCount()));
    }

    @Override
    public int getItemCount() {
        int otherCounts = getHeadersCount() + getFootersCount();
        return getData() == null ? otherCounts : getData().size() + otherCounts;
    }

    public List<T> getData() {
        return mData;
    }

    public T getItem(int pos) {
        if (pos >= 0 && pos < getData().size()) {
            return getData().get(pos);
        } else {
            return null;
        }
    }

    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiItemTypeAdapter addItemViewDelegate(int viewType, ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    @Override
    public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
        if (mOnItemDragListener != null) {
            mOnItemDragListener.onItemDragStart(viewHolder, pos);
        }
    }

    @Override
    public void onItemDragMoving(RecyclerView.ViewHolder viewHolder, int from, int to) {
        Collections.swap(getData(), from, to);
        notifyItemMoved(from, to);
        if (mOnItemDragListener != null) {
            mOnItemDragListener.onItemDragMoving(viewHolder, from, to);
        }
    }

    @Override
    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemDragListener != null) {
            mOnItemDragListener.onItemDragEnd(viewHolder);
        }
    }

    public abstract static class OnItemClickListener {
        public abstract void onItemClick(View view, ViewHolder holder, int position);

        public boolean onItemLongClick(View view, ViewHolder holder, int position) {
            return false;
        }
    }

    public interface OnItemChildClickListener {
        void onItemChildClick(View view, ViewHolder holder, int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public int getTypeSize() {
        return mItemViewDelegateManager.getItemViewDelegateCount();
    }

    /**
     * 添加头部视图
     *
     * @param header
     */
    public void addHeaderView(View header) {
        this.mHeaderView = header;
    }

    /**
     * 添加底部视图
     *
     * @param header
     */
    public void addFooterView(View header) {
        this.mFooterView = header;
    }

    public void addData(@IntRange(from = 0) int position, @NonNull T data) {
        notifyItemInserted(position + getHeadersCount());
        getData().add(position, data);
        compatibilityDataSizeChanged(1);
    }

    public void addData(@NonNull T data) {
        notifyItemInserted(this.getData().size() + getHeadersCount());
        getData().add(data);
        compatibilityDataSizeChanged(1);
    }

    public void addData(@IntRange(from = 0) int position, @NonNull List<T> newData) {
        notifyItemRangeInserted(position + getHeadersCount(), newData.size());
        getData().addAll(position, newData);
        compatibilityDataSizeChanged(newData.size());
    }

    public void addData(@NonNull List<T> newData) {
        getData().addAll(newData);
        notifyItemRangeInserted(getData().size() - newData.size() + getHeadersCount(), newData.size());
        compatibilityDataSizeChanged(newData.size());
    }

    private void compatibilityDataSizeChanged(int size) {
        final int dataSize = getData() == null ? 0 : getItemCount();
        if (dataSize == size) {
            notifyDataSetChanged();
        }
    }

    public void setData(@IntRange(from = 0) int index, @NonNull T data) {
        getData().set(index, data);
        notifyItemChanged(index + getHeadersCount());
    }

    public void setNewData(@Nullable List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
        notifyDataSetChanged();
    }

    public T remove(@IntRange(from = 0) int position) {
        T t = null;
        if (position >= 0 && position < getData().size()) {
            t = getData().remove(position);
            int internalPosition = position + getHeadersCount();
            notifyItemRemoved(internalPosition);
            compatibilityDataSizeChanged(0);
            notifyItemRangeChanged(internalPosition, getData().size() - internalPosition);
        }
        return t;
    }

    public int getHeadersCount() {
        return mHeaderView == null ? 0 : 1;
    }

    public int getFootersCount() {
        return mFooterView == null ? 0 : 1;
    }

    public View getViewByPosition(int pos, int viewId) {
        if (mRecyclerView != null) {
            ViewHolder holder = (ViewHolder) mRecyclerView.findViewHolderForLayoutPosition(pos + getHeadersCount());
            if (holder != null) {
                return holder.itemView.findViewById(viewId);
            }
        } else {
            Log.e(TAG, "getViewByPosition must bindRecyclerView() first!");
        }
        return null;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return isHeaderOrFooter(position) ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }


    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof ViewHolder) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeaderOrFooter(holder.getAdapterPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.mOnItemChildClickListener = onItemChildClickListener;
    }

    //设置需要点击事件的子view
    public void addChildClickViewIds(int... viewIds) {
        for (int viewId : viewIds) {
            mChildClickViewIds.add(viewId);
        }
    }

    public LinkedHashSet<Integer> getChildClickViewIds() {
        return mChildClickViewIds;
    }

    public boolean isHeaderOrFooter(int position) {
        return getItemViewType(position) == TYPE_HEADER ||
                getItemViewType(position) == TYPE_FOOTER;
    }

    public int getHeaderAndFooterCount() {
        return getHeadersCount() + getFootersCount();
    }
}
