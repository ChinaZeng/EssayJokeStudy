package com.zzw.baselibray.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzw.baselibray.permission.PermissionHelper;


/**
 * Created by zzw on 2017/5/4.
 * Version：1.0
 * description：
 */
public abstract class BaseFragment extends Fragment {

    protected View rootView;
    protected Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = getActivity();
        rootView = View.inflate(context, getLayoutId(), null);

        // 加入注解
        com.zzw.baselibray.ioc.ViewUtils.inject(rootView, this);//动态注入

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract int getLayoutId();


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionHelper.requestPermissionsResult(this, requestCode, permissions);
//    }

}
