package com.example.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.activity.EditPersonalDataActivity;
import com.example.demo.widget.RoundImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MineFragment extends Fragment {

    private View view;
    private CircleImageView ivHead;
    private TextView tvName;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine,container,false);
        return view;
    }

    private void initView(){
        ivHead = getActivity().findViewById(R.id.iv_head);
        tvName = getActivity().findViewById(R.id.tv_name);
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditPersonalDataActivity.class);
                startActivity(intent);
            }
        });
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditPersonalDataActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData(){

    }
}
