package com.zzw.baselibray.plugin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static java.lang.reflect.Proxy.newProxyInstance;

/**
 * Created by zzw on 2017/5/11.
 * Version:
 * Des: 通过hook绕过AndroidManifest注册启动Activity  在application注册   查看BaseApplication
 */

public class HookStartActivityUtil {

    private static final String TAG = "HookStartActivityUtil";
    private Context mContext;
    private Class<?> mProxyClass;//代理Activity
    private final String EXTRA_ORIGIN_INTENT = "EXTRA_ORIGIN_INTENT";

    public HookStartActivityUtil() {
    }


    public HookStartActivityUtil(Context mContext, Class<?> mProxyClass) {
        this.mContext = mContext.getApplicationContext();//怕内存泄露（静态 单例会出现）
        this.mProxyClass = mProxyClass;
    }

    public void hookHandleLaunchActivity() throws Exception {
        //1.拿到ActivityThread类里面的sCurrentActivityThread
        //private static volatile ActivityThread sCurrentActivityThread;
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
        sCurrentActivityThreadField.setAccessible(true);
        Object activityThread = sCurrentActivityThreadField.get(null);

        //2.获取ActivityThread里面的Handler  mH
        //final H mH = new H();
        Field mHField = activityThreadClass.getDeclaredField("mH");
        mHField.setAccessible(true);
        Object mHObject = mHField.get(activityThread);

        //3.hook handleLaunchActivity（）
        //给这个Handler设置CallBack回调，也只能通过反射
        Class handlerClass = Class.forName("android.os.Handler");
        Field mCallbackField = handlerClass.getDeclaredField("mCallback");
        mCallbackField.setAccessible(true);

        //定义callback来hook
        mCallbackField.set(mHObject, new HandlerCallBack());

        //设置进去
        mHField.set(mHObject, handlerClass);
    }

    public void hookStartActivity() throws Exception {

        //拿到当前应用的IActivityManager

        //1.获取ActivityManagerNative里面的private static final Singleton<IActivityManager> gDefault=xxx;
        Class<?> amnClass = Class.forName("android.app.ActivityManagerNative");
        //获取属性gDefault
        Field gDefaultField = amnClass.getDeclaredField("gDefault");
        gDefaultField.setAccessible(true);
        Object gDefaultObj = gDefaultField.get(null);//静态的  可以传入null  得到Singleton对象

        //2.获取gDefault(Singleton)里面的mInstance属性  IActivityManager是gDefault里面的mInstance
        Class<?> singleClass = Class.forName("android.util.Singleton");
        Field mInstanceField = singleClass.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);
        Object imaObj = mInstanceField.get(gDefaultObj);//拿到ActivityManagerNative里面的gDefault里面的mInstance（IActivityManager）

        imaObj = newProxyInstance(mContext.getClass().getClassLoader(),
                imaObj.getClass().getInterfaces(), new HookStartActivityInvocationHandler(imaObj));


        //下面这种写也是可以的
//        Class<?> iamClass = Class.forName("android.app.IActivityManager");
//        imaInstance = Proxy.newProxyInstance(HookStartActivityUtil.class.getClassLoader(),
//                new Class<?>[]{imaObj}, new HookStartActivityInvocationHandler(imaObj));
//        //第二个参数不能这样写:iamClass.getInterfaces()，测试请打印一下下面即可知道
//
////        Class<?>[] classes = iamClass.getInterfaces();
////        for (Class<?> aClass : classes) {
////            Log.e(TAG, aClass + "");
////        }
////        Log.e(TAG, "------");
////        classes= new Class<?>[]{iamClass};
////        for (Class<?> aClass : classes) {
////            Log.e(TAG, aClass + "");
////        }


        //重新指定ActivityManagerNative  gDefault(Singleton)里面的mInstance,也就是IActivityManager
        mInstanceField.set(gDefaultObj, imaObj);

    }


    /**
     * 开始启动创建Activity的拦截
     *
     * @param msg
     */
    private void handleLaunchActivity(Message msg) {

//        Message消息内容   ActivityThread中查看

//        ActivityClientRecord r = new ActivityClientRecord();
//        r.token = token;
//        r.ident = ident;
//        r.intent = intent;
//        r.referrer = referrer;
//        r.voiceInteractor = voiceInteractor;
//        r.activityInfo = info;
//        r.compatInfo = compatInfo;
//        r.state = state;
//        r.persistentState = persistentState;
//        r.pendingResults = pendingResults;
//        r.pendingIntents = pendingNewIntents;
//        r.startsNotResumed = notResumed;
//        r.isForward = isForward;
//        r.profilerInfo = profilerInfo;
//        r.overrideConfig = overrideConfig;
//        updatePendingConfiguration(curConfig);
//        sendMessage(H.LAUNCH_ACTIVITY, r);


        try {
            Object record = msg.obj;
            //1.拿到要替换的intent

            Field intentField = record.getClass().getDeclaredField("intent");
            //2.从safeIntent中获取原来的originIntent
            intentField.setAccessible(true);
            Intent intent = (Intent) intentField.get(record);
            Intent originIntent = intent.getParcelableExtra(EXTRA_ORIGIN_INTENT);

            if (originIntent != null) {
                //3.重新设置进去
                intentField.set(record, originIntent);
            }


            // 兼容AppCompatActivity报错问题
            Class<?> forName = Class.forName("android.app.ActivityThread");
            Field field = forName.getDeclaredField("sCurrentActivityThread");
            field.setAccessible(true);
            Object activityThread = field.get(null);
            Method getPackageManager = activityThread.getClass().getDeclaredMethod("getPackageManager");
            Object iPackageManager = getPackageManager.invoke(activityThread);

            PackageManagerHandler handler = new PackageManagerHandler(iPackageManager);
            Class<?> iPackageManagerIntercept = Class.forName("android.content.pm.IPackageManager");
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class<?>[]{iPackageManagerIntercept}, handler);

            // 获取 sPackageManager 属性
            Field iPackageManagerField = activityThread.getClass().getDeclaredField("sPackageManager");
            iPackageManagerField.setAccessible(true);
            iPackageManagerField.set(activityThread, proxy);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class PackageManagerHandler implements InvocationHandler {
        private Object mActivityManagerObject;

        public PackageManagerHandler(Object mActivityManagerObject) {
            this.mActivityManagerObject = mActivityManagerObject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("getActivityInfo")) {
                ComponentName componentName = new ComponentName(mContext, mProxyClass);
                args[0] = componentName;
            }

            return method.invoke(mActivityManagerObject, args);
        }
    }

    private class HookStartActivityInvocationHandler implements InvocationHandler {

        //方法的执行者
        private Object mObject;

        public HookStartActivityInvocationHandler(Object o) {
            this.mObject = o;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.e(TAG, method.getName());

            //替换Intent过AndroidManifest.xml检测
            if (method.getName().equals("startActivity")) {
                //1.获取原来的Intent
                Intent originIntent = (Intent) args[2];

                //2.创建一个Intent
                Intent safeIntent = new Intent(mContext, mProxyClass);
                args[2] = safeIntent;

                //3.绑定原来的Intent
                safeIntent.putExtra(EXTRA_ORIGIN_INTENT, originIntent);
            }

            return method.invoke(mObject, args);
        }
    }


    private class HandlerCallBack implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            //  public static final int LAUNCH_ACTIVITY  = 100;
            if (msg.what == 100) {
                handleLaunchActivity(msg);
            }

            return false;
        }
    }

}
