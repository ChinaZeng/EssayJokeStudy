package com.zzw.baselibray.permission;

import android.app.Activity;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.List;


/**
 * Created by zzw on 2017/5/23.
 * Version:权限申请辅助类
 * Des:
 */


//注意:必须在Activity或者Fragment里面重写onRequestPermissionsResult方法调用 PermissionHelper.requestPermissionsResult(this,requestCode, permissions);
//      @Override
//      public void onRequestPermissionsResult(int requestCode,@NonNull String[]permissions,@NonNull int[]grantResults){
//               super.onRequestPermissionsResult(requestCode,permissions,grantResults);
//               PermissionHelper.requestPermissionsResult(this,requestCode,permissions);
//      }

public class PermissionHelper {

    private Object mObject;
    private int mRequestCode;
    private String[] mRequestPermissions;


    private PermissionHelper(@NonNull Object object) {
        this.mObject = object;
    }

    //直接传参数
    public static void requestPermission(@NonNull Activity activity,
                                         @IntRange(from = 0) int requestCode,
                                         @NonNull String... permissions) {
        PermissionHelper.with(activity)
                .requestPermissions(permissions)
                .requestCode(requestCode)
                .request();
    }

    //链式传Activity
    public static PermissionHelper with(@NonNull Activity activity) {
        return new PermissionHelper(activity);
    }

    //链式传fragment
    public static PermissionHelper with(@NonNull android.support.v4.app.Fragment fragment) {
        return new PermissionHelper(fragment);
    }

    //链式传requestCode
    public PermissionHelper requestCode(@IntRange(from = 0) int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    //链式传requestPermissons
    public PermissionHelper requestPermissions(@NonNull String... permissions) {
        this.mRequestPermissions = permissions;
        return this;
    }

    //真正判断和发起请求权限
    public void request() {
        //判断是不是6.0以上


        //如果不是6.0以上的就直接执行
        if (!PermissionUtils.isOverMarshmallow()) {
            PermissionUtils.executeSucceedMethod(mObject, mRequestCode);
        } else {//如果是6.0以上的  先判断权限是否授予
            //获取到系统没有给app授予的权限
            List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(mObject, mRequestPermissions);
            if (deniedPermissions.size() == 0) {//如果全部授予了直接执行方法
                PermissionUtils.executeSucceedMethod(mObject, mRequestCode);
            } else {//哪几项权限没有就申请哪几项
                ActivityCompat.requestPermissions(PermissionUtils.getActivity(mObject),
                        deniedPermissions.toArray(new String[deniedPermissions.size()]), mRequestCode);
            }
        }
    }

    /**
     * 处理申请权限的回调
     *
     * @param obj         Activity或者Fragment
     * @param requestCode
     * @param permissions
     */
    public static void requestPermissionsResult(Object obj, int requestCode, String[] permissions) {
        //再次获取没有授予的权限
        List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(obj, permissions);
        if (deniedPermissions.size() == 0) {//表示权限都通过了
            PermissionUtils.executeSucceedMethod(obj, requestCode);
        } else {//表示用户不同意你申请的权限中的某一项权限
            PermissionUtils.executeFailMethod(obj, requestCode);
        }
    }


}

//原始调用方式
//    private final static int CALL_PHONE_REQUEST_CODE = 0x001;
//
//    private void permisson() {
//        //context和检测权限的字符串   返回值只有两个  授予和拒绝
//        int isPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
//        if (isPermission == PackageManager.PERMISSION_GRANTED) {//授予权限
//        } else {//没有授予权限
//            //我们申请
////            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_REQUEST_CODE);
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_REQUEST_CODE);
//
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CALL_PHONE_REQUEST_CODE) {//申请打电话成功
//            if (grantResults.length > 0) {
//                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){//授予权限成功
//
//                }
//            }
//        }
//    }
