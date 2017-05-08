package com.zzw.essayjokestudy;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.lang.reflect.Method;

public class TestSkinActivity extends AppCompatActivity {

    private ImageView mTestImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_skin);
        mTestImg = (ImageView) findViewById(R.id.test_img);
    }

    public void huanfu(View view) {
        try {
            //读取本地的a.skin
            Resources superRes = getResources();
            //创建一个AssetManager
            //AssetManager assetManager = new AssetManager(); hide的调用不了  只有用反射调用
            AssetManager assetManager = AssetManager.class.newInstance();
            //添加本地下载好的资源皮肤
            //  assetManager.addAssetPath(String path);// 也是hide的调用不了  继续用反射执行该方法
            Method addAssetPathMethod = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            addAssetPathMethod.invoke(assetManager,
                    Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "a.skin");

            Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(),
                    superRes.getConfiguration());

            int drawableId = resources.getIdentifier("img_src", "mipmap", "com.zzw.t2");
            Drawable imgSrcDrawable = resources.getDrawable(drawableId);
            mTestImg.setImageDrawable(imgSrcDrawable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
