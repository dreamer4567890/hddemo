package com.example.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.Utils.ScreenUtil;
import com.example.demo.widget.LeftSlideView;

import java.util.ArrayList;
import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.MyViewHolder> implements LeftSlideView.IonSlidingButtonListener {

    private Context mContext;

    private List<String> mDatas = new ArrayList<>();

    private IonSlidingViewClickListener mIonSlidingViewClickListener;

    private LeftSlideView mMenu = null;


    public SlideAdapter(Context context) {

        mContext = context;
        mIonSlidingViewClickListener = (IonSlidingViewClickListener) context;

        for (int i = 0; i < 10; i++) {
            mDatas.add(i + "");
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public String getData(int position){
        return mDatas.get(position);
    }

    public List<String> getmDatas(){
        return mDatas;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.textView.setText(mDatas.get(position));

        //设置内容布局的宽为屏幕宽度
        holder.layout_content.getLayoutParams().width = ScreenUtil.getScreenWidth(mContext);

        //item正文点击事件
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    int n = holder.getAdapterPosition();
                    mIonSlidingViewClickListener.onItemClick(v, n);
                }

            }
        });


        //左滑添加点击事件
        holder.btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = holder.getAdapterPosition();
                mIonSlidingViewClickListener.onSetAddClick(view, n);
            }
        });


        //左滑删除点击事件
        holder.btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = holder.getAdapterPosition();
                mIonSlidingViewClickListener.onDeleteBtnClick(view, n);
            }
        });

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {

        //获取自定义View的布局（加载item布局）
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_slide, arg0, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView btn_Add;
        public TextView btn_Delete;
        public TextView textView;
        public ViewGroup layout_content;

        public MyViewHolder(View itemView) {
            super(itemView);

            btn_Add = (TextView) itemView.findViewById(R.id.tv_add);
            btn_Delete = (TextView) itemView.findViewById(R.id.tv_delete);
            textView = (TextView) itemView.findViewById(R.id.text);
            layout_content = (ViewGroup) itemView.findViewById(R.id.layout_content);

            ((LeftSlideView) itemView).setSlidingButtonListener(SlideAdapter.this);
        }
    }


    /**
     * 删除item
     * @param position
     */
    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }


    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (LeftSlideView) view;
    }


    /**
     * 滑动或者点击了Item监听
     *
     * @param leftSlideView
     */
    @Override
    public void onDownOrMove(LeftSlideView leftSlideView) {
        if (menuIsOpen()) {
            if (mMenu != leftSlideView) {
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;

    }

    /**
     * 判断菜单是否打开
     *
     * @return
     */
    public Boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        return false;
    }


    /**
     * 注册接口的方法：点击事件。在Mactivity.java实现这些方法。
     */
    public interface IonSlidingViewClickListener {
        void onItemClick(View view, int position);//点击item正文

        void onDeleteBtnClick(View view, int position);//点击“删除”

        void onSetAddClick(View view, int position);//点击“设置”
    }
}
