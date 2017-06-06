package com.zzw.baselibray.http;

import android.content.Context;

/**
 * Created by zzw on 2017/5/13.
 * Version:
 * Des:错误信息处理接口
 */
public interface IResponseErrorListener {
    void handleResponseError(Context context, Exception e);
}
