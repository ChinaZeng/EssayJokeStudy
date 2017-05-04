package com.zzw.essayjokestudy;


import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.zzw.baselibray.ExceptionCrashHandler;
import com.zzw.framelibray.BaseSkinActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends BaseSkinActivity {

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView() {

    }


    @Override
    protected void initTitle() {

    }


    @Override
    protected void initData() {
//        File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();
//        if (crashFile.exists()) {
//            //上传到服务器...
//            try {
//                InputStreamReader reader = new InputStreamReader(new FileInputStream(crashFile));
//                char[] buffer = new char[1024];
//                int len = 0;
//                while ((len = reader.read(buffer)) != -1) {
//                    String str = new String(buffer, 0, len);
//                    Log.e("TAG", str);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }


        //修复Bug
        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.apatch");
        if (fixFile.exists()) {
            try {
                MyApplication.mPatchManager.addPatch(fixFile.getAbsolutePath());
                Toast.makeText(this, "修复成功", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "修复失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
