package com.zzw.framelibray;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zzw.framelibray.recyclerview.adapter.ViewHolder;

/**
 * Created by zzw on 2017/5/17.
 * Version:
 * Des:
 */

public class GlideHolderImageLoader extends ViewHolder.HolderImageLoader {

    public GlideHolderImageLoader(String imagePath) {
        super(imagePath);
    }

    @Override
    public void displayImage(Context context, ImageView imageView, String imagePath) {
        Glide.with(context).load(imagePath)
                .centerCrop().into(imageView);
    }
}
