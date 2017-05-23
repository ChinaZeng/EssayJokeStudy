package com.zzw.baselibray.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzw on 2017/5/23.
 * Version:处理权限请求的工具类
 * Des:
 */

public class PermissionUtils {

    //里面都是静态方法  随意不能直接new对象
    private PermissionUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断是不是6.0以上  M-Marshmallow棉花糖
     *
     * @return
     */
    public static boolean isOverMarshmallow() {

        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 执行成功的方法
     *
     * @param object      哪个对象 Activity还是Fragment
     * @param requestCode 请求码
     */
    public static void executeSucceedMethod(Object object, int requestCode) {
        Class<?> clazz = object.getClass();
        //获取clazz里面的@RequestPermissionSuccess方法
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            RequestPermissionSucceed requestPermissionSuccess = method.getAnnotation(RequestPermissionSucceed.class);
            if (requestPermissionSuccess != null) {
                //根据requestCode和注解方法上面的methodCode对比  执行对应的方法
                int methodCode = requestPermissionSuccess.requestCode();
                if (requestCode == methodCode) {
                    executeMethod(object, method);
                }
            }
        }
    }

    /**
     * 执行失败的方法
     *
     * @param object
     * @param requestCode
     */
    public static void executeFailMethod(Object object, int requestCode) {
        Class<?> clazz = object.getClass();
        //获取clazz里面的@RequestPermissionSuccess方法
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            RequestPermissionFail requestPermissionFail = method.getAnnotation(RequestPermissionFail.class);
            if (requestPermissionFail != null) {
                //根据requestCode和注解方法上面的methodCode对比  执行对应的方法
                int methodCode = requestPermissionFail.requestCode();
                if (requestCode == methodCode) {
                    executeMethod(object, method);
                }
            }
        }
    }


    /**
     * 执行方法
     *
     * @param object 哪个对象里面
     * @param method 那个方法
     */
    private static void executeMethod(Object object, Method method) {
        try {
            method.setAccessible(true);//private修饰的可以执行
            method.invoke(object, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取没有授予的权限
     *
     * @param object             Activity或者Fragment
     * @param requestPermissions 申请的权限列表
     * @return 没有授予过的权限
     */
    public static List<String> getDeniedPermissions(Object object, String[] requestPermissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String requestPermission : requestPermissions) {
            //把没有授予的权限加入集合
            if (ContextCompat.checkSelfPermission(getActivity(object), requestPermission)
                    == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(requestPermission);
            }
        }
        return deniedPermissions;
    }


    /**
     * 获取Context
     *
     * @param object Activity或者Fragment
     * @return
     */
    public static Activity getActivity(Object object) {
        if (object instanceof Activity) {
            return (Activity) object;
        }

        if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        }
        return null;
    }


}
