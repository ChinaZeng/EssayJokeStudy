package com.zzw.framelibray.http.retrofit;

import android.content.Context;
import android.os.Handler;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.zzw.framelibray.FrameActivity;
import com.zzw.framelibray.FrameFragment;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by zzw on 2017/6/5.
 * Version:
 * Des:添加生命周期绑定
 */

public class RxUtils {

    public static <T> ObservableTransformer<T, T> applySchedulers(final Object object) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxUtils.<T>bindToLifecycle(object));
            }
        };
    }


    public static <T> LifecycleTransformer<T> bindToLifecycle(Object object) {
        if (object instanceof FrameActivity) {
            return ((FrameActivity) object).<T>bindToLifecycle();
        } else if (object instanceof FrameFragment) {
            return ((FrameFragment) object).<T>bindToLifecycle();
        } else {
            throw new IllegalArgumentException("object isn't FrameActivity or FrameFragment");
        }
    }

}