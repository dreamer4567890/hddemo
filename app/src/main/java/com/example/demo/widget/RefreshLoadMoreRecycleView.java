package com.example.demo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class RefreshLoadMoreRecycleView extends RecyclerView {
    private Boolean isLoadMore;//是否可以加载更多标志
    private Boolean isLoadEnd;//加载到最后的标志
    private Boolean isLoadStart;//顶部的标志
    private Boolean isRefresh;//是否可以下拉刷新标志
    private int lastVisibleItem;//最后一项
    private IOnScrollListener listener;//事件监听

    public RefreshLoadMoreRecycleView(Context context) {
        super(context);
        init(context);
    }

    public RefreshLoadMoreRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshLoadMoreRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context) {
        isLoadEnd = false;
        isLoadStart = true;
        //isRefresh = true;

        this.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //SCROLL_STATE_DRAGGING 和 SCROLL_STATE_IDLE 两种效果
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (isLoadEnd) {
                        // 判断是否已加载所有数据
                        if (isLoadMore) {//未加载完所有数据，加载数据，并且还原isLoadEnd值为false，重新定位列表底部
                            if (getListener() != null) {
                                getListener().onLoadMore();
                            }
                        } else {//加载完了所有的数据
                            if(getListener()!=null){
                                getListener().onLoaded();
                            }
                        }
                        isLoadEnd = false;
                    } else if (isLoadStart) {
                        if(isRefresh){
                            if (getListener() != null) {
                                getListener().onRefresh();
                            }
                            isLoadStart=false;
                        }
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //上滑
                if(dy>0){
                    //是否滑到底部
                    if(!recyclerView.canScrollVertically(1)){
                        isLoadEnd = true;
                    }else{
                        isLoadEnd = false;
                    }
                }
                //下滑
                else if(dy<0){
                    //是否滑到顶部
                    if(!recyclerView.canScrollVertically(-1)){
                        isLoadStart=true;
                    }else{
                        isLoadStart=false;
                    }
                }
            }
        });
    }

    //监听事件
    public interface IOnScrollListener {
        void onRefresh();

        void onLoadMore();

        void onLoaded();
    }

    public IOnScrollListener getListener() {
        return listener;
    }

    public void setListener(IOnScrollListener listener) {
        this.listener = listener;
    }

    public Boolean getLoadMore() {
        return isLoadMore;
    }

    //设置是否支持加载更多
    public void setLoadMoreEnable(Boolean loadMore) {
        isLoadMore = loadMore;
    }

    public Boolean getRefresh() {
        return isRefresh;
    }

    //设置是否支持下拉刷新
    public void setRefreshEnable(Boolean refresh) {
        isRefresh = refresh;
    }
}