package com.zzw.framelibray.skin;

import android.app.Activity;
import android.content.Context;
import android.util.ArrayMap;

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

        SkinResource skinResource = new SkinResource(mContext, skinPath);
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
}
