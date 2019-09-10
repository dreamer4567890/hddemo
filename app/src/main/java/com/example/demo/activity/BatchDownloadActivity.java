package com.example.demo.activity;

import android.os.Bundle;
import android.widget.GridView;

import com.example.demo.R;
import com.example.demo.adapter.BatchDownloadAdapter;

public class BatchDownloadActivity extends BaseActionBarActivity {

    private GridView gridView;
    private BatchDownloadAdapter mAdapt;
    private String[] url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_download);
        initData();
        initView();
    }

    private void initView(){
        setMyActionBar("Batch Download",false);
        gridView = findViewById(R.id.gridView);
        mAdapt = new BatchDownloadAdapter(this,0, url, gridView);
        gridView.setAdapter(mAdapt);
    }

    private void initData(){
        url = new String[30];
        for(int i = 0; i < url.length; i++){
            url[i] = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1567573436014&di=8b7e693380bb766caff8a8038eb37594&imgtype=0&src=http%3A%2F%2F02.imgmini.eastday.com%2Fmobile%2F20180613%2F1254485964e3efed7cf49afa51349d49_wmk.jpeg";
        }
    }
}
