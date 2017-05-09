package com.zzw.framelibray.skin.attr;

import android.view.View;

import com.zzw.framelibray.skin.attr.SkinType;

/**
 * Created by zzw on 2017/5/8.
 */

public class SkinAttr {
    private String mResName;
    private SkinType mSkinType;

    public SkinAttr(String resName, SkinType skinType) {
        this.mResName = resName;
        this.mSkinType = skinType;
    }

    public void skin(View mView) {
        mSkinType.skin(mView, mResName);
    }

    @Override
    public String toString() {
        return "SkinAttr{" +
                "mResName='" + mResName + '\'' +
                ", mSkinType=" + mSkinType.getResName() +
                '}';
    }
}
