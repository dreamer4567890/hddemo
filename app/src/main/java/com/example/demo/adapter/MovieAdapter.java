package com.example.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.demo.bean.Movie;
import com.example.demo.R;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> mMovieList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView movieImage;
        TextView movieName;
        View itemView;
        Context context;

        private ViewHolder(View view){
            super(view);
            movieImage = (ImageView) view.findViewById(R.id.iv_movie);
            movieName = (TextView) view.findViewById(R.id.tv_movie);
            itemView = view;
            context = view.getContext();
        }
    }

    public MovieAdapter(List<Movie> movieList){
        mMovieList = movieList;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,parent,false);
        final MovieAdapter.ViewHolder holder = new MovieAdapter.ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Toast.makeText(view.getContext(),mMovieList.get(position).getName(),Toast.LENGTH_LONG).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position){
        Movie movie = mMovieList.get(position);
        holder.movieName.setText(movie.getName());
        Glide.with(holder.context).load(movie.getUrl()).into(holder.movieImage);
    }

    @Override
    public int getItemCount(){
        return mMovieList.size();
    }
}
