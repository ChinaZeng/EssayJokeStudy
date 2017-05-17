package com.zzw.essayjokestudy;


import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zzw.baselibray.ExceptionCrashHandler;
import com.zzw.baselibray.dialog.AlertDialog;
import com.zzw.baselibray.fixBug.FixBugManager;
import com.zzw.baselibray.http.HttpUtils;
import com.zzw.baselibray.ioc.OnClick;
import com.zzw.essayjokestudy.bean.Person;
import com.zzw.essayjokestudy.utils.PathchUtils;
import com.zzw.framelibray.BaseSkinActivity;
import com.zzw.framelibray.db.DaoSupportFactory;
import com.zzw.framelibray.selectimage.ImageSelector;
import com.zzw.framelibray.selectimage.SelectImageActivity;
import com.zzw.framelibray.skin.SkinManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissonItem;


public class MainActivity extends BaseSkinActivity {


    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView() {
//        fixDexBug();
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


    public void huanfu(View view) {
//        Toast.makeText(this, 2 / 1, Toast.LENGTH_SHORT).show();

        String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "red.skin";

        SkinManager.getInstance().loadSkin(skinPath);

//        SkinManager.getInstance().restoreDefault();//恢复默认


    }

    @Override
    protected void initTitle() {

    }


    @OnClick({R.id.bind, R.id.unBind, R.id.getUserName, R.id.getPassWord})
    private void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.bind:
                    ImageSelector.create().single().showCamera(true).start(this, 5);

//                    final AlertDialog dialog = new AlertDialog.Builder(this)
//                            .setContentView(R.layout.dialog_)
//                            .setText(R.id.tv, "测试")
//                            .formBottom(true)
//                            .fullWidth()
//                            .create();
//
//                    dialog.setOnclickListener(R.id.tv, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            TextView textView = dialog.getView(R.id.tv);
//                            Toast.makeText(v.getContext(), textView.getText(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                    dialog.show();

//                    DaoSupportFactory.getFactory().getDao(Person.class).insert(new Person("zzw", 23, "zzw@qq.com"));
                    break;
                case R.id.getUserName:
                    ImageSelector.create().multi().showCamera(true).count(8).start(this, 5);

//                    List<Person> d = new ArrayList<>();
//
//                    for (int i = 0; i < 20; i++) {
//                        d.add(new Person("zzw", i, "zzw@qq.com" + i));
//                    }
//                    DaoSupportFactory.getFactory().getDao(Person.class).insert(d);
                    break;
                case R.id.getPassWord:
                    List<Person> list = DaoSupportFactory.getFactory().getDao(Person.class).querySupport().query();
                    for (Person person : list) {
                        Log.e("zzz", person.toString());
                    }
                    break;
                case R.id.unBind:
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String patchPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "down" + File.separator + "version_1.0_2.0.patch";

    private String newApkPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "down" + "version2.0.apk";

    @Override
    protected void initData() {
        List<PermissonItem> permissonItems = new ArrayList<PermissonItem>();
        permissonItems.add(new PermissonItem(Manifest.permission.READ_EXTERNAL_STORAGE, "读取内存卡", R.drawable.permission_ic_memory));
        permissonItems.add(new PermissonItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "写入内存卡", R.drawable.permission_ic_location));
        HiPermission.create(MainActivity.this)
                .permissions(permissonItems)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        showToast("用户关闭权限申请");
                    }

                    @Override
                    public void onFinish() {
                        showToast("所有权限申请完成");
                    }

                    @Override
                    public void onDeny(String permisson, int position) {
                        showToast(permisson + "");
                    }

                    @Override
                    public void onGuarantee(String permisson, int position) {
                        showToast(permisson + "");
                    }
                });


    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 增量更新
     */
    private void install() {
        //1.访问后台接口，需不需要更新版本


        //2.需要更新版本，提示用户需要下载
        //也可以直接下载 提示用户


        //3.下载差分包，调用我们的方法去合并生成新的apk,是一个耗时操作
        //getPackageResourcePath()获取当前apk路径
        PathchUtils.combine(getPackageResourcePath(), newApkPath, patchPath);

        //4.校验签名


        //5.安装最新版
        installAPK(this, newApkPath);
    }

    /**
     * 绑定Service
     */
    private void bindService() {


//        Intent intent = new Intent();
//        intent.setAction("com.study.aidl.user");
//        // 在Android 5.0之后google出于安全的角度禁止了隐式声明Intent来启动Service.也禁止使用Intent filter.否则就会抛个异常出来
//        intent.setPackage("com.zzw.essayjokestudy");
//        bindService(intent, mConnection, BIND_AUTO_CREATE);
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


    /**
     * 下载完成后自动安装apk
     */
    public boolean installAPK(Context context, String apkPath) {

        File apkFile = new File(apkPath);
        if (!apkFile.exists())
            return false;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            String uri = BuildConfig.APPLICATION_ID + ".fileProvider";
            Uri contentUri = FileProvider.getUriForFile(context, uri, apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);

        return true;
    }

}
