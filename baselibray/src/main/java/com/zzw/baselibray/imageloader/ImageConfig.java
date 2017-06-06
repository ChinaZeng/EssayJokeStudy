package com.zzw.baselibray.imageloader;

import android.widget.ImageView;


/**
 * Created by zzw on 2016/05/08.
 * Version:1.0
 * Des:图片加载的配置信息
 */
public class ImageConfig {
    protected String url;
    protected ImageView imageView;
    protected int placeholder;
    protected int errorPic;


    public String getUrl() {
        return url;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public int getErrorPic() {
        return errorPic;
    }
}
