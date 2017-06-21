package com.zzw.essayjokestudy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zzw.baselibray.permission.PermissionHelper;
import com.zzw.baselibray.permission.RequestPermissionFail;
import com.zzw.baselibray.permission.RequestPermissionSucceed;
import com.zzw.baselibray.popwindow.PublicPopupWindow;
import com.zzw.essayjokestudy.utils.ImageUtil;
import com.zzw.framelibray.FrameActivity;
import com.zzw.framelibray.recyclerview.adapter.CommonRecyclerAdapter;
import com.zzw.framelibray.recyclerview.adapter.GlideHolderImageLoader;
import com.zzw.framelibray.recyclerview.adapter.OnItemClickListener;
import com.zzw.framelibray.recyclerview.adapter.ViewHolder;
import com.zzw.framelibray.selectimage.ImageSelector;
import com.zzw.framelibray.selectimage.SelectImageActivity;

import java.io.File;
import java.util.List;


/**
 * Created by zzw on 2017/6/15.
 * Version:
 * Des:
 */

public class ImageZipActivity extends FrameActivity {

    private RecyclerView recy1;
    private RecyclerView recy2;

    private LinearLayout root;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_zip_image;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        final View bt = findViewById(R.id.select_image);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSelector.create().multi().showCamera(true).count(8).start(ImageZipActivity.this, 5);
            }
        });

        recy1 = (RecyclerView) findViewById(R.id.recy1);
        recy2 = (RecyclerView) findViewById(R.id.recy2);

        recy1.setLayoutManager(new GridLayoutManager(this, 3));
        recy2.setLayoutManager(new GridLayoutManager(this, 3));

        root = (LinearLayout) findViewById(R.id.root);

    }


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode != RESULT_CANCELED) {
            final List<String> pathList = data.getStringArrayListExtra(SelectImageActivity.EXTRA_RESULT);
            RecyAdapter adapter = new RecyAdapter(this, pathList, R.layout.item_img);
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    String path = pathList.get(position);
                    Bitmap bitmap = ImageUtil.decodeFile(path, 800);
                    int a = ImageUtil.compressBitmap(bitmap, 75,
                            Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                                    + new File(path).getName()
                    );
                    Log.e("zzz", a + "");

                }
            });
            recy1.setAdapter(adapter);
        }
    }

    @Override
    protected void initData() {
        PermissionHelper.with(this)
                .requestCode(1)
                .requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.requestPermissionsResult(this, requestCode, permissions);
    }

    @RequestPermissionSucceed(requestCode = 1)
    private void a() {
        Toast.makeText(this, "权限申请ok", Toast.LENGTH_SHORT).show();
    }

    @RequestPermissionFail(requestCode = 1)
    private void b() {
        Toast.makeText(this, "权限申请是失败", Toast.LENGTH_SHORT).show();
    }


    private class RecyAdapter extends CommonRecyclerAdapter<String> {
        public RecyAdapter(Context context, List<String> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void convert(ViewHolder holder, String item, int position) {
            holder.setImageByUrl(R.id.item_img, new GlideHolderImageLoader(item));
        }
    }


}
