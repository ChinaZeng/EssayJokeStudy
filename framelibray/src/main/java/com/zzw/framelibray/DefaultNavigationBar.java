package com.zzw.framelibray;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zzw.baselibray.navigationbar.AbsNavigationBar;


/**
 * Created by zzw on 2017/5/17.
 * Version 1.0
 * Description: 默认的头部
 */
public class DefaultNavigationBar<D extends
        DefaultNavigationBar.Builder.DefaultNavigationParams> extends
        AbsNavigationBar<DefaultNavigationBar.Builder.DefaultNavigationParams> {

    public DefaultNavigationBar(DefaultNavigationBar.Builder.DefaultNavigationParams params) {
        super(params);
    }


    @Override
    public int bindLayoutId() {
        return R.layout.title_bar;
    }

    @Override
    public void applyView() {
        // 绑定效果
        setText(R.id.title, getParams().mTitle);
        setText(R.id.right_text, getParams().mRightText);
        setOnClickListener(R.id.right_text, getParams().mRightClickListener);
        // 左边 要写一个默认的  finishActivity
        setOnClickListener(R.id.back,getParams().mLeftClickListener);
        setVisibility(R.id.back,getParams().leftIconVisible);
    }




    public static class Builder extends AbsNavigationBar.Builder {

        DefaultNavigationParams P;


        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new DefaultNavigationParams(context, parent);
        }

        public Builder(Context context) {
            super(context, null);
            P = new DefaultNavigationParams(context, null);
        }

        @Override
        public DefaultNavigationBar builder() {
            DefaultNavigationBar navigationBar = new DefaultNavigationBar(P);
            return navigationBar;
        }

        // 1. 设置所有效果
        public DefaultNavigationBar.Builder setTitle(String title) {
            P.mTitle = title;
            return this;
        }


        public DefaultNavigationBar.Builder setRightText(String rightText) {
            P.mRightText = rightText;
            return this;
        }

        /**
         * 设置右边的点击事件
         */
        public DefaultNavigationBar.Builder
        setRightClickListener(View.OnClickListener rightListener) {
            P.mRightClickListener = rightListener;
            return this;
        }

        /**
         * 设置左边的点击事件
         */
        public DefaultNavigationBar.Builder
        setLeftClickListener(View.OnClickListener rightListener) {
            P.mLeftClickListener = rightListener;
            return this;
        }

        /**
         * 设置右边的图片
         */
        public DefaultNavigationBar.Builder setRightIcon(int rightRes) {

            return this;
        }

        public DefaultNavigationBar.Builder hideLeftIcon() {
            P.leftIconVisible = View.INVISIBLE;
            return this;
        }

        public static class DefaultNavigationParams extends
                AbsNavigationBar.Builder.AbsNavigationParams {

            // 所有效果放置
            public String mTitle;

            public String mRightText;

            public int leftIconVisible = View.VISIBLE;

            // 后面还有一些通用的
            public View.OnClickListener mRightClickListener;

            public View.OnClickListener mLeftClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 关闭当前Activity
                    ((Activity) mContext).finish();
                }
            };

            public DefaultNavigationParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
