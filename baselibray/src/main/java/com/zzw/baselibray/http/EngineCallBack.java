package com.zzw.baselibray.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by zzw on 2017/5/13.
 * Version:
 * Des:网络回调
 */

public interface EngineCallBack {
    // 开始执行，在执行之前会回掉的方法
    public void onPreExecute(Context context, Map<String,Object> params);

    // 错误
    public void onError(Exception e);

    // 成功  返回对象会出问题   成功  data对象{"",""}  失败  data:""
    public void onSuccess(String result);


    // 默认的

    public final EngineCallBack DEFAULT_CALL_BACK = new EngineCallBack() {
        @Override
        public void onPreExecute(Context context, Map<String, Object> params) {

        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };
}
