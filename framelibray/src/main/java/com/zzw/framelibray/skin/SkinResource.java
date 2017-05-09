package com.zzw.framelibray.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.zzw.baselibray.util.L;

import java.lang.reflect.Method;

/**
 * Created by  zzw on 2017/5/8.
 * 皮肤的资源管理类
 */

public class SkinResource {

    private Resources mSkinResources;
    private String mPackageName;

    /**
     * @param context
     * @param skinPath 皮肤路径
     */
    public SkinResource(Context context, String skinPath) {
        try {
            //创建一个AssetManager
            //AssetManager assetManager = new AssetManager(); hide的调用不了  只有用反射调用
            AssetManager assetManager = AssetManager.class.newInstance();
            //添加本地下载好的资源皮肤
            //  assetManager.addAssetPath(String path);// 也是hide的调用不了  继续用反射执行该方法
            Method addAssetPathMethod = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);

            addAssetPathMethod.invoke(assetManager, skinPath);

            Resources superResources = context.getResources();
            mSkinResources = new Resources(assetManager, superResources.getDisplayMetrics(), superResources.getConfiguration());

            //获取skinPath包名
            mPackageName = context.getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).applicationInfo.packageName;
            L.e(mPackageName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过名字获取Drawable
     *
     * @param resName
     * @return null表示无此资源
     */
    public Drawable getDrawableByName(String resName) {
        try {
            int resId = mSkinResources.getIdentifier(resName, "drawable", mPackageName);

            if (resId == 0) {
                resId = mSkinResources.getIdentifier(resName, "mipmap", mPackageName);
            }

            return resId == 0 ? null : mSkinResources.getDrawable(resId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过名字获取Color
     *
     * @param resName
     * @return null表示无此资源
     */
    public ColorStateList getColorByName(String resName) {
        try {
            int resId = mSkinResources.getIdentifier(resName, "color", mPackageName);
            return resId == 0 ? null : mSkinResources.getColorStateList(resId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
