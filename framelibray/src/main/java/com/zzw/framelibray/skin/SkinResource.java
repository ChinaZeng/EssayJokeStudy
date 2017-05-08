package com.zzw.framelibray.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by  zzw on 2017/5/8.
 * 皮肤的资源管理类
 */

public class SkinResource {

    private Resources mSkinResources;
    private String mPackageName;

    public SkinResource(Context context, String skinPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            String FilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "a.skip";
            addAssetPathMethod.invoke(assetManager, FilePath);
            Resources superResources = context.getResources();
            mSkinResources = new Resources(assetManager, superResources.getDisplayMetrics(), superResources.getConfiguration());

            //获取skinPath包名
            mPackageName = context.getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).applicationInfo.packageName;


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过名字获取Drawable
     *
     * @param resName
     * @return
     */
    public Drawable getDrawableByName(String resName) {
        try {
            int resId = mSkinResources.getIdentifier(resName, "drawable", mPackageName);
            return mSkinResources.getDrawable(resId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * 通过名字获取Color
     *
     * @param resName
     * @return
     */
    public ColorStateList getColorByName(String resName) {
        try {
            int resId = mSkinResources.getIdentifier(resName, "color", mPackageName);
            return mSkinResources.getColorStateList(resId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
