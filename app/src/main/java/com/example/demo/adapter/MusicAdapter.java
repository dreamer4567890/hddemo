package com.example.demo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.demo.bean.Music;
import com.example.demo.R;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private List<Music> mMusicList;
    private onClickListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView musicImage;
        TextView musicSinger;
        View itemView;
        Context context;

        private ViewHolder(View view){
            super(view);
            musicImage = (ImageView) view.findViewById(R.id.iv_singer);
            musicSinger = (TextView) view.findViewById(R.id.tv_singer);
            itemView = view;
            context = view.getContext();
        }
    }

    public MusicAdapter(List<Music> musicList){
        mMusicList = musicList;
    }

    public List<Music> getmMusicList() {
        return mMusicList;
    }

    public void setmMusicList(List<Music> mMusicList) {
        this.mMusicList = mMusicList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        Music music = mMusicList.get(position);
        holder.musicSinger.setText(music.getSinger());
        Glide.with(holder.context).load(music.getUrl()).into(holder.musicImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(view.getContext(),mMusicList.get(position).getSinger(),Toast.LENGTH_LONG).show();
              /*  mMusicList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mMusicList.size() - position);*/
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        return mMusicList.size();
    }

    public interface onClickListener {
        void onClick(int position);
    }

    public void setOnClickListener(onClickListener listener) {
        this.listener = listener;
    }
}
