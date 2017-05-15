package com.zzw.essayjokestudy.twoServiceLife;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.zzw.essayjokestudy.ProcessConnection;
import com.zzw.framelibray.skin.L;

/**
 * Created by zzw on 2017/5/9.
 * 模拟扣扣聊天通讯  代码需要轻
 */

public class MessageService extends Service {


    //        startService(new Intent(this, MessageService.class));
//        startService(new Intent(this, GuardService.class));
//
//        //大于等于5.0的才启动JobWakeUpService
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            startService(new Intent(this, JobWakeUpService.class));
//        }

    private static final String TAG = "MessageService";

    private static final int MESSAGE_FOREGROUND_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);
                        L.e(TAG, "等待接受消息");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //提高优先级
        startForeground(MESSAGE_FOREGROUND_ID, new Notification());
        //绑定建立链接
        bindService(new Intent(this, GuardService.class), mServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub() {

            @Override
            public String getUserName() throws RemoteException {
                return null;
            }
        };
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接上
            L.e("MessageService-->GuardService 建立链接");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开链接  由于杀进程是一个一个杀的   所以断开的时候重新启动
            L.e("MessageService-->GuardService 断开链接-->正在重启和重新绑定");
            startService(new Intent(MessageService.this, GuardService.class));
            bindService(new Intent(MessageService.this, GuardService.class), mServiceConnection, Context.BIND_IMPORTANT);
        }
    };


}
