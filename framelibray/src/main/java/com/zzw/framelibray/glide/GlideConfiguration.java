package com.zzw.framelibray.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.zzw.framelibray.utils.DataHelper;


/**
 * Created by zzw on 2016/05/08.
 * Version:1.0
 * Des:
 */
public class GlideConfiguration implements GlideModule {
    public static final int IMAGE_DISK_CACHE_MAX_SIZE = 100 * 1024 * 1024;//图片缓存文件最大值为100Mb

    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {
        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                // Careful: the external cache directory doesn't enforce permissions
                return DiskLruCacheWrapper.get(DataHelper.getCacheFile(context), IMAGE_DISK_CACHE_MAX_SIZE);
            }
        });

        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);
        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
