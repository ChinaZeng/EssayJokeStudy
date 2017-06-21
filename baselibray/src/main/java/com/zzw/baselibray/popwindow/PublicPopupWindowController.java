package com.zzw.baselibray.popwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;


/**
 * Created by zzw on 2017/5/16.
 * Version:
 * Des:拼装参数
 */

//不被外部访问
class PublicPopupWindowController {


    private PopupWindow mPopupWindow;

    private PopupWindowViewHelper mViewHelper;

    public PublicPopupWindowController(PopupWindow popupWindow) {
        this.mPopupWindow = popupWindow;
    }

    public void setViewHelper(PopupWindowViewHelper viewHelper) {
        this.mViewHelper = viewHelper;
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    public void setText(int viewId, CharSequence text) {
        mViewHelper.setText(viewId, text);
    }

    public <T extends View> T getView(int viewId) {
        return mViewHelper.getView(viewId);
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param listener
     */
    public void setOnclickListener(int viewId, View.OnClickListener listener) {
        mViewHelper.setOnclickListener(viewId, listener);
    }

    /**
     * 获取PopupWindow
     *
     * @return
     */
    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }


    public static class PopupWindowParams {
        public Context mContext;

        // dialog Dismiss监听
        public PopupWindow.OnDismissListener mOnDismissListener;
        // 布局View
        public View mView;
        // 布局layout id
        public int mViewLayoutResId;
        // 存放字体的修改-->使用SparseArray的原因: <integer,object>结构比hashMap高效
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();
        // 存放点击事件
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();
        // 宽度
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 动画
        public int mAnimations = 0;
        // 高度
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        //背景
        public Drawable mBackgroundDrawable = new ColorDrawable(0x00000000);//默认弄一个
        //是否获取焦点 默认获取 true的时候按返回键先关闭popupWindow
        public boolean mFocusable = true;
        //是否外部点击消失 默认为true   这个需要设置背景才会生效 默认设置了
        public boolean mOutsideTouchable = true;

        public PopupWindowParams(Context context) {
            this.mContext = context;
        }

        /**
         * 绑定和设置参数
         *
         * @param mAlert
         */
        public void apply(PublicPopupWindowController mAlert) {
            // 完善这个地方 设置参数

            // 1. 设置PopupWindow布局  PopupWindowViewHelper
            PopupWindowViewHelper viewHelper = null;
            if (mViewLayoutResId != 0) {
                viewHelper = new PopupWindowViewHelper(mContext, mViewLayoutResId);
            }

            if (mView != null) {
                viewHelper = new PopupWindowViewHelper();
                viewHelper.setContentView(mView);
            }

            if (viewHelper == null) {
                throw new IllegalArgumentException("请设置布局setContentView()");
            }

            // 给Dialog 设置布局
            mAlert.getPopupWindow().setContentView(viewHelper.getContentView());
            mAlert.getPopupWindow().setFocusable(mFocusable);

            // 设置 Controller的辅助类
            mAlert.setViewHelper(viewHelper);

            // 2.设置文本
            int textArraySize = mTextArray.size();
            for (int i = 0; i < textArraySize; i++) {
                mAlert.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }

            // 3.设置点击
            int clickArraySize = mClickArray.size();
            for (int i = 0; i < clickArraySize; i++) {
                mAlert.setOnclickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }

            //设置背景
            mAlert.getPopupWindow().setBackgroundDrawable(mBackgroundDrawable);
            mAlert.getPopupWindow().setOutsideTouchable(mOutsideTouchable);

            // 设置动画
            if (mAnimations != 0) {
                mAlert.getPopupWindow().setAnimationStyle(mAnimations);
            }

            // 设置宽高
            mAlert.getPopupWindow().setWidth(mWidth);
            mAlert.getPopupWindow().setHeight(mHeight);
        }
    }
}
