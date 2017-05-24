package com.zzw.framelibray.banner;

import android.view.View;

/**
 * Created by zzw on 2017/5/24.
 * Version:
 * Des:
 */

public abstract class BannerAdapter {

    /**
     * 根据位置获取ViewPager里面的View
     *
     * @param position
     *  @param convertView 复用的界面
     * @return
     */
    public abstract View getView(int position,View convertView);

    /**
     * 获取轮播的数量
     *
     * @return
     */
    public abstract int getCount();


    /**
     * 获取当前轮播位置的描述
     *
     * @param position
     * @return
     */
    public String getBannerDesc(int position) {
        return "";
    }
}
