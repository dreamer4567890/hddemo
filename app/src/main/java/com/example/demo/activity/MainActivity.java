package com.example.demo.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.widget.Toast;

import com.example.demo.adapter.DemoFragmentAdapter;
import com.example.demo.fragment.BusinessFragment;
import com.example.demo.fragment.HomepageFragment;
import com.example.demo.fragment.MineFragment;
import com.example.demo.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseUiActivity {

    private ViewPager viewpager;
    private TabLayout tab;
    private List<String> titel;
    private List<Fragment> mFragment;
    private DemoFragmentAdapter mAdapter;

    @Override
    protected int getLayoutId(){
        return R.layout.activity_main;
    }

    @Override
    protected void initData(){
        setMyActionBar("Demo",true);

        titel = new ArrayList<>();
        titel.add(getResources().getString(R.string.home_page));
        titel.add(getResources().getString(R.string.business_page));
        titel.add(getResources().getString(R.string.mine_page));

        mFragment = new ArrayList<>();
        mFragment.add(new HomepageFragment());
        mFragment.add(new BusinessFragment());
        mFragment.add(new MineFragment());

        tab = findViewById(R.id.tab);
        //tab.setSelectedTabIndicatorHeight(0);
        viewpager = findViewById(R.id.viewpager);

        mAdapter = new DemoFragmentAdapter(getSupportFragmentManager(),mFragment,titel);
        viewpager.setAdapter(mAdapter);
        tab.setupWithViewPager(viewpager);
        mAdapter.updateFragments(mFragment);
        viewpager.setOffscreenPageLimit(3);

        initTab();
    }

    private void initTab(){
        //TabLayout.Tab tt;
        tab.removeAllTabs();
        for(int i = 0; i < 3; i++){
            tab.addTab(tab.newTab().setText(titel.get(i)).setIcon(R.mipmap.ic_launcher));
        }
        tab.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewpager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
            }
        });
    }

    @Override
    public void showToast(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

}
