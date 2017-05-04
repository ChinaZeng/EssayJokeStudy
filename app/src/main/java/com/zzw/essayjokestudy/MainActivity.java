package com.zzw.essayjokestudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zzw.baselibray.base.BaseActivity;
import com.zzw.baselibray.ioc.ViewUtils;
import com.zzw.framelibray.BaseSkinActivity;

public class MainActivity extends BaseSkinActivity {

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        ViewUtils.inject(this);
    }

    @Override
    protected void initTitle() {

    }
}
