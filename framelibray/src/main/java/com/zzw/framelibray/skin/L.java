package com.zzw.framelibray.skin;

import android.util.Log;

/**
 * Created by zzw on 2017/5/6.
 * Version:
 * Des:
 */

public class L {
    private static final String TAG = "L";

    private static boolean SHOW_LOG = false;

    public static void e(String msg) {
        e(TAG, msg);
    }


    public static void e(String tag, String msg) {
        if (SHOW_LOG) {
            Log.e(tag, msg + "");
        }
    }

}
