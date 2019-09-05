package com.example.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.demo.adapter.HomepageFragmentAdapter;
import com.example.demo.R;

import java.util.ArrayList;
import java.util.List;

public class HomepageFragment extends Fragment {

    private ViewPager viewpager;
    private TabLayout tab;
    private View view;
    private List<String> titel;
    private List<Fragment> mFragment;
    private HomepageFragmentAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
        initTab();

        mAdapter = new HomepageFragmentAdapter(getChildFragmentManager(),mFragment,titel);
        viewpager.setAdapter(mAdapter);
        tab.setupWithViewPager(viewpager);
        mAdapter.updateFragments(mFragment);
        viewpager.setOffscreenPageLimit(3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepage,container,false);
        return view;
    }

    private void initData(){
        titel = new ArrayList<>();
        titel.add(getResources().getString(R.string.music_page));
        titel.add(getResources().getString(R.string.movie_page));
        titel.add(getResources().getString(R.string.sport_page));

        mFragment = new ArrayList<>();
        mFragment.add(new MusicFragment());
        mFragment.add(new MovieFragment());
        //mFragment.add(new MovieFragment());
    }

    private void initView(){
        tab = getActivity().findViewById(R.id.tab);
        //tab.setSelectedTabIndicatorHeight(0);
        viewpager = getActivity().findViewById(R.id.viewpager1);
    }

    private void initTab(){
        //TabLayout.Tab tt;
        for(int i = 0; i < 3; i++){
            tab.addTab(tab.newTab().setText(titel.get(i)));
        }
        tab.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewpager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
            }
        });
    }
}
