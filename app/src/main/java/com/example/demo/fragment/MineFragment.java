package com.example.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.activity.EditPersonalDataActivity;
import com.example.demo.activity.BatchDownloadActivity;
import com.example.demo.activity.TaskActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class MineFragment extends Fragment implements View.OnClickListener {

    private View view;
    private CircleImageView ivHead;
    private TextView tvName;
    private LinearLayout llTask;
    private LinearLayout llBatch;

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
        llTask = getActivity().findViewById(R.id.ll_task);
        llBatch = getActivity().findViewById(R.id.ll_batch);
        ivHead.setOnClickListener(this);
        tvName.setOnClickListener(this);
        llTask.setOnClickListener(this);
        llBatch.setOnClickListener(this);
    }

    private void initData(){

    }

    @Override
    public void onClick(View view){
        Intent intent = null;
        switch (view.getId()){
            case R.id.iv_head:
            case R.id.tv_name:
                intent = new Intent(getActivity(), EditPersonalDataActivity.class);
                break;
            case R.id.ll_task:
                intent = new Intent(getActivity(),TaskActivity.class);
                break;
            case R.id.ll_batch:
                intent = new Intent(getActivity(), BatchDownloadActivity.class);
            default:
                break;
        }
        startActivity(intent);
    }
}
