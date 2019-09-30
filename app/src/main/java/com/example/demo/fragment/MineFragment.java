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
import com.example.demo.activity.ContactsActivity;
import com.example.demo.activity.EditPersonalDataActivity;
import com.example.demo.activity.BatchDownloadActivity;
import com.example.demo.activity.GreenDaoActivity;
import com.example.demo.activity.RetrofitActivity;
import com.example.demo.activity.SlideActivity;
import com.example.demo.activity.SqlActivity;
import com.example.demo.activity.TaskActivity;
import com.example.demo.bean.Contacts;
import com.example.demo.mvp.IBaseView;
import com.example.demo.presenter.MinePresenter;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class MineFragment extends BasePresenterFragment<MinePresenter, IBaseView> implements IBaseView,View.OnClickListener {

    private View view;
    private CircleImageView ivHead;
    private TextView tvName;
    private LinearLayout llTask;
    private LinearLayout llBatch;
    private LinearLayout llRetrofit;
    private LinearLayout llSql;
    private LinearLayout llContacts;
    private LinearLayout llGreenDAO;
    private LinearLayout llSlide;

    @Override
    protected MinePresenter initPresenter(){
        return new MinePresenter();
    }

    @Override
    protected int getLayoutId(){
        return R.layout.fragment_mine;
    }

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(view);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine,container,false);
        return view;
    }*/

    @Override
    protected void initView(View view){
        ivHead = view.findViewById(R.id.iv_head);
        tvName = view.findViewById(R.id.tv_name);
        llTask = view.findViewById(R.id.ll_task);
        llBatch = view.findViewById(R.id.ll_batch);
        llRetrofit = view.findViewById(R.id.ll_retrofit);
        llSql = view.findViewById(R.id.ll_sql);
        llContacts = view.findViewById(R.id.ll_contacts);
        llGreenDAO = view.findViewById(R.id.ll_greendao);
        llSlide = view.findViewById(R.id.ll_slide);
        ivHead.setOnClickListener(this);
        tvName.setOnClickListener(this);
        llTask.setOnClickListener(this);
        llBatch.setOnClickListener(this);
        llRetrofit.setOnClickListener(this);
        llSql.setOnClickListener(this);
        llContacts.setOnClickListener(this);
        llGreenDAO.setOnClickListener(this);
        llSlide.setOnClickListener(this);
    }

    @Override
    protected void initData(){
        /*SharedPreferences pref = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        if (!TextUtils.isEmpty(pref.getString("nickname",null))){
            tvName.setText(pref.getString("nickname",null));
        }*/
        tvName.setText(mPresenter.getMyInfo(getActivity()));
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
            case R.id.ll_sql:
                intent = new Intent(getActivity(), SqlActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_contacts:
                intent = new Intent(getActivity(), ContactsActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_greendao:
                intent = new Intent(getActivity(), GreenDaoActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_slide:
                intent = new Intent(getActivity(), SlideActivity.class);
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