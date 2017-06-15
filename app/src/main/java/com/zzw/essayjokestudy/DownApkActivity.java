package com.zzw.essayjokestudy;


import android.view.View;

import com.zzw.baselibray.base.BaseActivity;
import com.zzw.essayjokestudy.update.AppUpgradeService;

/**
 * Created by zzw on 2017/6/14.
 * Version:
 * Des:
 */

public class DownApkActivity extends BaseActivity {
    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        findViewById(R.id.bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUpgradeService.newInstance(DownApkActivity.this, "http://imtt.dd.qq.com/16891/7B3AAC3060036902E7C98371956DFF3D.apk?fsname=com.qihoo.cleandroid_cn_5.5.1_56.apk&csr=1bbd", "test.apk");
            }
        });
    }


    @Override
    protected void initData() {

    }
}
