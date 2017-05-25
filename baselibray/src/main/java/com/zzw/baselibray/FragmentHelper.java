package com.zzw.baselibray;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by zzw on 2017/5/25.
 * Version:
 * Des:Fragment辅助类
 */

public class FragmentHelper {

    private FragmentManager mFragmentManager;
    private int mContainerViewId;//容器布局id

//    public FragmentHelper() {
//
//    }

    /**
     * @param fragmentManager Fragment管理类
     * @param containerViewId 容器布局id
     */
    public FragmentHelper(@NonNull FragmentManager fragmentManager, @IdRes int containerViewId) {
        this.mFragmentManager = fragmentManager;
        this.mContainerViewId = containerViewId;
    }

    /**
     * 添加Fragment
     *
     * @param fragment
     */
    public void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(mContainerViewId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * 切换显示Fragment
     *
     * @param fragment
     */
    public void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        List<Fragment> childFragments = mFragmentManager.getFragments();

        //先影藏所有的已经有的Fragment
        for (Fragment childFragment : childFragments) {
            fragmentTransaction.hide(childFragment);
        }

        //如果不包含这个fragment，就先添加
        if (!childFragments.contains(fragment)) {
            fragmentTransaction.add(mContainerViewId, fragment);
        } else {//有的话就直接提交
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.commit();
    }

}
