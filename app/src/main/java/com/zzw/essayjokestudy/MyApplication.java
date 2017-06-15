package com.zzw.essayjokestudy;


import com.zzw.framelibray.*;


/**
 * Created by zzw on 2017/5/4.
 * Version:
 * Des:
 */

public class MyApplication extends FrameApplication {


    @Override
    public void onCreate() {
        CommonData.LOG_DEBUG = BuildConfig.LOG_DEBUG;  //必须在这初始化
        super.onCreate();
    }

}
