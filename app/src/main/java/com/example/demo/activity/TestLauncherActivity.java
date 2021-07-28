package com.example.demo.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.demo.R;
import com.example.demo.adapter.DiffAdapter;
import com.example.demo.adapter.DiffAsyncAdapter;
import com.example.demo.adapter.common.MultiItemTypeAdapter;
import com.example.demo.adapter.common.base.ViewHolder;
import com.example.demo.bean.DiffBean;
import com.example.demo.diff.DiffBeanDiffCallBack;
import com.example.demo.utils.GsonUtil;
import com.example.demo.utils.ScreenUtils;
import com.example.demo.widget.XMaxHeightRecyclerView;
import com.example.demo.widget.draglayout.NewLauncherView;
import com.example.demo.widget.draglayout.OnRecyclerItemTouchListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.demo.adapter.DiffAdapter.VIEW_TYPE_EMPTY;

public class TestLauncherActivity extends BaseUiActivity {
    private XMaxHeightRecyclerView mRecyclerView;
    protected NewLauncherView mLauncherView;
    private DiffAsyncAdapter mAdapter;
    private OnRecyclerItemTouchListener mOnRecyclerItemTouchListener;

    @Override
    protected int getLayoutId() {
        return R.layout.test_launcher_activity;
    }

    @Override
    protected void initData() {
        super.initData();
        mRecyclerView = findViewById(R.id.recycler_view);
        mLauncherView = findViewById(R.id.home_fragment_root);
        mLauncherView.setDragListener(new LauncherDragListener());

        mOnRecyclerItemTouchListener = new OnRecyclerItemTouchListener(mRecyclerView) {
            @Override
            public void onShowPress(RecyclerView.ViewHolder vh, int pos) {
                if (pos < 0) {
                    return;
                }
                int index = pos - mAdapter.getHeadersCount();
                if (index > 0 && index < mAdapter.getData().size() && mAdapter.getItem(index).isEmpty()) {
                    return;
                }

                int scrollState = mRecyclerView.getScrollState();
                if (scrollState != RecyclerView.SCROLL_STATE_IDLE) {
                    Log.e(TAG, "RecyclerView   is not idle ,can not perform click event");
                    return;
                }
                if (index < mAdapter.getData().size() && index >= 0) {
                    RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(pos);
                    if (viewHolder != null) {
                        View view = mRecyclerView.findViewHolderForAdapterPosition(pos).itemView;
                        view.clearAnimation();
                        mLauncherView.setBottomOffset(ScreenUtils.dpToPx(0));
                        mLauncherView.startDrag(view, pos, false, 0, mRecyclerView);
                    } else {
                        Log.e(TAG, "onItemLongClick viewHolder is null!!!");
                    }
                }
            }
        };
        mOnRecyclerItemTouchListener.setEditModel(true);
        mRecyclerView.addOnItemTouchListener(mOnRecyclerItemTouchListener);
        mAdapter = new DiffAsyncAdapter(this, new ArrayList<DiffBean>());
        mRecyclerView.setFocusableInTouchMode(false);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter.bindRecyclerView(mRecyclerView);
//        mAdapter.enableDragItem(true);
        mAdapter.setOnItemChildClickListener(new MultiItemTypeAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(View view, ViewHolder holder, int position) {
                DiffBean diffBean = mAdapter.getItem(position);
                if (diffBean == null) {
                    return;
                }
                List<DiffBean> list = deepCopyList(mAdapter.getData());
                if (view.getId() == R.id.point_item) {
                    if (diffBean.getType() == DiffAdapter.VIEW_TYPE_H) {
                        diffBean.setName(Integer.parseInt(diffBean.getName()) * 2 + "");
                        list.set(position, diffBean);
                        mAdapter.submitList(list);
                        mAdapter.notifyItemDiffChanged(position, DiffBeanDiffCallBack.DIFF_NAME);
                    } else {
                        diffBean.setValue(Integer.parseInt(diffBean.getValue()) * 2 + "");
                        list.set(position, diffBean);
                        mAdapter.submitList(list);
                        mAdapter.notifyItemDiffChanged(position, DiffBeanDiffCallBack.DIFF_VALUE);
                    }
                }
            }
        });
        setData();
    }

    private void setData() {
        List<DiffBean> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(new DiffBean(i, i + "", i + "", i + "",
                    i < 40 ? DiffAdapter.VIEW_TYPE_H : VIEW_TYPE_EMPTY));
        }
        mAdapter.submitList(list);
    }

    //深度复制list
    public List<DiffBean> deepCopyList(List<DiffBean> oldList) {
        String json = new Gson().toJson(oldList);
        TypeToken<List<DiffBean>> typeToken = new TypeToken<List<DiffBean>>() {
        };
        return GsonUtil.jsonToListObject(json, typeToken);
    }

    public class LauncherDragListener implements NewLauncherView.IDragActionCallback {

        @Override
        public void onDrop(View dragView, int startPosition, int lastPosition, int currentPosition, boolean out) {
            dragView.setVisibility(View.VISIBLE);
            List<DiffBean> list = mAdapter.getNewData();
            int index = currentPosition == -1 ? startPosition : currentPosition;
            if (index < list.size()) {
                mAdapter.notifyItemDiffChanged(index);
            }
        }

        @Override
        public boolean prepareInsert(int startPos, int targetPost) {
            int targetIndex = targetPost - mAdapter.getHeadersCount();
            int startIndex = startPos - mAdapter.getHeadersCount();
            if (targetIndex == startIndex || startIndex < 0 || targetIndex < 0 ||
                    startIndex >= mAdapter.getData().size() ||
                    targetIndex >= mAdapter.getData().size()) {
                return false;
            }
            DiffBean startItem = mAdapter.getItem(startIndex);
            DiffBean endItem = mAdapter.getItem(targetIndex);
            if (!startItem.isEmpty() && endItem.isEmpty()) {
                return true;
            }

            return false;
        }

        @Override
        public void unPrepareInsert(boolean isInsert) {
            if (isInsert) {
                mLauncherView.endDrag();
            }
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public boolean onSwap(View dragView, int startPosition, int lastPosition, int currentPosition, boolean out) {
            if (startPosition == currentPosition) {
                return false;
            }
            Log.i("mTest", "onSwap==startPosition=" + startPosition + " currentPosition=" + currentPosition);
            int fromIndex = startPosition - mAdapter.getHeadersCount();
            int toIndex = currentPosition - mAdapter.getHeadersCount();
            if (fromIndex < 0 || toIndex < 0 || toIndex > mAdapter.getData().size() - 1 || fromIndex > mAdapter.getData().size() - 1) {
                return false;
            }
            List<DiffBean> list = mAdapter.getNewData();
            DiffBean fromDelegate = list.get(fromIndex);
            DiffBean toDelegate = list.get(toIndex);
            if (toDelegate.isEmpty()) {
                return false;
            }
            if (fromIndex < toIndex) {
                if (toIndex >= list.size()) {
                    return false;
                }
                for (int i = fromIndex; i < toIndex; i++) {
                    Collections.swap(list, i, i + 1);
                }
            } else {
                if (fromIndex >= list.size()) {
                    return false;
                }
                for (int i = fromIndex; i > toIndex; i--) {
                    Collections.swap(list, i, i - 1);
                }
            }
            mAdapter.notifyItemMoved(startPosition, currentPosition);
            return true;
        }

        @Override
        public boolean onInsert(View dragView, int startPosition, int lastPosition, int currentPosition, boolean needOpenPack) {
            int headerCount = mAdapter.getHeadersCount();
            int fromIndex = startPosition - headerCount;
            int toIndex = currentPosition - headerCount;

            if (fromIndex < 0 || toIndex < 0) {
                return false;
            }
            if (toIndex > mAdapter.getData().size() - 1) {
                toIndex = mAdapter.getData().size() - 1;
            }
            List<DiffBean> list = deepCopyList(mAdapter.getData());
            DiffBean toDelegate = list.get(toIndex);
            if (!toDelegate.isEmpty()) {
                return false;
            }

            DiffBean fromDelegate = list.get(fromIndex);

            toDelegate.setValue(fromDelegate.getValue());
            toDelegate.setName(fromDelegate.getName());
            toDelegate.setId(fromDelegate.getId());
            toDelegate.setTitle(fromDelegate.getTitle());
            toDelegate.setType(fromDelegate.getType());

            list.set(toIndex, toDelegate);
            list.remove(fromIndex);
            mAdapter.submitList(list);
            return true;
        }

        @Override
        public void unPack(View dragView, int startPosition, boolean hidePack) {

        }
    }
}
