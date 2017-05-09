package com.zzw.essayjokestudy;


import com.zzw.baselibray.base.BaseApplication;
import com.zzw.baselibray.fixBug.FixBugManager;
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

        FixBugManager manager = new FixBugManager(this);
        try {
            manager.loadFixDex();
        } catch (Exception e) {
            e.printStackTrace();
        }


        SkinManager.getInstance().init(this);

    }
}
