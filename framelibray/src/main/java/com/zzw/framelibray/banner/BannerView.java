package com.zzw.framelibray.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzw.framelibray.R;

/**
 * Created by zzw on 2017/5/24.
 * Version:
 * Des:
 */

public class BannerView extends RelativeLayout {


    private Context mContext;

    //轮播ViewPager
    private BannerViewPager mBannerVP;
    //banner  adapter
    private BannerAdapter mAdapter;
    //描述TextView
    private TextView mBannerDescTv;
    //点的容器
    private LinearLayout mContainLl;
    //底部容器
    private View mBottomRl;


    public static final int GRAVITY_CENTER = 0;
    public static final int GRAVITY_LEFT = 1;
    public static final int GRAVITY_RIGHT = 2;

    /**
     * 当前位置   对应BannerAdapter里面的position  也就是数据源的pos
     */
    private int mCurrentPosition = 0;


    /**
     * 选中的指示器Drawable  默认红色
     */
    private Drawable mIndicatorFocusDrawable;
    /**
     * 默认的指示器Drawable   默认白色
     */
    private Drawable mIndicatorNormalDrawable;

    /**
     * 指示点的位置  0中间  1左边  2右边   默认在右边
     */
    private int mDotGravity;

    /**
     * 是否显示描述 默认显示
     */
    private boolean mIsShowDesc;

    /**
     * 点的大小  默认8dp
     */
    private int mDotSize;

    /**
     * 点的间距  默认8dp
     */
    private int mDotDistance;

    /**
     * 底部颜色  默认透明
     */
    private int mBottomColor;

    /**
     * 宽高比例
     */
    private float mWidthProportion, mHeightProportion;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        inflate(context, R.layout.ui_banner_layout, this);
        initView();
        initAttr(attrs);
    }

    private void initView() {
        mBannerVP = (BannerViewPager) findViewById(R.id.banner_vp);
        mBannerDescTv = (TextView) findViewById(R.id.banner_desc_tv);
        mContainLl = (LinearLayout) findViewById(R.id.dot_container_ll);
        mBottomRl = findViewById(R.id.bottom_rl);
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.BannerView);
        mDotGravity = typedArray.getInt(R.styleable.BannerView_dotGravity, GRAVITY_RIGHT);//默认在右边
        mIndicatorFocusDrawable = typedArray.getDrawable(R.styleable.BannerView_dotIndicatorFocus);
        if (mIndicatorFocusDrawable == null)
            mIndicatorFocusDrawable = new ColorDrawable(Color.RED);
        mIndicatorNormalDrawable = typedArray.getDrawable(R.styleable.BannerView_dotIndicatorNormal);
        if (mIndicatorNormalDrawable == null)
            mIndicatorNormalDrawable = new ColorDrawable(Color.WHITE);
        mDotSize = typedArray.getDimensionPixelSize(R.styleable.BannerView_dotSize, dp2px(8));
        mDotDistance = typedArray.getDimensionPixelSize(R.styleable.BannerView_dotDistance, dp2px(8));
        mBottomColor = typedArray.getColor(R.styleable.BannerView_bottomColor, Color.TRANSPARENT);
        mIsShowDesc = typedArray.getBoolean(R.styleable.BannerView_showDesc, true);
        mWidthProportion = typedArray.getFloat(R.styleable.BannerView_widthProportion, 1.0f);
        mHeightProportion = typedArray.getFloat(R.styleable.BannerView_heightProportion, 1.0f);
        typedArray.recycle();
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        //动态指定宽高  计算高度
//        if (mWidthProportion == 0 || mHeightProportion == 0)
//            return;
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = (int) ((width * mHeightProportion) / mWidthProportion);
//        setMeasuredDimension(width, height);
//    }

    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(BannerAdapter adapter) {
        this.mAdapter = adapter;
        mBannerVP.setAdapter(adapter);
        initIndicate();//初始化点指示器和其他指示器
        mBannerVP.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentDotIndicator(position);
                setBannerDesc(position);
            }
        });

        if (mHeightProportion == 0 || mWidthProportion == 0) {
            return;
        }

        post(new Runnable() {
            @Override
            public void run() {
                // 动态指定宽高  计算高度
                int width = getMeasuredWidth();
                // 计算高度
                int height = (int) (width * mHeightProportion / mWidthProportion);
                // 指定宽高
                getLayoutParams().height = height;
                mBannerVP.getLayoutParams().height = height;
                //不添加 这句代码的话Params的高要下一个切换页面的时候才会变   时间太长
                mBannerVP.setCurrentItem(mCurrentPosition + 1);
            }
        });


    }


    public void setOnBannerItemClickListener(BannerItemClickListener listener) {
        mBannerVP.setOnBannerItemClickListener(listener);
    }


    /**
     * 初始化点指示器和其他指示器
     */

    private void initIndicate() {
        mContainLl.setGravity(getViewGravity(mDotGravity));//设置点的位置
        mBottomRl.setBackgroundColor(mBottomColor);//设置底部背景颜色
        mBannerDescTv.setVisibility(mIsShowDesc ? VISIBLE : GONE);
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            DotIndicatorView indicatorView = new DotIndicatorView(mContext);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(mDotSize, mDotSize);//设置大小
            params.leftMargin = mDotDistance;//设置左右间距
            indicatorView.setLayoutParams(params);

            if (i == mCurrentPosition) {//默认选中位置
                setBannerDesc(mCurrentPosition);
                indicatorView.setDrawable(mIndicatorFocusDrawable);
            } else {
                indicatorView.setDrawable(mIndicatorNormalDrawable);
            }
            mContainLl.addView(indicatorView);
        }

    }


    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension
                (TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    /**
     * 开始滚动
     */
    public void startRoll() {
        mBannerVP.startRoll();
    }


    /**
     * 设置banner广告描述
     *
     * @param position
     */
    private void setBannerDesc(int position) {
        if (!mIsShowDesc)
            return;
        String desc = mAdapter.getBannerDesc(position % mAdapter.getCount());
        mBannerDescTv.setText(desc);
    }

    /**
     * 设置指示灯
     *
     * @param position
     */
    private void setCurrentDotIndicator(int position) {
        //以前的设置为normal
        DotIndicatorView oldIndicatorView = (DotIndicatorView) mContainLl.getChildAt(mCurrentPosition);
        oldIndicatorView.setDrawable(mIndicatorNormalDrawable);

        //现在的设置为Focus
        mCurrentPosition = position % mAdapter.getCount();
        DotIndicatorView nowIndicatorView = (DotIndicatorView) mContainLl.getChildAt(mCurrentPosition);
        nowIndicatorView.setDrawable(mIndicatorFocusDrawable);
    }


    /**
     * 通过自己的Gravity得到View的对应位置
     *
     * @return
     */
    public int getViewGravity(int g) {
        int gravity = Gravity.RIGHT;
        switch (g) {
            case GRAVITY_CENTER:
                gravity = Gravity.CENTER;
                break;

            case GRAVITY_LEFT:
                gravity = Gravity.LEFT;
                break;

            case GRAVITY_RIGHT:
                gravity = Gravity.RIGHT;
                break;
        }

        return gravity;
    }

    /**
     * 隐藏页面指示器
     */
    public void hidePageIndicator() {
        mContainLl.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示页面指示器
     */
    public void showPageIndicator() {
        mContainLl.setVisibility(View.VISIBLE);
    }
}
