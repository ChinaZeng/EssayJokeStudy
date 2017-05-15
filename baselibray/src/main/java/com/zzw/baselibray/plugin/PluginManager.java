package com.zzw.baselibray.plugin;

import android.content.Context;

import com.zzw.baselibray.fixBug.FixBugManager;

/**
 * Created by zzw on 2017/5/12.
 * Version:插件化应用管理类 还未写加载资源
 * Des:
 */

public class PluginManager {


    public static final void install(Context context, String apk) {
        try {
            //解决类的加载问题
            FixBugManager fixBugManager = new FixBugManager(context);
            //把apk的class加载到applicationClassLoader
            fixBugManager.fixDex(apk);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
