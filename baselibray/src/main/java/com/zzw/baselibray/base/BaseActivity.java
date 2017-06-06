package com.zzw.baselibray.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ViewUtils;
import android.view.View;


import java.io.IOException;

/**
 * Created by zzw on 2017/5/4.
 * 注意:只能放每个Activity都要用的通用方法.
 * 如果是两个或者两个以上的地方要使用,最好是写一个工具类.
 * 业务层不要直接集成BaseActivity  永远留一层   framelibray--->BaseSkinActivity
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayoutId());
        com.zzw.baselibray.ioc.ViewUtils.inject(this);//动态注入
        initTitle();
        initView();
        initData();
    }

    //初始化界面
    protected abstract int initLayoutId();

    //初始化头部
    protected abstract void initTitle();

    //初始化界面
    protected abstract void initView();

    //初始化数据
    protected abstract void initData();


    //权限申请在具体的activity里面重写
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionHelper.requestPermissionsResult(this, requestCode, permissions);
//    }

    /**
     * 启动Activity
     *
     * @param clazz
     */
    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
//
//    /**
//     * findViewById
//     *
//     * @param viewId
//     * @param <T>
//     * @return View
//     */
//    protected <T extends View> T viewById(@IdRes int viewId) {
//        return (T) findViewById(viewId);
//    }
}
