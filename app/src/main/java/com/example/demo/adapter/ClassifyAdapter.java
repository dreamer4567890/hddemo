package com.example.demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.bean.Classify;
import com.example.demo.bean.Music;

import java.util.List;

public class ClassifyAdapter extends RecyclerView.Adapter<ClassifyAdapter.ViewHolder> {

    private List<Classify> mClassifyList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView classifyName;
        View itemView;

        private ViewHolder(View view){
            super(view);
            classifyName = (TextView) view.findViewById(R.id.tv_name);
            itemView = view;
        }
    }

    public ClassifyAdapter(List<Classify> classifyList){
        mClassifyList = classifyList;
    }

    @Override
    public ClassifyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classify,parent,false);
        final ClassifyAdapter.ViewHolder holder = new ClassifyAdapter.ViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Toast.makeText(view.getContext(),mClassifyList.get(position).getName(),Toast.LENGTH_LONG).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ClassifyAdapter.ViewHolder holder, int position){
        Classify classify = mClassifyList.get(position);
        holder.classifyName.setText(classify.getName());
    }

    @Override
    public int getItemCount(){
        return mClassifyList.size();
    }
}
