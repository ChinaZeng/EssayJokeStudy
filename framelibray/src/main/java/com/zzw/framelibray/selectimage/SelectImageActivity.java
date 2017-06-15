package com.zzw.framelibray.selectimage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zzw.baselibray.base.BaseActivity;
import com.zzw.framelibray.BaseSkinActivity;
import com.zzw.framelibray.DefaultNavigationBar;
import com.zzw.framelibray.FrameActivity;
import com.zzw.framelibray.R;
import com.zzw.framelibray.utils.StatusBarUtil;

import java.util.ArrayList;

/**
 * Created by zzw on 2017/5/10.
 * Version:
 * Des:选择图片   多选不会出现相机按钮   单选可以配置是否出现
 */

public class SelectImageActivity extends FrameActivity implements View.OnClickListener, SelectImageListener {


    // 加载所有的数据  LoaderManager使用
    private static final int LOADER_TYPE = 0x0021;

    //返回数据
    public static final int IMAGE_RESULT = 0X0022;
    // 返回选择图片列表的EXTRA_KEY
    public static final String EXTRA_RESULT = "EXTRA_RESULT";

    // 带过来的Key
    // 是否显示相机的EXTRA_KEY
    public static final String EXTRA_SHOW_CAMERA = "EXTRA_SHOW_CAMERA";
    // 总共可以选择多少张图片的EXTRA_KEY
    public static final String EXTRA_SELECT_COUNT = "EXTRA_SELECT_COUNT";
    // 原始的图片路径的EXTRA_KEY
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "EXTRA_DEFAULT_SELECTED_LIST";
    // 选择模式的EXTRA_KEY
    public static final String EXTRA_SELECT_MODE = "EXTRA_SELECT_MODE";


    /**
     * 可以传递过来的参数
     * ---------------------------------------------------------------
     */
    // 选择图片的模式 - 多选
    public static final int MODE_MULTI = 0x0011;
    // 选择图片的模式 - 单选
    public static int MODE_SINGLE = 0x0012;
    // 单选或者多选，int类型的type
    private int mMode = MODE_MULTI;
    // int 类型的图片张数
    private int mMaxCount = 8;
    // boolean 类型的是否显示拍照按钮
    private boolean mShowCamera = true;
    // ArraryList<String> 已经选择好的图片
    private ArrayList<String> mResultList;
    /*
     * ---------------------------------------------------------------
     */

    private RecyclerView mImageListRv;
    private TextView mSelectNumTv;
    private TextView mSelectPreview;
    private TextView mSelectFinish;
    private ProgressBar mLoadingProgress;
    private View mOpBar;
    private View mContentRl;

    @Override
    protected int initLayoutId() {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        return R.layout.activity_select_image;
    }

    @Override
    protected void initTitle() {
        DefaultNavigationBar navigationBar = new DefaultNavigationBar.Builder(this)
                .setTitle("所有图片")
                .builder();
//        navigationBar.findViewById();

        // 改变状态栏的颜色
        StatusBarUtil.statusBarTintColor(this, Color.parseColor("#261f1f"));
    }

    @Override
    protected void initView() {
        mImageListRv = (RecyclerView) findViewById(R.id.image_list_rv);
        mSelectNumTv = (TextView) findViewById(R.id.select_num);
        mSelectPreview = (TextView) findViewById(R.id.select_preview);
        mSelectFinish = (TextView) findViewById(R.id.select_finish);
        mLoadingProgress = (ProgressBar) findViewById(R.id.loading_progress);
        mOpBar = findViewById(R.id.op_bar);
        mContentRl = findViewById(R.id.content_rl);

        mSelectPreview.setOnClickListener(this);
        mSelectFinish.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        setLoadState(false);
        // 1.获取传递过来的参数
        Intent intent = getIntent();
        mMode = intent.getIntExtra(EXTRA_SELECT_MODE, mMode);
        mMaxCount = intent.getIntExtra(EXTRA_SELECT_COUNT, mMaxCount);
        mShowCamera = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, mShowCamera);

        if (mMode == MODE_MULTI) {//多选
            mOpBar.setVisibility(View.VISIBLE);
            mResultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
            if (mResultList == null) {
                mResultList = new ArrayList<>();
            }
            int nowSize = mResultList.size();
            mSelectNumTv.setText((nowSize >= mMaxCount ? mMaxCount : nowSize) + "/" + mMaxCount);

        } else if (mMode == MODE_SINGLE) {//单选
            mOpBar.setVisibility(View.GONE);
        }

