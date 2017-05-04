package com.zzw.baselibray.ioc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by zzw on 2017/5/4.
 * ioc注入工具类
 */

public class ViewUtils {

    public static void inject(Activity activity) {
        inject(new ViewHelper(activity), activity);
    }

    //为了兼容View
    public static void inject(View v) {
        inject(new ViewHelper(v), v);
    }

    //为了兼容Fragment
    public static void inject(View v, Object o) {
        inject(new ViewHelper(v), o);
    }


    /**
     * 最终都调用这个方法
     *
     * @param helper View的帮助类  通过这个类根据id找到相应View
     * @param o      相关对象  Activity  view 等  从那个类传进来的
     */
    private static void inject(ViewHelper helper, Object o) {
        injectField(helper, o);
        injectMethod(helper, o);
    }

    /**
     * 通过@ViewById得到id注入相应的View
     *
     * @param helper View帮助类
     * @param o      相关对象  Activity  view 等  从那个类传进来的
     */
    private static void injectField(ViewHelper helper, Object o) {
        //1.获取到Object中所有的带有@ViewById的字段
        Class<?> clazz = o.getClass();
        //获取所有的字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //2.获取到相应的value值，也就是id值得到相应的View
            ViewById viewById = field.getAnnotation(ViewById.class);
            if (viewById != null) {
                int viewId = viewById.value();
                View view = helper.findViewById(viewId);
                if (view != null) {
                    try {
                        //3.设置字段值，也就是给字段赋值
                        field.setAccessible(true);//为了使不被修饰符梭影响
                        field.set(o, view);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 设置点击事件
     *
     * @param helper View帮助类
     * @param o      相关对象  Activity  view 等  从那个类传进来的
     */
    private static void injectMethod(ViewHelper helper, Object o) {
        //1.获取到所有带有@OnClick的方法
        Class<?> clazz = o.getClass();
        Method[] methods = clazz.getDeclaredMethods();//获取所有方法
        for (Method method : methods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                //网络检测
                CheckNet checkNet = method.getAnnotation(CheckNet.class);
                String hint = null;
                if (checkNet != null) {
                    hint = checkNet.value();
                }

                //2.获取到相应的value值,也就是要设置点击时间的id数组
                int[] values = onClick.value();
                for (int viewId : values) {
                    //3.通过id获取到相应的Vie,然后设置点击事件
                    View view = helper.findViewById(viewId);
                    if (view != null) {
                        view.setOnClickListener(new DeclaredOnClickListener(method, o, hint));
                    }
                }
            }
        }
    }

    private static class DeclaredOnClickListener implements View.OnClickListener {

        //设置点击事件的方法
        private Method mMethod;
        //在那个类中
        private Object mObject;
        //是否检查网络
        private String mNoNetHint;

        public DeclaredOnClickListener(Method method, Object o, String hint) {
            this.mMethod = method;
            this.mObject = o;
            this.mNoNetHint = hint;
        }

        @Override
        public void onClick(View v) {
            try {
                mMethod.setAccessible(true);//所有修饰符都可以搞事
                if (mNoNetHint != null && !isNetConnected(v.getContext())) {
                    Toast.makeText(v.getContext(), mNoNetHint, Toast.LENGTH_SHORT).show();
                    return;
                }
                mMethod.invoke(mObject, v);//可以避免点击闪退
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    mMethod.invoke(mObject, (Object[]) null);//当方法体里面没有参数时候调用改方法，执行没有方法体的修饰的函数
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    /**
     * 检测网络是否连接
     *
     * @return
     */
    private static boolean isNetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo ni : infos) {
                    if (ni.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
