package com.zzw.framelibray.skin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.zzw.framelibray.skin.attr.SkinView;
import com.zzw.framelibray.skin.callback.ISkinChangeListener;
import com.zzw.framelibray.skin.config.SkinConfig;
import com.zzw.framelibray.skin.config.SkinPreUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zzw on 2017/5/8.
 * 皮肤管理类
 */

public class SkinManager {
    private static SkinManager mInstance;
    private Context mContext;
    private SkinResource mSkinResource;
    private Map<ISkinChangeListener, List<SkinView>> mSkinViews = new HashMap<>();

    public static SkinManager getInstance() {
        if (mInstance == null)
            mInstance = new SkinManager();
        return mInstance;
    }

    private SkinManager() {

    }


    public void init(Context context) {
        try {
            this.mContext = context.getApplicationContext();

            //每次打开应用都要到这里来,防止皮肤被任意删除,做一些措施
            String currentSkinPath = SkinPreUtils.getInstance(context).getSkinPath();
            if (!TextUtils.isEmpty(currentSkinPath)) {
                File skinFile = new File(currentSkinPath);
                if (!skinFile.exists()) {
                    //文件被删除了  清空信息
                    SkinPreUtils.getInstance(context).cleanSkinInfo();
                    return;
                }
                //最好做一下能不能获取到报名
                //获取skinPath包名
                String PackageName = getPackNameBySkinPath(currentSkinPath);
                if (TextUtils.isEmpty(PackageName)) {
                    //不是一个apk  清空信息
                    SkinPreUtils.getInstance(context).cleanSkinInfo();
                    return;
                }

                //最好校验签名

                // 初始化的工作  如果在这里不初始化mSkinResource 在进入的时候如果有皮肤状态路径的话在SkinAttr里面mSkinResource==null就会崩溃
                mSkinResource = new SkinResource(mContext, currentSkinPath, PackageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 加载皮肤
     *
     * @param skinPath
     * @return
     */
    public int loadSkin(String skinPath) {

        //校验签名


        //判断当前皮肤是否是正在使用的皮肤
        String currentSkinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        if (skinPath.equals(currentSkinPath))//如果皮肤资源相同
            return SkinConfig.SKIN_CHANGE_NOTHING;

        //判断文件是否存在
        File skinFile = new File(skinPath);
        if (!skinFile.exists()) {
            //不存在就不执行下去
            return SkinConfig.SKIN_FILE_NO_EXISTS;
        }

        //最好做一下能不能获取到报名
        //获取skinPath包名
        String PackageName = getPackNameBySkinPath(skinPath);

        if (TextUtils.isEmpty(PackageName)) {
            return SkinConfig.SKIN_FILE_ERROR;
        }
        //初始化皮肤管理
        mSkinResource = new SkinResource(mContext, skinPath, PackageName);
        //改变皮肤
        changeSkin();

        //保存皮肤的状态
        saveSkinStatus(skinPath);

        return SkinConfig.SKIN_CHANGE_SUCCESS;
    }

    /**
     * 获取skinPath包名
     *
     * @param skinPath
     */
    private String getPackNameBySkinPath(String skinPath) {

        return mContext.getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).applicationInfo.packageName;
    }

    /**
     * 改变皮肤
     */
    private void changeSkin() {
        try {
            Set<ISkinChangeListener> keySet = mSkinViews.keySet();
            for (ISkinChangeListener listener : keySet) {
                List<SkinView> skinViews = mSkinViews.get(listener);
                for (SkinView skinView : skinViews) {
                    skinView.skin();
                }
                listener.changeSkin(mSkinResource);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存当前皮肤状态 是属于那套皮肤
     *
     * @param skinPath
     */
    private void saveSkinStatus(String skinPath) {
        SkinPreUtils.getInstance(mContext).savaSkinPath(skinPath);
    }


    /**
     * 恢复默认
     *
     * @return SkinConfig.SKIN_CHANGE_NOTHING不需要改变（当前本来就是默认的） SkinConfig.SKIN_CHANGE_SUCCESS换肤成功
     */
    public int restoreDefault() {
        //判断当前是否在使用其他皮肤
        String currentSkinPath = SkinPreUtils.getInstance(mContext).getSkinPath();

        if (TextUtils.isEmpty(currentSkinPath))//如果没有皮肤就不换
            return SkinConfig.SKIN_CHANGE_NOTHING;

        //当前手机运行的app的APK路径
        String skinPath = mContext.getPackageResourcePath();
        mSkinResource = new SkinResource(mContext, skinPath);
        //改变皮肤
        changeSkin();
        SkinPreUtils.getInstance(mContext).cleanSkinInfo();//恢复状态

        return SkinConfig.SKIN_CHANGE_SUCCESS;
    }

    /**
     * 获取当前ISkinChangeListener的List<SkinView>集合
     *
     * @param listener
     * @return
     */
    public List<SkinView> getSkinViews(ISkinChangeListener listener) {

        return mSkinViews.get(listener);
    }

    /**
     * 注册
     *
     * @param listener
     * @param skinViews
     */
    public void register(ISkinChangeListener listener, List<SkinView> skinViews) {
        mSkinViews.put(listener, skinViews);
    }

    /**
     * 注销
     *
     * @param listener
     */
    public void unregister(ISkinChangeListener listener) {
        mSkinViews.remove(listener);
    }

    /**
     * 获取当前皮肤资源管理
     *
     * @return
     */
    public SkinResource getSkinResource() {
        return mSkinResource;
    }


    /**
     * 检测是不是要换肤
     *
     * @param skinView
     */
    public void checkChangeSkin(SkinView skinView) {
        //如果当前有皮肤,也就是保存的皮肤路径
        String currentSkinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        if (!TextUtils.isEmpty(currentSkinPath)) {
            skinView.skin();
        }
    }
}
