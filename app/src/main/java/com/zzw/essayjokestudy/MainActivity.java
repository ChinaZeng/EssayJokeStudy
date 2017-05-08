package com.zzw.essayjokestudy;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zzw.baselibray.ExceptionCrashHandler;
import com.zzw.baselibray.fixBug.FixBugManager;
import com.zzw.baselibray.ioc.OnClick;
import com.zzw.framelibray.BaseSkinActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends BaseSkinActivity {

    private UserAidl mUserAidl;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mUserAidl = UserAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView() {

    }

    /**
     * 热修复测试
     */
    private void fixDexBug() {
        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.dex");
        if (fixFile.exists()) {
            FixBugManager manager = new FixBugManager(this);
            try {
                manager.fixDex(fixFile.getAbsolutePath());
                Toast.makeText(this, "修复成功!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "修复失败!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void initTitle() {

    }


    @OnClick({R.id.bind, R.id.unBind, R.id.getUserName, R.id.getPassWord})
    private void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.bind:
                    startActivity(TestSkinActivity.class);
//                    bindService();
                    break;
                case R.id.getUserName:
                    Toast.makeText(this, mUserAidl == null ? "请先绑定" : mUserAidl.getUserName(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.getPassWord:
                    Toast.makeText(this, mUserAidl == null ? "请先绑定" : mUserAidl.getPassWord(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.unBind:
                    unbindService();
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void initData() {

    }

    /**
     * 绑定Service
     */
    private void bindService() {
        Intent intent = new Intent();
        intent.setAction("com.study.aidl.user");
        // 在Android 5.0之后google出于安全的角度禁止了隐式声明Intent来启动Service.也禁止使用Intent filter.否则就会抛个异常出来
        intent.setPackage("com.zzw.essayjokestudy");
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }


    /**
     * 解绑Service
     */
    private void unbindService() {
        if (mUserAidl != null) {
            unbindService(mConnection);
            mUserAidl = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService();
    }

    /**
     * 上传错误信息
     */
    private void uploadException() {
        File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();
        if (crashFile.exists()) {
            //上传到服务器...
            try {
                //打印错误信息
                InputStreamReader reader = new InputStreamReader(new FileInputStream(crashFile));
                char[] buffer = new char[1024];
                int len = 0;
                while ((len = reader.read(buffer)) != -1) {
                    String str = new String(buffer, 0, len);
                    Log.e("TAG", str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