        // 2.初始化本地图片数据
        initImageList();
        // 3.改变显示
        exchangeViewShow();
    }

    private void setLoadState(boolean loadOk) {
        if (loadOk) {//防止闪屏  加了500ms延时
            ObjectAnimator alphaOP = ObjectAnimator.ofFloat(mLoadingProgress, "alpha", 1.0f, 0f);
            alphaOP.setDuration(500);
            ObjectAnimator alphaAR = ObjectAnimator.ofFloat(mContentRl, "alpha", 0.0f, 1.0f);
            alphaAR.setDuration(500);
            alphaAR.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mContentRl.setVisibility(View.VISIBLE);
                }
            });

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(alphaOP).before(alphaAR);

            animatorSet.start();

        } else {
            mLoadingProgress.setVisibility(View.VISIBLE);
            mContentRl.setVisibility(View.GONE);
        }
    }

    /**
     * 改变布局显示 需要及时更新，每次点击的地方下手
     */
    private void exchangeViewShow() {
        if (mMode == MODE_SINGLE)
            return;

        // 预览是不是可以点击，显示什么颜色
        if (mResultList.size() > 0) {
            // 至少选择了一张
            mSelectPreview.setEnabled(true);
            mSelectPreview.setOnClickListener(this);

            mSelectFinish.setEnabled(true);
            mSelectFinish.setOnClickListener(this);
        } else {
            // 一张都没选
            mSelectPreview.setEnabled(false);
            mSelectPreview.setOnClickListener(null);

            mSelectFinish.setEnabled(false);
            mSelectFinish.setOnClickListener(null);
        }


        // 中间图片的张数也要显示
        mSelectNumTv.setText(mResultList.size() + "/" + mMaxCount);
    }


    /**
     * ContentProvider获取内存卡中所有的图片
     */
    private void initImageList() {
        // 耗时操作，开线程，AsyncTask,
        // int id 查询全部
        getLoaderManager().initLoader(LOADER_TYPE, null, mLoaderCallback);
    }


    /**
     * 加载图片的CallBack
     */
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            // 查询数据库一样 语句
            CursorLoader cursorLoader = new CursorLoader(SelectImageActivity.this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                    IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR "
                            + IMAGE_PROJECTION[3] + "=? ",
                    new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");//DESC
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            // 解析，封装到集合  只保存String路径
            if (data != null && data.getCount() > 0) {
                ArrayList<String> images = new ArrayList<>();

                // 如果需要显示拍照，就在第一个位置上加一个空String
                if (mShowCamera && mMode == MODE_SINGLE) {//单选才能加相机
                    images.add("");
                }

                //把游标移动到第一个位置
                if (data.moveToFirst()) {
                    // 不断的遍历循环
                    while (data.moveToNext()) {
                        // 只保存路径
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        images.add(path);
                    }
                }

                // 显示列表数据
                showImageList(images);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
//            showImageList(new ArrayList<String>());
        }
    };


    /**
     * 展示获取到的图片显示到列表
     *
     * @param images
     */
    private void showImageList(ArrayList<String> images) {
        SelectImageListAdapter listAdapter = new SelectImageListAdapter(this, images, mResultList, mMaxCount, mMode);
        listAdapter.setOnSelectImageListener(this);
        mImageListRv.setLayoutManager(new GridLayoutManager(this, 4));
        mImageListRv.setAdapter(listAdapter);

        setLoadState(true);
    }

    @Override
    public void onClick(View v) {
        //图片预览
//        mResultList 当前图片集合
        int i = v.getId();
        if (i == R.id.select_finish) {//确定
            selectFinish();
        } else if (i == R.id.select_preview) {//预览

        }
    }

    private void selectFinish() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT, mResultList);
        setResult(IMAGE_RESULT, intent);
        finish();
    }


    //选择回调
    @Override
    public void select(boolean isCamera) {

        if (isCamera) {//点击相机
            //进入相机

        } else {//不是相机
            if (mMode == MODE_MULTI) {
                exchangeViewShow();
            } else {//单选后  进入预览

            }
        }
    }

    //拍照返回  或者预览返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //相机：
        //1.图片加载到集合
        //2.返回数据
        //3.通知系统本地图库有改变，下次进来还可以找到这张图片

    }
}
