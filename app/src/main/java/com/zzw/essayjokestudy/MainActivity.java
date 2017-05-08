package com.zzw.essayjokestudy;


import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.zzw.baselibray.ExceptionCrashHandler;
import com.zzw.baselibray.fixBug.FixBugManager;
import com.zzw.framelibray.BaseSkinActivity;
import com.zzw.framelibray.skin.SkinManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import rx.Observable;

public class MainActivity extends BaseSkinActivity {

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView() {
//        fixDexBug();
    }

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


    public void huanfu(View view) {
//        Toast.makeText(this, 2 / 1, Toast.LENGTH_SHORT).show();

        String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "red.skin";

        SkinManager.getInstance().loadSkin(skinPath);

//        SkinManager.getInstance().restoreDefault();//恢复默认


    }

    @Override
    protected void initTitle() {

    }


    @Override
    protected void initData() {

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
