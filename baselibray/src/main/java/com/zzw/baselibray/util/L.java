package com.zzw.baselibray.util;

import android.graphics.Bitmap;
import android.util.Config;
import android.util.Log;

/**
 * Created by zzw on 2017/5/6.
 * Version:
 * Des:
 */

public class L {
    private static final String TAG = "L";

    private static boolean SHOW_LOG = true;

    public static void e(String msg) {
        e(TAG, msg);
    }


    public static void e(String tag, String msg) {
        if (SHOW_LOG) {
            Log.e(tag, msg + "");
        }
    }

}
