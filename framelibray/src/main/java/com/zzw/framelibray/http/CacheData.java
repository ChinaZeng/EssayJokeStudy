package com.zzw.framelibray.http;


/**
 * Created by zzw on 2017/5/13.
 * Version: 1.0
 * Des: 缓存的实体类
 */
public class CacheData {
    // 请求链接
    private String mUrlKey;

    // 后台返回的Json
    private String mResultJson;

    public CacheData() {

    }

    public CacheData(String urlKey, String resultJson) {
        this.mUrlKey = urlKey;
        this.mResultJson = resultJson;
    }

    public String getResultJson() {
        return mResultJson;
    }
}
