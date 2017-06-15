package com.zzw.baselibray.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.zzw.baselibray.manager.AppManager;

/**
 * Created by zzw on 2017/5/4.
 * Version:
 * Des:
 */

public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {


    @Override
    public void onCreate() {
        super.onCreate();
//        HookStartActivityUtil hookStartActivityUtil = new HookStartActivityUtil(this, ProxyActivity.class);
//        try {
//            hookStartActivityUtil.hookStartActivity();
//            hookStartActivityUtil.hookHandleLaunchActivity();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        AppManager.getInstance().addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        AppManager.getInstance().finishActivity(activity);
    }

    /**
     * 退出Activity
     */
    public void exit() {
        AppManager.getInstance().exitApp();
    }
}
