package com.zzw.framelibray.glide;

import android.util.Log;
import android.widget.ImageView;

import com.zzw.baselibray.imageloader.ImageConfig;


/**
 * Created by zzw on 2016/05/08.
 * Version:1.0
 * Des:图片加载的配置信息
 */

public class GlideImageConfig extends ImageConfig {

    private GlideImageConfig(Buidler builder) {
        this.url = builder.url;
        this.imageView = builder.imageView;
        this.placeholder = builder.placeholder;
        this.errorPic = builder.errorPic;
    }

    public static Buidler builder() {
        return new Buidler();
    }


    public static final class Buidler {
        private String url;
        private ImageView imageView;
        private int placeholder;
        protected int errorPic;

        private Buidler() {
        }

        public Buidler url(String url) {
            this.url = url;
            return this;
        }

        public Buidler placeholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Buidler errorPic(int errorPic) {
            this.errorPic = errorPic;
            return this;
        }

        public Buidler imagerView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public GlideImageConfig build() {
//            if (url == null) throw new IllegalStateException("url is required");
            if (url == null) {
                Log.e("GlideImageConfig", "url==null");
                url = "";
            }
            if (imageView == null) throw new IllegalStateException("imageview is required");
            return new GlideImageConfig(this);
        }
    }
}
