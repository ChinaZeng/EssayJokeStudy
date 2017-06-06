package com.zzw.baselibray.imageloader;

import android.content.Context;

/**
 * Created by zzw on 2016/05/08.
 * Version:1.0
 * Des:
 */
public interface BaseImageLoaderStrategy<T extends ImageConfig> {
    void loadImage(Context ctx, T config);
}
