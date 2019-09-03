package com.example.demo.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.Bean.Music;
import com.example.demo.R;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private List<Music> mMusicList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView musicImage;
        TextView musicSinger;
        View itemView;

        public ViewHolder(View view){
            super(view);
            musicImage = (ImageView) view.findViewById(R.id.iv_singer);
            musicSinger = (TextView) view.findViewById(R.id.tv_singer);
            itemView = view;
        }
    }

    public MusicAdapter(List<Music> musicList){
        mMusicList = musicList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Toast.makeText(view.getContext(),mMusicList.get(position).getSinger(),Toast.LENGTH_LONG).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Music music = mMusicList.get(position);
        holder.musicImage.setImageResource(music.getImageId());
        holder.musicSinger.setText(music.getSinger());
    }

    @Override
    public int getItemCount(){
        return mMusicList.size();
    }
}
