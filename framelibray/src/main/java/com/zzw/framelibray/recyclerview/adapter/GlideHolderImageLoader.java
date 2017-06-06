package com.zzw.framelibray.recyclerview.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zzw.baselibray.imageloader.ImageLoader;
import com.zzw.framelibray.glide.GlideImageConfig;
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
        ImageLoader.getInstance().loadImage(context, GlideImageConfig
                .builder()
                .imagerView(imageView)
                .url(imagePath)
                .build()
        );
    }
}
