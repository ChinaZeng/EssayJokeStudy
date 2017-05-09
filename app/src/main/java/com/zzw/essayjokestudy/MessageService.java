package com.zzw.essayjokestudy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.zzw.framelibray.skin.L;

/**
 * Created by zzw on 2017/5/9.
 * 模拟扣扣聊天通讯  代码需要轻
 */

public class MessageService extends Service {
    private static final String TAG = "MessageService";

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

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
