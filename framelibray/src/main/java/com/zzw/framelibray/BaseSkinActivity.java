package com.zzw.framelibray;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;

import com.zzw.baselibray.base.BaseActivity;
import com.zzw.framelibray.skin.attr.SkinAttr;
import com.zzw.framelibray.skin.attr.SkinView;
import com.zzw.framelibray.skin.support.SkinAppCompatViewInflater;
import com.zzw.framelibray.skin.support.SkinAttrSupport;

import java.util.List;


/**
 * Created by zzw on 2017/5/4.
 * 换肤层
 */

public abstract class BaseSkinActivity extends BaseActivity implements LayoutInflaterFactory {
    private static final String TAG = "BaseSkinActivity";

    private SkinAppCompatViewInflater mSkinAppCompatViewInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //拦截到View的创建
        //设置自己的LayoutInflaterFactory 自己管理View的创建
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory(layoutInflater, this);
        super.onCreate(savedInstanceState);
    }


    @Override
    public final View onCreateView(View parent, String name,
                                   Context context, AttributeSet attrs) {
        //1.创建View
        View view = createView(parent, name, context, attrs);
        Log.e(TAG, view + "");

        //2.解析属性 src textColor background  自定义属性
        if (view != null) {
            //2.1 一个Activity对应多个SkinView
            List<SkinAttr> skinAttrList = SkinAttrSupport.getSkinAttrs(context, attrs);
            SkinView skinView = new SkinView(view, skinAttrList);

            //3.统一交给SkinManager管理
            managerSkinView(skinView);

        }
        return view;
    }


    /**
     * 同意管理SkinView
     *
     * @param skinView
     */
    private void managerSkinView(SkinView skinView) {

    }


    /**
     * 创建View
     *
     * @param parent
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        final boolean isPre21 = Build.VERSION.SDK_INT < 21;

        if (mSkinAppCompatViewInflater == null) {
            mSkinAppCompatViewInflater = new SkinAppCompatViewInflater();//兼容V21
        }

        // We only want the View to inherit its context if we're running pre-v21
        final boolean inheritContext = isPre21 && shouldInheritContext((ViewParent) parent);

        return mSkinAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                isPre21, /* Only read android:theme pre-L (L+ handles this anyway) */
                true /* Read read app:theme as a fallback at all times for legacy reasons */
        );
    }


    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        final View windowDecor = getWindow().getDecorView();
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == windowDecor || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }
}
