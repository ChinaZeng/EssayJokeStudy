package com.zzw.framelibray.skin;

import android.app.Activity;
import android.content.Context;
import android.util.ArrayMap;

import com.zzw.baselibray.util.L;
import com.zzw.framelibray.BaseSkinActivity;
import com.zzw.framelibray.skin.attr.SkinView;

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
    private Map<Activity, List<SkinView>> mSkinViews = new HashMap<>();

    public static SkinManager getInstance() {
        if (mInstance == null)
            mInstance = new SkinManager();
        return mInstance;
    }

    private SkinManager() {

    }


    public void init(Context context) {
        this.mContext = context.getApplicationContext();
    }

    /**
     * 加载皮肤
     *
     * @param skinPath
     * @return
     */
    public int loadSkin(String skinPath) {

        //校验签名

        //初始化皮肤管理

        mSkinResource = new SkinResource(mContext, skinPath);
        //改变皮肤
        Set<Activity> keySet = mSkinViews.keySet();

        for (Activity activity : keySet) {
            List<SkinView> skinViews = mSkinViews.get(activity);
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
        }

        return 0;
    }


    /**
     * 恢复默认
     *
     * @return
     */
    public int restoreDefault() {


        return 0;
    }

    /**
     * 获取当前Activity的List<SkinView>集合
     *
     * @param activity
     * @return
     */
    public List<SkinView> getSKinViews(Activity activity) {

        return mSkinViews.get(activity);
    }

    /**
     * 注册
     *
     * @param baseSkinActivity
     * @param skinViews
     */
    public void register(BaseSkinActivity baseSkinActivity, List<SkinView> skinViews) {
        mSkinViews.put(baseSkinActivity, skinViews);
    }

    /**
     * 获取当前皮肤资源管理
     * @return
     */
    public SkinResource getmSkinResource() {
        return mSkinResource;
    }
}
