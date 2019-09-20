package com.example.demo.activity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.adapter.BatchDownloadAdapter;

import java.util.ArrayList;
import java.util.List;

public class BatchDownloadActivity extends BaseUiActivity {

    private GridView gridView;
    private BatchDownloadAdapter mAdapt;
    private List<String> url;

    @Override
    protected int getLayoutId(){
        return R.layout.activity_batch_download;
    }

    @Override
    protected void initData(){
        url = new ArrayList<>();
        for(int i = 0; i < 30; i++){
            url.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1567573436013&di=c98946f3a4e8381ff4910bc57d0641f4&imgtype=0&src=http%3A%2F%2F07imgmini.eastday.com%2Fmobile%2F20190311%2F20190311065348_fe19ec86dc9d1551e2b1b12eae9cd8e2_2.jpeg");
        }
        setMyActionBar("Batch Download",false);
        gridView = findViewById(R.id.gridView);
        mAdapt = new BatchDownloadAdapter(this,0, url, gridView);
        gridView.setAdapter(mAdapt);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapt.cancelAllTasks();
    }
}
