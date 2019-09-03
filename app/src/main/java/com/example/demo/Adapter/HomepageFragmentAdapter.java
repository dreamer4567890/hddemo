package com.example.demo.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HomepageFragmentAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<Fragment> fragments;
    private List<String> titles;
    private FragmentManager mFragmentManager;
    private List<String> tags;

    public HomepageFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.tags = new ArrayList<>();
        this.mFragmentManager = fm;
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public void updateFragments(List<Fragment> fragments) {
        if (this.tags != null) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            for (int i = 0; i < tags.size(); i++) {
                fragmentTransaction.remove(mFragmentManager.findFragmentByTag(tags.get(i)));
            }
            fragmentTransaction.commit();
            mFragmentManager.executePendingTransactions();
            tags.clear();
        }
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        tags.add(makeFragmentName(container.getId(), getItemId(position)));
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        this.mFragmentManager.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
}
