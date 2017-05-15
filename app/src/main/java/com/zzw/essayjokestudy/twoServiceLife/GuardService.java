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
 * Created by zzw on 2017/5/10.
 * 守护进程 ,双进程通讯
 */

public class GuardService extends Service {




    private static final int GUARD_ID = 5;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub() {

            @Override
            public String getUserName() throws RemoteException {
                return "zzw";
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //提高优先级
        startForeground(GUARD_ID, new Notification());

        //绑定建立链接
        bindService(new Intent(this, MessageService.class), mServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;

    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接上
            L.e("GuardService-->MessageService 建立链接");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开链接  由于杀进程是一个一个杀的   所以断开的时候重新启动
            L.e("GuardService-->MessageService 断开链接");

            startService(new Intent(GuardService.this, MessageService.class));
            bindService(new Intent(GuardService.this, MessageService.class), mServiceConnection, Context.BIND_IMPORTANT);
        }
    };

}
