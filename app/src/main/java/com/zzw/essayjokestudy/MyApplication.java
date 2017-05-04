package com.zzw.essayjokestudy;

import android.content.pm.PackageManager;

import com.alipay.euler.andfix.patch.PatchManager;
import com.zzw.baselibray.ExceptionCrashHandler;
import com.zzw.baselibray.base.BaseApplication;

import java.security.PublicKey;

/**
 * Created by zzw on 2017/5/4.
 * Version:
 * Des:
 */

public class MyApplication extends BaseApplication {

    public static PatchManager mPatchManager;

    @Override
    public void onCreate() {
        super.onCreate();
        ExceptionCrashHandler.getInstance().init(this);
        // Ali热修复
        try {
            mPatchManager = new PatchManager(this);
            // 初始化patch版本
            String pkName = this.getPackageName();
            String versionName = getPackageManager().getPackageInfo(pkName, 0).versionName;
            // 初始化版本名称
            mPatchManager.init(versionName);
            // 加载之前的patch
            mPatchManager.loadPatch();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
