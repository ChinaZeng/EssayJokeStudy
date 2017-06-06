package com.zzw.baselibray.imageloader;

import android.content.Context;

/**
 * Created by zzw on 2016/05/08.
 * Version:1.0
 * Des:
 */
public class ImageLoader {
    //    private volatile static ImageLoader mImageLoader;
    private static ImageLoader mInstance;
    private BaseImageLoaderStrategy mStrategy;

    private ImageLoader() {
    }

    public static ImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null)
                    mInstance = new ImageLoader();
            }
        }
        return mInstance;
    }

//    public static ImageLoader getInstance() {
//        if (mImageLoader == null) {
//            synchronized (ImageLoader.class) {
//                if (mImageLoader == null)
//                    mImageLoader = new ImageLoader();
//            }
//        }
//        return mImageLoader;
//    }

    public <T extends ImageConfig> void loadImage(Context context, T config) {
        this.mStrategy.loadImage(context, config);
    }

    public void setLoadImgStrategy(BaseImageLoaderStrategy strategy) {
        this.mStrategy = strategy;
    }

}
