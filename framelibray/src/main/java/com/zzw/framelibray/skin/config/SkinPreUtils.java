package com.zzw.framelibray.skin.config;

import android.content.Context;

/**
 * Created by zzw on 2017/5/9.
 */

public class SkinPreUtils {

    private static SkinPreUtils mInstance;
    private Context mContext;

    private SkinPreUtils(Context context) {
        this.mContext=context;
    }


    public static SkinPreUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SkinPreUtils.class) {
                mInstance = new SkinPreUtils(context);
            }
        }
//        mInstance.init(context);
        return mInstance;
    }

//    private void init(Context context) {
//        this.mContext = context;
//    }


    /**
     * 保存当前皮肤路径
     *
     * @param skinPath
     */
    public void savaSkinPath(String skinPath) {
        mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME, Context.MODE_PRIVATE)
                .edit().putString(SkinConfig.SKIN_PATH_NAME, skinPath).commit();
    }

    /**
     * 获取当前皮肤路径
     *
     * @return 当前皮肤路径
     */
    public String getSkinPath() {
        return mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME, Context.MODE_PRIVATE)
                .getString(SkinConfig.SKIN_PATH_NAME, null);
    }

    /**
     * 清空皮肤状态路径信息
     */
    public void cleanSkinInfo() {
        savaSkinPath("");
    }
}
