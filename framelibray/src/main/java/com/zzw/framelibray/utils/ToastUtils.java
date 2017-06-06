package com.zzw.framelibray.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by zzw on 2017/6/5.
 * Version:
 * Des:避免重复toast
 */

public class ToastUtils {

    private static Toast mToast;

    public static void showToast(Context context, String text) {
        if (TextUtils.isEmpty(text))
            return;

        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
