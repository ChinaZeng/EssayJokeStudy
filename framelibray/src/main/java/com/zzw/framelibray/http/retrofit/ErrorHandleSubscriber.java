package com.zzw.framelibray.http.retrofit;


import com.zzw.baselibray.http.ErrorHandlerFactory;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * Created by zzw on 2017/5/13.
 * Version:
 * Des:retrofit错误信息处理接口
 */
public abstract class ErrorHandleSubscriber<T> implements Observer<T> {
    private ErrorHandlerFactory mHandlerFactory;

    public ErrorHandleSubscriber(ErrorHandlerFactory errorHandlerFactory) {
        this.mHandlerFactory = errorHandlerFactory;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }


    @Override
    public void onComplete() {

    }


    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
        mHandlerFactory.handleError(e);
    }

}

