package com.zzw.baselibray.http;

import android.content.Context;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzw on 2017/5/13.
 * Version:
 * Des:网络引擎 工具类
 */

public class HttpUtils {

    // 直接带参数 ，链式调用
    private String mUrl;

    // 请求方式
    private int mType = GET_TYPE;
    private static final int POST_TYPE = 0x0011;
    private static final int GET_TYPE = 0x0022;

    private Map<String, Object> mParams;

    // 默认OkHttpEngine
    private static IHttpEngine mHttpEngine = null;

    // 上下文
    private Context mContext;

    // 是否读取缓存
    private boolean mCache = false;

    private HttpUtils(Context context) {
        mContext = context;
        mParams = new HashMap<>();
    }

    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    public HttpUtils url(String url) {
        this.mUrl = url;
        return this;
    }

    // 请求的方式
    public HttpUtils post() {
        mType = POST_TYPE;
        return this;
    }

    public HttpUtils get() {
        mType = GET_TYPE;
        return this;
    }

    // 是否配置缓存
    public HttpUtils cache(boolean isCache) {
        mCache = isCache;
        return this;
    }


    // 添加参数
    public HttpUtils addParam(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    public HttpUtils addParams(Map<String, Object> params) {
        mParams.putAll(params);
        return this;
    }

    // 请求头：

    // 添加回掉 执行
    public void execute(EngineCallBack callBack) {

        if (callBack == null) {
            callBack = EngineCallBack.DEFAULT_CALL_BACK;
        }

        // 每次执行都会进入这个方法：这里添加是行不通的
        // 1.baseLibrary里面这里面不包含业务逻辑
        // 2.如果有多条业务线，
        // 让callBack回调去
        callBack.onPreExecute(mContext, mParams);

        // 判断执行方法
        if (mType == POST_TYPE) {
            post(mUrl, mParams, callBack);
        }

        if (mType == GET_TYPE) {
            get(mUrl, mParams, callBack);
        }
    }

    public void execute() {
        execute(null);
    }


    // 在Application初始化引擎
    public static void init(IHttpEngine httpEngine) {
        mHttpEngine = httpEngine;
    }

    /**
     * 每次可以自带引擎
     */
    public HttpUtils exchangeEngine(IHttpEngine httpEngine) {
        mHttpEngine = httpEngine;
        return this;
    }


    private void get(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.get(mCache, mContext, url, params, callBack);
    }


    private void post(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.post(mCache, mContext, url, params, callBack);
    }

    /**
     * 拼接参数
     */
    public static String jointParams(String url, Map<String, Object> params) {
        if (params == null || params.size() <= 0) {
            return url;
        }

        StringBuffer stringBuffer = new StringBuffer(url);
        if (!url.contains("?")) {
            stringBuffer.append("?");
        } else {
            if (!url.endsWith("?")) {
                stringBuffer.append("&");
            }
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }

        stringBuffer.deleteCharAt(stringBuffer.length() - 1);

        return stringBuffer.toString();
    }

    /**
     * 解析一个类上面的class信息
     */
    public static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }
}
