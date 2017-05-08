package com.zzw.essayjokestudy;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zzw.baselibray.util.L;

/**
 * Created by zzw on 2017/5/6.
 * Version:
 * Des:
 */

public class TestService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        L.e("TestService_onBind");
        return new UserBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        L.e("TestService_onUnbind");
        return super.onUnbind(intent);
    }

    private class UserBinder extends UserAidl.Stub {

        @Override
        public String getUserName() throws RemoteException {
            return "userName";
        }

        @Override
        public String getPassWord() throws RemoteException {
            return "pwd";
        }
    }
}
