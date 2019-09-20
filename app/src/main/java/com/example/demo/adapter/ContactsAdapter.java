package com.example.demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.bean.Classify;
import com.example.demo.bean.Contacts;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<Contacts> mContactsList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        TextView tvPhone;
        View itemView;

        private ViewHolder(View view){
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvPhone = view.findViewById(R.id.tv_phone);
            itemView = view;
        }
    }

    public ContactsAdapter(List<Contacts> contactsList){
        mContactsList = contactsList;
    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacts,parent,false);
        final ContactsAdapter.ViewHolder holder = new ContactsAdapter.ViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Toast.makeText(view.getContext(),mContactsList.get(position).getName(),Toast.LENGTH_LONG).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.ViewHolder holder, int position){
        Contacts contacts = mContactsList.get(position);
        holder.tvName.setText(contacts.getName());
        holder.tvPhone.setText(contacts.getPhone());
    }

    @Override
    public int getItemCount(){
        return mContactsList.size();
    }
}
