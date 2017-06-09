package com.zzw.framelibray.http;

import android.content.Context;
import android.net.ParseException;
import android.widget.Toast;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.zzw.baselibray.http.IResponseErrorListener;
import com.zzw.framelibray.utils.ToastUtils;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * Created by zzw on 2017/6/5.
 * Version:
 * Des:
 */

public class NetWorkError implements IResponseErrorListener {

    @Override
    public void handleResponseError(Context context, Exception e) {
//        ToastUtils.showToast(context, e.getMessage());

        String msg = "未知错误";
        if (e instanceof UnknownHostException) {
            msg = "网络不可用";
        } else if (e instanceof SocketTimeoutException) {
            msg = "请求网络超时";
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            msg = convertStatusCode(httpException);
        } else if (e instanceof JsonParseException ||
                e instanceof ParseException ||
                e instanceof JSONException ||
                e instanceof JsonIOException) {
            msg = "数据解析错误";
        }

        ToastUtils.showToast(context, msg);
    }


    private String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() == 500) {
            msg = "服务器发生错误";
        } else if (httpException.code() == 404) {
            msg = "请求地址不存在";
        } else if (httpException.code() == 403) {
            msg = "请求被服务器拒绝";
        } else if (httpException.code() == 307) {
            msg = "请求被重定向其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }


}
