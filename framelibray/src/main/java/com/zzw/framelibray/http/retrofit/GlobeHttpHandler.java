package com.zzw.framelibray.http.retrofit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by zzw on 2017/5/30.
 * Version:
 * Des: Retrofit的请求拦截  请求一个命令还没发送之前拦截 和  请求得到数据拦截  的接口
 */
public interface GlobeHttpHandler {
    Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response);
    Request onHttpRequestBefore(Interceptor.Chain chain, Request request);
}
