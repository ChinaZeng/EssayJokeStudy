package com.zzw.framelibray;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zzw.baselibray.base.BaseActivity;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by zzw on 2017/5/31.
 * Version:
 * Des:应用层继承FrameActivity，在这修改是继承BaseActivity还是BaseSkinActivity   rxJava对应生命周期
 *
 */

public abstract class FrameActivity extends BaseActivity implements LifecycleProvider<ActivityEvent> {


    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
    }

//    protected CompositeSubscription mCompositeSubscription;
//
//    protected void addSubscrebe(Subscription subscription) {
//        if (mCompositeSubscription == null) {
//            mCompositeSubscription = new CompositeSubscription();
//        }
//        mCompositeSubscription.add(subscription);//将所有subscription放入,集中处理
//    }
//
//    protected void unSubscribe() {
//        if (mCompositeSubscription != null) {
//            mCompositeSubscription.unsubscribe();//保证activity结束时取消所有正在执行的订阅
//        }
//    }
//
//    public void unSubscribe(Subscription subscription) {
//        if (subscription != null && !subscription.isUnsubscribed()) {
//            subscription.unsubscribe();//保证activity结束时取消所有正在执行的订阅
//        }
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unSubscribe();//解除订阅
//    }

}
