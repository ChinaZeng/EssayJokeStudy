package com.zzw.essayjokestudy;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.zzw.framelibray.BaseSkinActivity;
import com.zzw.framelibray.skin.SkinManager;
import com.zzw.framelibray.skin.SkinResource;

import java.io.File;

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
        //最好来个不要申请权限的路径
        String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "a.skin";
        SkinManager.getInstance().loadSkin(skinPath);
    }

    public void huifu(View view) {
        SkinManager.getInstance().restoreDefault();
    }


    //换肤回调
    @Override
    public void changeSkin(SkinResource skinResource) {
        super.changeSkin(skinResource);
        Toast.makeText(this, "换肤回调", Toast.LENGTH_SHORT).show();
    }
}
