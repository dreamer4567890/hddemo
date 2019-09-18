package com.example.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.example.demo.R;
import com.example.demo.fragment.BaseFragment;

public abstract class BaseFragmentActivity extends BaseUiActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.base_fragment_layout;
    }

    /**
     * 添加fragment
     * activity中首次调用该方法
     *
     * @param fragment
     */
    public void addFragment(BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName()).commit();
        }
    }

    /**
     * 添加fragment
     * fragment不放入堆栈，替换
     *
     * @param fragment
     */
    public void addFragmentNoAdd(BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, fragment.getClass().getSimpleName()).commit();
        }
    }

    public void clearTopFragment(BaseFragment fragment) {

//        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
//            FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(i);
//        }
        if (fragment != null) {
            getSupportFragmentManager().popBackStackImmediate(0, 1);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName()).commit();
        }
    }


    /**
     * 添加fragment
     * 用add和hide主要是不替换fragment， 可以用接口形式进行数据交互
     * 多页面的长数据建议用单例存储
     *
     * @param fragment
     */
    public void addFragmentAddHide(BaseFragment fragment, BaseFragment hideFragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container, fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName());
            if (hideFragment != null) {
                transaction.hide(hideFragment);
            }
            transaction.commit();
        }
    }

    /**
     * 清空指定fragment前的所有堆栈
     * type 0 弹出fragment 之上所有
     * type 1弹出  fragment和之上所有（包括本身）
     */
    public void popBackStackImmediate(BaseFragment fragment, int type) {
        getSupportFragmentManager().popBackStackImmediate(fragment.getClass().getSimpleName(), type);
    }

    /**
     * 添加fragment
     * 直接添加，不放入堆栈
     *
     * @param fragment
     */
    public void addFragmentAdd(BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment, fragment.getClass().getSimpleName()).commit();
        }
    }


    /**
     * 获取堆栈中的fragment
     *
     * @param name
     * @return
     */
    public Fragment getFragment(String name) {
        return getSupportFragmentManager().findFragmentByTag(name);
    }

    /**
     * 移除fragment
     */
    public void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            supportFinishAfterTransition();
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    protected abstract void initData(Bundle savedInstanceState);

}
