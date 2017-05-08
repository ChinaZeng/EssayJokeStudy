package com.zzw.framelibray.skin.attr;

import android.view.View;

/**
 * Created by zzw on 2017/5/8.
 */

public enum SkinType {

    TEXT_COLOR("textColor") {
        @Override
        public void skin(View mView, String mResName) {
            //获取资源设置
        }
    }, BACKGROUND("background") {
        @Override
        public void skin(View mView, String mResName) {

        }
    }, SRC("src") {
        @Override
        public void skin(View mView, String mResName) {

        }
    };

    private String mResName;

    SkinType(String resName) {
        this.mResName = resName;
    }

    public abstract void skin(View mView, String mResName);

    public String getResName() {
        return mResName;
    }
}
