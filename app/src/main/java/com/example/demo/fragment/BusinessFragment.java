package com.example.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.adapter.ClassifyAdapter;
import com.example.demo.bean.Classify;
import com.example.demo.mvp.IBaseView;
import com.example.demo.presenter.BusinessPresenter;
import com.example.demo.widget.GlideImageLoader;
import com.example.demo.widget.RoundImageView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class BusinessFragment extends BasePresenterFragment<BusinessPresenter, IBaseView> implements View.OnClickListener {

    private Banner banner;
    private View view;
    private List<String> mList;
    private List<String> mTitle;
    private List<Classify> mClassifyList;

    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;
    private LinearLayout linearLayout3;
    private RoundImageView roundImageView1;
    private RoundImageView roundImageView2;
    private RoundImageView roundImageView3;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private RecyclerView recyclerView;
    private ClassifyAdapter classifyAdapter;

    @Override
    protected BusinessPresenter initPresenter(){
        return new BusinessPresenter();
    }

    @Override
    protected int getLayoutId(){
        return R.layout.fragment_business;
    }


    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initBanner();
        initData();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        classifyAdapter = new ClassifyAdapter(mClassifyList);
        recyclerView.setAdapter(classifyAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_business,container,false);
        return view;
    }*/

    @Override
    protected void initView(View view){
        banner = view.findViewById(R.id.banner);
        mList = new ArrayList<>();
        mTitle = new ArrayList<>();
        mClassifyList = new ArrayList<>();
        linearLayout1 = view.findViewById(R.id.linearLayout1);
        linearLayout2 = view.findViewById(R.id.linearLayout2);
        linearLayout3 = view.findViewById(R.id.linearLayout3);
        roundImageView1 = view.findViewById(R.id.roundImageView1);
        roundImageView2 = view.findViewById(R.id.roundImageView2);
        roundImageView3 = view.findViewById(R.id.roundImageView3);
        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.textView3);
        textView4 = view.findViewById(R.id.textView4);
        textView5 = view.findViewById(R.id.textView5);
        recyclerView = view.findViewById(R.id.recyclerView2);
        linearLayout1.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
        linearLayout3.setOnClickListener(this);
        textView2.setOnClickListener(this);
        initBanner();
    }

    private void initBanner(){
        mList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1567573436013&di=c98946f3a4e8381ff4910bc57d0641f4&imgtype=0&src=http%3A%2F%2F07imgmini.eastday.com%2Fmobile%2F20190311%2F20190311065348_fe19ec86dc9d1551e2b1b12eae9cd8e2_2.jpeg");
        mList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1567573436014&di=4a217cf9a01dcc2d868be7c238230537&imgtype=0&src=http%3A%2F%2F09imgmini.eastday.com%2Fmobile%2F20181106%2F20181106090642_d542ff68d96ccb1c7787a5a0122ad22f_1.jpeg");
        mList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1567573570837&di=f2d0124ae1388d0e3ba3ae014f0b7140&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201703%2F25%2F20170325142932_FvPZy.jpeg");
        mList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1567573436014&di=8b7e693380bb766caff8a8038eb37594&imgtype=0&src=http%3A%2F%2F02.imgmini.eastday.com%2Fmobile%2F20180613%2F1254485964e3efed7cf49afa51349d49_wmk.jpeg");
        mTitle.add("1");
        mTitle.add("2");
        mTitle.add("3");
        mTitle.add("4");

        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(mList);
        banner.setBannerTitles(mTitle);
        banner.setDelayTime(3000);
        banner.setBannerAnimation(Transformer.ZoomOutSlide);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.isAutoPlay(true);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Toast.makeText(getContext(),mTitle.get(position + 1),Toast.LENGTH_SHORT).show();
            }
        });
        banner.start();
    }

    @Override
    protected void initData(){
        roundImageView1.setImageResource(R.mipmap.ic_launcher);
        roundImageView2.setImageResource(R.mipmap.ic_launcher);
        roundImageView3.setImageResource(R.mipmap.ic_launcher);
        textView3.setText("中国好声音");
        textView4.setText("中国新说唱2019");
        textView5.setText("2019年男篮世界杯");

        Classify movie = new Classify("movie");
        Classify comic = new Classify("comic");
        for(int i = 0; i < 3; i++){
            mClassifyList.add(movie);
            mClassifyList.add(comic);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        classifyAdapter = new ClassifyAdapter(mClassifyList);
        recyclerView.setAdapter(classifyAdapter);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.linearLayout1:
                Toast.makeText(getContext(),textView3.getText().toString(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.linearLayout2:
                Toast.makeText(getContext(),textView4.getText().toString(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.linearLayout3:
                Toast.makeText(getContext(),textView5.getText().toString(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.textView2:
                Toast.makeText(getContext(),"敬请期待",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        banner.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }
}