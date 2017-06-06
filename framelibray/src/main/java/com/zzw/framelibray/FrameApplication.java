package com.zzw.framelibray;


import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.zzw.baselibray.base.BaseApplication;
import com.zzw.baselibray.imageloader.ImageLoader;
import com.zzw.framelibray.glide.GlideImageLoaderStrategy;
import com.zzw.framelibray.http.NetWorkError;
import com.zzw.framelibray.http.retrofit.GlobeHttpHandler;
import com.zzw.framelibray.http.retrofit.RetrofitHttpEngine;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by zzw on 2017/5/30.
 * Version:
 * Des:
 */

public class FrameApplication extends BaseApplication implements GlobeHttpHandler {

    private RefWatcher mRefWatcher;//leakCanary观察器

    @Override
    public void onCreate() {
        super.onCreate();
        try {

            installLeakCanary();//leakCanary内存泄露检查
            //初始化ImageLoader  默认Glide
            ImageLoader.getInstance().setLoadImgStrategy(new GlideImageLoaderStrategy());

            RetrofitHttpEngine.buidler(this)
                    .baseUrl("http://v.juhe.cn/")
                    .globeHttpHandler(this)
//                    .interceptors()
                    .responseErrorListener(new NetWorkError())
                    .build();

            //在应用层设置 这里设置 BuildConfig.DEBUG为false
            if (CommonData.LOG_DEBUG) {//Timber日志打印
                Timber.plant(new Timber.DebugTree());
            }

            //插件换肤
//            SkinManager.getInstance().init(this);
            //网络引擎配置
//            HttpUtils.init(new OkHttpEngine());
            //热修复
//            FixBugManager manager = new FixBugManager(this);
//            manager.loadFixDex();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 安装leakCanary检测内存泄露
     */
    protected void installLeakCanary() {
        this.mRefWatcher = CommonData.LOG_DEBUG ? LeakCanary.install(this) : RefWatcher.DISABLED;
    }

    /**
     * 获得leakCanary观察器
     *
     * @param context
     * @return
     */
    public static RefWatcher getRefWatcher(Context context) {
        FrameApplication application = (FrameApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }

    @Override
    public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
        //这里可以先客户端一步拿到每一次http请求的结果,
        // 可以解析成json,做一些操作,如检测到token过期后


        return response;
    }

    @Override
    public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
        //如果需要再请求服务器之前做一些操作,
        //则重新返回一个做过操作的的request如增加header,不做操作则返回request


        return request;
    }
}
