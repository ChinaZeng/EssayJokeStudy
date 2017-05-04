package com.zzw.baselibray.ioc;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.view.View;

/**
 * Created by zzw on 2017/5/4.
 * View帮助类
 */

public class ViewHelper {

    private Activity mActivity;
    private View mView;

    public ViewHelper(Activity activity) {
        this.mActivity = activity;
    }

    public ViewHelper(View v) {
        this.mView = v;
    }

    public View findViewById(@IdRes int id) {

        return mActivity == null ? mView.findViewById(id) : mActivity.findViewById(id);
    }
}
