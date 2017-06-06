package com.zzw.framelibray.glide;

import android.app.Activity;
import android.content.Context;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zzw.baselibray.imageloader.BaseImageLoaderStrategy;


/**
 * Created by zzw on 2016/05/08.
 * Version:1.0
 * Des:
 */
public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy<GlideImageConfig> {
    @Override
    public void loadImage(Context ctx, GlideImageConfig config) {
        RequestManager manager = null;
        if (ctx instanceof Activity)//如果是activity则可以使用Activity的生命周期
            manager = Glide.with((Activity) ctx);
        else
            manager = Glide.with(ctx);

        DrawableRequestBuilder<String> requestBuilder = manager.load(config.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade();

        if (config.getPlaceholder() != 0)//设置占位符
            requestBuilder.placeholder(config.getPlaceholder());
//        else
//            requestBuilder.placeholder(R.mipmap.ic_load);

        if (config.getErrorPic() != 0)//设置错误的图片
            requestBuilder.error(config.getErrorPic());
//        else
//            requestBuilder.error(R.mipmap.ic_load);

        requestBuilder
                .into(config.getImageView());
    }
}
