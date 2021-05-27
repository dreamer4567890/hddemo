package com.example.demo.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class CommonPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;
    private FragmentManager mFragmentManager;
    private List<String> mTagList;

    public CommonPagerAdapter(FragmentManager mFragmentManager, List<Fragment> mFragmentList) {
        super(mFragmentManager);
        this.mFragmentManager = mFragmentManager;
        this.mTagList = new ArrayList<>();
        this.mFragmentList = mFragmentList;
    }

    //强制刷新
    public void setNewFragments(List<Fragment> fragments) {
        if (this.mTagList != null) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            for (int i = 0; i < mTagList.size(); i++) {
                fragmentTransaction.remove(mFragmentManager.findFragmentByTag(mTagList.get(i)));
            }
            fragmentTransaction.commitAllowingStateLoss();
            mFragmentManager.executePendingTransactions();
            mTagList.clear();
        }
        this.mFragmentList = fragments;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }


    @Override
    public int getCount() {
        return mFragmentList != null ? mFragmentList.size() : 0;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        mTagList.add(makeFragmentName(container.getId(), getItemId(position)));
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        this.mFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mFragmentList.size() > position) {
            Fragment fragment = mFragmentList.get(position);
            mFragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss();
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Fragment fragment = mFragmentList.get(position);
        Bundle bundle = fragment.getArguments();
       /* if (bundle != null) {
            RoomInfo roomInfo = bundle.getParcelable(KEY_ROOM_INFO);
            if (roomInfo != null) {
                return roomInfo.getRoom_name();
            }
        }*/
        return "";
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

}
