package com.example.demo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TaskActivity extends BaseUiActivity implements View.OnClickListener{

    private ImageView ivTask;
    private Button btDownload;
    private Button btCancel;
    private ProgressBar progressBar;
    private TextView tvProgress;

    private String urlStr;
    private MyAsyncTask myAsyncTask;

    @Override
    protected int getLayoutId(){
        return R.layout.activity_task;
    }

    @Override
    protected void initData(){
        setMyActionBar("AsyncTask",false);
        ivTask = findViewById(R.id.iv_task);
        btDownload = findViewById(R.id.bt_download);
        btCancel = findViewById(R.id.bt_cancel);
        progressBar = findViewById(R.id.progressbar);
        tvProgress = findViewById(R.id.tv_progress);
        btDownload.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        btCancel.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);
        urlStr = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1567573436013&di=c98946f3a4e8381ff4910bc57d0641f4&imgtype=0&src=http%3A%2F%2F07imgmini.eastday.com%2Fmobile%2F20190311%2F20190311065348_fe19ec86dc9d1551e2b1b12eae9cd8e2_2.jpeg";
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_download:
                myAsyncTask = new MyAsyncTask();
                tvProgress.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
                myAsyncTask.execute(urlStr);
                progressBar.setProgress(100);
                break;
            case R.id.bt_cancel:
                if (myAsyncTask!=null && myAsyncTask.getStatus() == AsyncTask.Status.RUNNING){
                    myAsyncTask.cancel(true);
                    btCancel.setEnabled(false);
                    btDownload.setEnabled(true);
                    progressBar.setVisibility(View.INVISIBLE);
                    tvProgress.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                break;
        }
    }

    public class MyAsyncTask extends AsyncTask<String,Integer, Bitmap>{
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            btDownload.setEnabled(false);
            btCancel.setEnabled(true);
            tvProgress.setVisibility(View.INVISIBLE);
            tvProgress.setText("加载中");
        }

        @Override
        protected Bitmap doInBackground(String... params){
            HttpURLConnection connection = null;
            try {
                URL url = new URL(params[0]);
                connection= (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setConnectTimeout(20000);
                int code = connection.getResponseCode();
                if (code==200){
                    InputStream is = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int length;
                    int progress =0;
                    int count = connection.getContentLength();
                    byte[] bs = new byte[5];
                    while ((length=is.read(bs))!=-1){
                        progress+=length;
                        if (count ==0){
                            publishProgress(-1);
                        }else{
                            publishProgress((int)((float)progress/count*100));
                        }
                        if (isCancelled()){
                            return null;
                        }
                        Thread.sleep(1);
                        bos.write(bs,0,length);
                    }
                    return BitmapFactory.decodeByteArray(bos.toByteArray(),0,bos.size());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (connection!=null){
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            progressBar.setVisibility(View.VISIBLE);
            if (progress!=-1) {
                progressBar.setProgress(progress);
            }
            tvProgress.setVisibility(View.VISIBLE);
            tvProgress.setText("loading..." + values[0] + "%");
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Toast.makeText(TaskActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
            ivTask.setImageBitmap(bitmap);
            btDownload.setEnabled(true);
            btCancel.setEnabled(false);
            tvProgress.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
