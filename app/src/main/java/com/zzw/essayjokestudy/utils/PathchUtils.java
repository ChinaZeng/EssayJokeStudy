package com.zzw.essayjokestudy.utils;

/**
 * Created by zzw on 2017/5/13.
 * Version:增量更新  相关工具类
 * Des:
 */

public class PathchUtils {

    static {
        System.loadLibrary("bspatch");
    }

    /**
     * 合并apk
     *
     * @param oldApkPath 原来的apk路径  1.0版本
     * @param newApkPath 合并后新的apk路径  需要生成2.0版本
     * @param patchPath  差分包路径，从服务器上下载的
     */
    public static native void combine(String oldApkPath, String newApkPath, String patchPath);

}
