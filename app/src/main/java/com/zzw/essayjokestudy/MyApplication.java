package com.zzw.essayjokestudy;


import com.zzw.baselibray.base.BaseApplication;
import com.zzw.baselibray.fixBug.FixBugManager;
import com.zzw.baselibray.http.HttpUtils;
import com.zzw.framelibray.http.OkHttpEngine;
import com.zzw.framelibray.skin.SkinManager;


/**
 * Created by zzw on 2017/5/4.
 * Version:
 * Des:
 */

public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            //热修复
            FixBugManager manager = new FixBugManager(this);
            manager.loadFixDex();


            //插件换肤
            SkinManager.getInstance().init(this);

            HttpUtils.init(new OkHttpEngine());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
