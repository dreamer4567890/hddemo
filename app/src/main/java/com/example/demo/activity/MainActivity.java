package com.example.demo.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.view.KeyEvent;
import android.view.View;

import com.example.demo.active.activity.ScreenBroadcastListener;
import com.example.demo.active.activity.ScreenManager;
import com.example.demo.active.service.LiveService;
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
    private List<String> title;
    private List<Fragment> mFragment;
    private DemoFragmentAdapter mAdapter;
    private ScreenManager screenManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        ivBack.setVisibility(View.INVISIBLE);
        ivSearch.setVisibility(View.INVISIBLE);
        //一、1像素Activity进程保活
       /* screenManager = ScreenManager.getInstance(this);
        ScreenBroadcastListener listener = new ScreenBroadcastListener(this);
        listener.setScreenStateListener(new ScreenBroadcastListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                screenManager.finishActivity();
            }

            @Override
            public void onScreenOff() {
                screenManager.startActivity();
            }
        });*/

        //二、Service进程保活
        /*LiveService.toLiveService(this);*/
    }

    @Override
    protected void initData() {
        setMyActionBar("Demo", true);

        title = new ArrayList<>();
        title.add(getResources().getString(R.string.home_page));
        title.add(getResources().getString(R.string.business_page));
        title.add(getResources().getString(R.string.mine_page));

        mFragment = new ArrayList<>();
        mFragment.add(new HomepageFragment());
        mFragment.add(new BusinessFragment());
        mFragment.add(new MineFragment());

        tab = findViewById(R.id.tab);
        //tab.setSelectedTabIndicatorHeight(0);
        viewpager = findViewById(R.id.viewpager);

        mAdapter = new DemoFragmentAdapter(getSupportFragmentManager(), mFragment, title);
        viewpager.setAdapter(mAdapter);
        tab.setupWithViewPager(viewpager);
        mAdapter.updateFragments(mFragment);
        viewpager.setOffscreenPageLimit(3);

        initTab();
    }

    private void initTab() {
        //TabLayout.Tab tt;
        tab.removeAllTabs();
        for (int i = 0; i < 3; i++) {
            tab.addTab(tab.newTab().setText(title.get(i)).setIcon(R.mipmap.ic_launcher));
        }
        tab.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewpager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
            }
        });
    }

    public void exit() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
