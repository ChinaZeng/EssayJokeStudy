package com.zzw.framelibray.http;

import android.content.Context;
import android.widget.Toast;

import com.zzw.baselibray.http.IResponseErrorListener;
import com.zzw.framelibray.utils.ToastUtils;

/**
 * Created by zzw on 2017/6/5.
 * Version:
 * Des:
 */

public class NetWorkError implements IResponseErrorListener {

    @Override
    public void handleResponseError(Context context, Exception e) {
//        ToastUtils.showToast(context, e.getMessage());
        ToastUtils.showToast(context, "连接网络失败");
    }

}
