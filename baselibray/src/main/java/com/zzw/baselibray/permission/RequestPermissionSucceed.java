package com.zzw.baselibray.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zzw on 2017/5/23.
 * Version:权限请求成功注解
 * Des:
 */

@Retention(RetentionPolicy.RUNTIME)//运行的时候检测     SOURCE是编译的时候检测
@Target(ElementType.METHOD)//方法上面
public @interface RequestPermissionSucceed {
    int requestCode();//请求码
}
