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
            HttpUtils.init(new OkHttpEngine());
            //插件换肤
            SkinManager.getInstance().init(this);
            //热修复
//            FixBugManager manager = new FixBugManager(this);
//            manager.loadFixDex();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
