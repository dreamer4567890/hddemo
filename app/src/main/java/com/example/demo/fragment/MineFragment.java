package com.example.demo.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.activity.EditPersonalDataActivity;
import com.example.demo.activity.BatchDownloadActivity;
import com.example.demo.activity.RetrofitActivity;
import com.example.demo.activity.TaskActivity;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class MineFragment extends Fragment implements View.OnClickListener {

    private View view;
    private CircleImageView ivHead;
    private TextView tvName;
    private LinearLayout llTask;
    private LinearLayout llBatch;
    private LinearLayout llRetrofit;

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
        llRetrofit = getActivity().findViewById(R.id.ll_retrofit);
        ivHead.setOnClickListener(this);
        tvName.setOnClickListener(this);
        llTask.setOnClickListener(this);
        llBatch.setOnClickListener(this);
        llRetrofit.setOnClickListener(this);
    }

    private void initData(){
        SharedPreferences pref = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        if (!TextUtils.isEmpty(pref.getString("nickname",""))){
            tvName.setText(pref.getString("nickname",""));
        }
    }

    @Override
    public void onClick(View view){
        Intent intent = null;
        switch (view.getId()){
            case R.id.iv_head:
            case R.id.tv_name:
                intent = new Intent(getActivity(), EditPersonalDataActivity.class);
                intent.putExtra("nickname",tvName.getText().toString());
                startActivityForResult(intent,1);
                break;
            case R.id.ll_task:
                intent = new Intent(getActivity(),TaskActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_batch:
                intent = new Intent(getActivity(), BatchDownloadActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_retrofit:
                intent = new Intent(getActivity(), RetrofitActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            String name= data.getStringExtra("name");
            if(name != null){
                tvName.setText(name);
            }
        }
    }
}
