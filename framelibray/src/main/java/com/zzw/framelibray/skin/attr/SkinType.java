package com.zzw.framelibray.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzw.framelibray.skin.SkinManager;
import com.zzw.framelibray.skin.SkinResource;

/**
 * Created by zzw on 2017/5/8.
 */

public enum SkinType {

    TEXT_COLOR("textColor") {
        @Override
        public void skin(View mView, String mResName) {
            //获取资源设置
            SkinResource skinResource = getSkinResource();

            ColorStateList colorStateList = skinResource.getColorByName(mResName);
            if (colorStateList == null) {
                return;
            }

//            if (mView instanceof TextView) {
//                TextView textView = (TextView) mView;
//                textView.setTextColor(colorStateList);
//            }

            if (mView instanceof Button) {
                Button bt = (Button) mView;
                bt.setTextColor(colorStateList);
            } else if (mView instanceof TextView) {
                TextView textView = (TextView) mView;
                textView.setTextColor(colorStateList);
            }
        }
    },

    BACKGROUND("background") {
        @Override
        public void skin(View mView, String mResName) {
            SkinResource skinResource = getSkinResource();

            //背景可能是图片
            Drawable drawable = skinResource.getDrawableByName(mResName);
            if (drawable != null) {
//                ImageView imageView = (ImageView) mView;
//                imageView.setBackgroundDrawable(drawable);
                mView.setBackgroundDrawable(drawable);
                return;
            }

            //也可能是颜色
            ColorStateList colorStateList = skinResource.getColorByName(mResName);
            if (colorStateList != null) {
                mView.setBackgroundColor(colorStateList.getDefaultColor());
            }
        }
    },

    SRC("src") {
        @Override
        public void skin(View mView, String mResName) {
            SkinResource skinResource = getSkinResource();

            //背景可能是图片
            Drawable drawable = skinResource.getDrawableByName(mResName);
            if (drawable == null) {
                return;
            }
            if(mView instanceof ImageView){
                ImageView imageView = (ImageView) mView;
                imageView.setImageDrawable(drawable);
            }
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

    public SkinResource getSkinResource() {
        return SkinManager.getInstance().getSkinResource();
    }
}
