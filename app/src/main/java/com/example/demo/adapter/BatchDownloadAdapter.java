package com.example.demo.adapter;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.demo.R;

@SuppressLint("NewApi")
public class BatchDownloadAdapter extends ArrayAdapter {

    private GridView mGridView;
    //图片缓存类
    private LruCache<String,Bitmap> mLruCache;
    //记录所有正在下载或等待下载的任务
    private HashSet mDownloadBitmapAsyncTaskHashSet;
    //GridView中可见的第一张图片的下标
    private int mFirstVisibleItem;
    //GridView中可见的图片的数量
    private int mVisibleItemCount;
    //记录是否是第一次进入该界面
    private boolean isFirstEnterThisActivity = true;

    private String[] Urls;

    public BatchDownloadAdapter(Context context, int textViewResourceId,String[] objects, GridView gridView) {
        super(context, textViewResourceId, objects);

        Urls = objects;
        mGridView = gridView;
        mGridView.setOnScrollListener(new ScrollListenerImpl());

        mDownloadBitmapAsyncTaskHashSet = new HashSet();

        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        // 设置图片缓存大小为maxMemory的1/6
        int cacheSize = maxMemory/6;

        mLruCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String url = getItem(position).toString();
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_batch_download, null);
        } else {
            view = convertView;
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        //为该ImageView设置一个Tag,防止图片错位
        imageView.setTag(url);
        //为该ImageView设置显示的图片
        setImageForImageView(url, imageView);
        return view;
    }


    private void setImageForImageView(String imageUrl, ImageView imageView) {
        Bitmap bitmap = getBitmapFromLruCache(imageUrl);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher);
        }
    }


    private void addBitmapToLruCache(String key, Bitmap bitmap) {
        if (getBitmapFromLruCache(key) == null) {
            mLruCache.put(key, bitmap);
        }
    }


    private Bitmap getBitmapFromLruCache(String key) {
        return (Bitmap) mLruCache.get(key);
    }




    private void loadBitmaps(int firstVisibleItem, int visibleItemCount, String[] Urls) {
        try {
            for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
                String imageUrl = Urls[i];
                Bitmap bitmap = getBitmapFromLruCache(imageUrl);
                if (bitmap == null) {
                    DownloadBitmapAsyncTask downloadBitmapAsyncTask = new DownloadBitmapAsyncTask();
                    mDownloadBitmapAsyncTaskHashSet.add(downloadBitmapAsyncTask);
                    downloadBitmapAsyncTask.execute(imageUrl);
                } else {
                    //依据Tag找到对应的ImageView显示图片
                    ImageView imageView = (ImageView) mGridView.findViewWithTag(imageUrl);
                    if (imageView != null && bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void cancelAllTasks() {
        if (mDownloadBitmapAsyncTaskHashSet != null) {
            for (Object task : mDownloadBitmapAsyncTaskHashSet) {
                DownloadBitmapAsyncTask asyncTask = (DownloadBitmapAsyncTask) task;
                asyncTask.cancel(false);
            }
        }
    }


    private class ScrollListenerImpl implements OnScrollListener{

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
            mFirstVisibleItem = firstVisibleItem;
            mVisibleItemCount = visibleItemCount;
            if (isFirstEnterThisActivity && visibleItemCount > 0) {
                loadBitmaps(firstVisibleItem, visibleItemCount, Urls);
                isFirstEnterThisActivity = false;
            }
        }


        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_IDLE) {
                loadBitmaps(mFirstVisibleItem, mVisibleItemCount, Urls);
            } else {
                cancelAllTasks();
            }
        }

    }


    class DownloadBitmapAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private String imageUrl;
        @Override
        protected Bitmap doInBackground(String... params) {
            imageUrl = params[0];
            Bitmap bitmap = downloadBitmap(params[0]);
            if (bitmap != null) {
                //下载完后,将其缓存到LrcCache
                addBitmapToLruCache(params[0], bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //下载完成后,找到其对应的ImageView显示图片
            ImageView imageView = (ImageView) mGridView.findViewWithTag(imageUrl);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            mDownloadBitmapAsyncTaskHashSet.remove(this);
        }
    }

    // 获取Bitmap
    private Bitmap downloadBitmap(String imageUrl) {
        Bitmap bitmap = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(imageUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5 * 1000);
            httpURLConnection.setReadTimeout(10 * 1000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return bitmap;
    }

}