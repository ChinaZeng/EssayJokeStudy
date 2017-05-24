package com.zzw.framelibray.banner;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzw on 2017/5/24.
 * Version:
 * Des:
 */

public class BannerViewPager extends ViewPager {

    private BannerAdapter mBannerAdapter;

    //item点击事件
    private BannerItemClickListener mListener;

    //自定义的改变ViewPager页面切换的动画持续时间
    private BannerScroller mBannerScroller;

    private int mCutDownTime = 3500;//切换间隔时间
    private final int SCROLL_MSG = 0x001;

    //复用的界面  设置集合是为了防止滑动过快 复用的View还存在parent
    private List<View> mConvertViews;
    // 是否可以滚动
    private boolean mScrollAble = true;


    private Handler mHandler;
    private Activity mActivity;

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (Activity) context;
        changeScroller(context);
        mConvertViews = new ArrayList<>();
        initHandler();
    }

    /**
     * 改变原始的ViewPager的页面切换持续时间
     */
    private void changeScroller(Context context) {
        try {
            //smoothScrollTo(int x, int y, int velocity) --->   mScroller.startScroll(sx, sy, dx, dy, duration);
            Field field = ViewPager.class.getDeclaredField("mScroller");
            mBannerScroller = new BannerScroller(context);
            field.setAccessible(true);
            //设置自己的Scroller
            field.set(this, mBannerScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置页面切换间隔时间
     *
     * @param cutDownTime
     */
    public void setCutDownTime(int cutDownTime) {
        this.mCutDownTime = cutDownTime;
    }

    /**
     * 设置页面切换动画持续时间
     *
     * @param scrollerDuration 切换动画持续时间
     */
    public void setScrollerDuration(int scrollerDuration) {
        mBannerScroller.setScrollerDuration(scrollerDuration);
    }


    /**
     * 设置自定义的BannerAdapter
     *
     * @param adapter
     */
    public void setAdapter(BannerAdapter adapter) {
        this.mBannerAdapter = adapter;
        //设置父类ViewPager的Adapter
        setAdapter(new BannerPagerAdapter());
    }

    /**
     * 开始轮播滚动
     */
    public void startRoll() {
        // adapter不能是空
        if (mBannerAdapter == null) {
            return;
        }

        // 判断是不是只有一条数据
        mScrollAble = mBannerAdapter.getCount() != 1;
        if (mScrollAble && mHandler != null) {
            // 清除消息
            mHandler.removeMessages(SCROLL_MSG);
            // 消息  延迟时间  3500
            mHandler.sendEmptyMessageDelayed(SCROLL_MSG, mCutDownTime);
        }
    }


    private View getConvertView() {
        for (int i = 0; i < mConvertViews.size(); i++) {
            if (mConvertViews.get(i).getParent() == null) {
                return mConvertViews.get(i);
            }
        }
        return null;
    }

    private class BannerPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            //实现无限轮播 2的31次方
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        //创建ViewPager  Item的方法
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View bannerItemView = mBannerAdapter.getView(position % mBannerAdapter.getCount(), getConvertView());
            container.addView(bannerItemView);
            bannerItemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 回调点击监听
                    if (mListener != null) {
                        mListener.click(position % mBannerAdapter.getCount());
                    }
                }
            });
            return bannerItemView;
//            return super.instantiateItem(container, position);
        }


        //销毁ViewPager  Item的方法
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
            mConvertViews.add((View) object);
        }
    }


    public void setOnBannerItemClickListener(BannerItemClickListener listener) {
        this.mListener = listener;
    }

    private void initHandler() {
        if (mHandler == null)
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 每隔*ms后切换到下一页
                    setCurrentItem(getCurrentItem() + 1);
                    // 不断循环执行
                    startRoll();
                }
            };
    }


    @Override
    protected void onAttachedToWindow() {
        if (mBannerAdapter != null) {
            initHandler();
            startRoll();
            // 管理Activity的生命周期
            mActivity.getApplication().registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        }
        super.onAttachedToWindow();
    }

    //销毁Handler停止发送    Activity调用onDestroy()方法的时候触发防止内存泄漏
    @Override
    protected void onDetachedFromWindow() {
        if (mHandler != null) {
            // 销毁Handler的生命周期
            mHandler.removeMessages(SCROLL_MSG);
            // 解除绑定
            mActivity.getApplication().unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
            mHandler = null;
        }
        super.onDetachedFromWindow();
    }


    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new SimpleActivityLifecycleCallbacks() {
        @Override
        public void onActivityResumed(Activity activity) {
            super.onActivityResumed(activity);
            // 是不是监听的当前Activity的生命周期
            if (activity == mActivity) {
                // 开启轮播
                startRoll();
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            super.onActivityPaused(activity);
            if (activity == mActivity) {
                // 停止轮播
                mHandler.removeMessages(SCROLL_MSG);
            }
        }
    };

}
