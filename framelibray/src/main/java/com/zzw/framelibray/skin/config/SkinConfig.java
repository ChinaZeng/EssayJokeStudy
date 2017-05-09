package com.zzw.framelibray.skin.config;

/**
 * Created by zzw on 2017/5/9.
 */

public class SkinConfig {
    //sp 文件名称
    public static final String SKIN_INFO_NAME = "SKIN_INFO_NAME";

    //保存文件路径的名称
    public static final String SKIN_PATH_NAME = "SKIN_PATH_NAME";


    //恢复默认的时候  如果当前使用的是默认的  就不需要改变
    public static final int SKIN_CHANGE_NOTHING = -1;

    //换肤成功
    public static final int SKIN_CHANGE_SUCCESS = 1;

    //皮肤路径不存在
    public static final int SKIN_FILE_NO_EXISTS = -2;

    //皮肤文件错误  可能不是一个APK
    public static final int SKIN_FILE_ERROR = -3;
}
