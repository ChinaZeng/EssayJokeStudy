package com.zzw.framelibray.http;

import android.content.Context;

import com.google.gson.Gson;
import com.zzw.baselibray.http.EngineCallBack;
import com.zzw.baselibray.http.HttpUtils;

import java.util.Map;


/**
 * Created by zzw on 2017/5/13.
 * Version:1.0
 * Des: OkHttp实现的回调
 */

public abstract class HttpCallBack<T> implements EngineCallBack {

    @Override
    public void onPreExecute(Context context, Map<String, Object> params) {
        // 大大方方的添加公用参数  与项目业务逻辑有关
        // 项目名称  context
//        params.put("app_name","zzwStudy");
//        params.put("version_name","5.7.0");
//        params.put("ac","wifi");
//        params.put("device_id","30036118478");
//        params.put("device_brand","Xiaomi");
//        params.put("update_version_code","5701");
//        params.put("manifest_version_code","570");
//        params.put("longitude","113.000366");
//        params.put("latitude","28.171377");
//        params.put("device_platform","android");

        onPreExecute();
    }

    // 开始执行了
    public void onPreExecute() {

    }

    @Override
    public void onSuccess(String result) {

        Gson gson = new Gson();
        // data:{"name","zzw"}   data:"请求失败"
        T objResult = (T) gson.fromJson(result,
                HttpUtils.analysisClazzInfo(this));
        onSuccess(objResult);
    }

    // 返回可以直接操作的对象
    public abstract void onSuccess(T result);
}
