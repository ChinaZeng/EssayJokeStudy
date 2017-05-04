package com.zzw.baselibray.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zzw on 2017/5/4.
 * 点击之后是否要执行网络检测
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckNet {
    String value() default "亲，您的网络链接有问题哦!";
}
