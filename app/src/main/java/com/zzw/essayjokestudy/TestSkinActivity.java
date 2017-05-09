package com.zzw.essayjokestudy;

import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzw.baselibray.util.L;
import com.zzw.framelibray.BaseSkinActivity;
import com.zzw.framelibray.skin.SkinManager;

import java.io.File;
import java.lang.reflect.Method;

public class TestSkinActivity extends BaseSkinActivity {


    @Override
    protected int initLayoutId() {
        return R.layout.activity_test_skin;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    public void huanfu(View view) {
        String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "a.skin";
        L.e(skinPath);
        SkinManager.getInstance().loadSkin(skinPath);
    }
}
