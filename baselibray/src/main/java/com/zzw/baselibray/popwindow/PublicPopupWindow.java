package com.zzw.baselibray.popwindow;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.zzw.baselibray.R;

//                final PublicPopupWindow popupWindow = new PublicPopupWindow
//                        .Builder(v.getContext())
//                        .setWidthAndHeight(300, 300)
//                        .setContentView(R.layout.dialog_)
//                        .addDefaultAnimation()
////                        .setAnimations()
////                        .setBackgroundDrawable()
////                        .setFocusable(true)
////                        .setOutsideTouchable(true)
//                        .setText(R.id.tv, "测试")
//                        .create();
//
//                popupWindow.setOnclickListener(R.id.tv, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        TextView textView = popupWindow.getView(R.id.tv);
//                        Toast.makeText(v.getContext(), textView.getText(), Toast.LENGTH_SHORT).show();
//                    }
//                });
////                popupWindow.showParentViewBottom(root);
//                popupWindow.showViewBottom(bt);



//    /**
//     * 让popupwindow以外区域阴影显示
//     *
//     * @param popupWindow
//     */
//    private void popOutShadow(final Activity activity, PopupWindow popupWindow) {
//        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//        lp.alpha = 0.7f;//设置阴影透明度
//        activity.getWindow().setAttributes(lp);
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//                lp.alpha = 1f;
//                activity.getWindow().setAttributes(lp);
//            }
//        });
//    }



/**
 * Created by zzw on 2017/6/21.
 * Version:
 * Des:
 */

public class PublicPopupWindow extends PopupWindow {

    private PublicPopupWindowController mAlert;

    public PublicPopupWindow(Context context) {
        super(context);
        mAlert = new PublicPopupWindowController(this);
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    public void setText(int viewId, CharSequence text) {
        mAlert.setText(viewId, text);
    }

    public <T extends View> T getView(int viewId) {
        return mAlert.getView(viewId);
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param listener
     */
    public void setOnclickListener(int viewId, View.OnClickListener listener) {
        mAlert.setOnclickListener(viewId, listener);
    }

    /**
     * 显示在一个parentView的下方
     *
     * @param parent
     */
    public void showParentViewBottom(View parent) {
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    /**
     * @param view
     */
    public void showViewBottom(View view) {
        showViewBottom(view, 0, 0);
    }

    /**
     * @param view
     * @param offsetX 偏移量
     * @param offsetY 偏移量
     */
    public void showViewBottom(View view, int offsetX, int offsetY) {
        int[] loca = new int[2];
        view.getLocationOnScreen(loca);
        int x = loca[0] + offsetX;
        int y = loca[1] + view.getHeight() + offsetY;
        showAtLocation(view, Gravity.NO_GRAVITY, x, y);
    }


    public static class Builder {

        private final PublicPopupWindowController.PopupWindowParams P;

        /**
         * Creates a builder for an alert dialog that uses the default alert
         * dialog theme.
         * <p/>
         * The default alert dialog theme is defined by
         * {@link android.R.attr#alertDialogTheme} within the parent
         * {@code context}'s theme.
         *
         * @param context the parent context
         */
        public Builder(Context context) {
            P = new PublicPopupWindowController.PopupWindowParams(context);
        }


        /**
         * Sets a custom view to be the contents of the alert dialog.
         * <p/>
         * When using a pre-Holo theme, if the supplied view is an instance of
         * a {@link ListView} then the light background will be used.
         * <p/>
         * <strong>Note:</strong> To ensure consistent styling, the custom view
         * should be inflated or constructed using the alert dialog's themed
         * context obtained via {@link #getContext()}.
         *
         * @param view the view to use as the contents of the alert dialog
         * @return this Builder object to allow for chaining of calls to set
         * methods
         */
        public Builder setContentView(View view) {
            P.mView = view;
            P.mViewLayoutResId = 0;
            return this;
        }

        // 设置布局内容的layoutId
        public Builder setContentView(int layoutId) {
            P.mView = null;
            P.mViewLayoutResId = layoutId;
            return this;
        }

        // 设置文本
        public Builder setText(int viewId, CharSequence text) {
            P.mTextArray.put(viewId, text);
            return this;
        }

        // 设置点击事件
        public Builder setOnClickListener(int viewId, View.OnClickListener listener) {
            P.mClickArray.put(viewId, listener);
            return this;
        }

        // 配置一些万能的参数
        public Builder fullWidth() {
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        /**
         * 设置Dialog的宽高
         *
         * @param width
         * @param height
         * @return
         */
        public Builder setWidthAndHeight(int width, int height) {
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }

        /**
         * 设置Dialog的宽高
         *
         * @param drawable
         * @param
         * @return
         */
        public Builder setBackgroundDrawable(Drawable drawable) {
            P.mBackgroundDrawable = drawable;
            return this;
        }

        /**
         * 设置是否获取焦点 true的时候按返回键先关闭popupWindow 并且点击popupWindow外部消失
         *
         * @param focusable
         * @param
         * @return
         */
        public Builder setFocusable(boolean focusable) {
            P.mFocusable = focusable;
            return this;
        }

        /**
         * 设置外部点击生效   需要设置背景才生效  默认设置了背景
         * 如果设置了mOutsideTouchable=true   mFocusable=false  那么点击外部之后popupWindow消失
         * 并且点击事件将会触发
         *
         * @param touchable
         * @return
         */
        public Builder setOutsideTouchable(boolean touchable) {
            P.mOutsideTouchable = touchable;
            return this;
        }

        /**
         * 添加默认动画
         *
         * @return
         */
        public Builder addDefaultAnimation() {
            P.mAnimations = R.style.dialog_scale_anim;
            return this;
        }

        /**
         * 设置动画
         *
         * @param styleAnimation
         * @return
         */
        public Builder setAnimations(int styleAnimation) {
            P.mAnimations = styleAnimation;
            return this;
        }


        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }


        /**
         * Creates an {@link PublicPopupWindow} with the arguments supplied to this
         * builder.
         * <p/>
         * Calling this method does not display the dialog. If no additional
         * processing is needed, {@link # showAtLocation()} may be called instead to both
         * create and display the dialog.
         */
        public PublicPopupWindow create() {
            // Context has already been wrapped with the appropriate theme.
            final PublicPopupWindow popupWindow = new PublicPopupWindow(P.mContext);
            P.apply(popupWindow.mAlert);
            popupWindow.setOnDismissListener(P.mOnDismissListener);
            return popupWindow;
        }


        /**
         * Creates an {@link PublicPopupWindow} with the arguments supplied to this
         * builder and immediately displays the dialog.
         * <p/>
         * Calling this method is functionally identical to:
         * <pre>
         *     AlertDialog dialog = builder.create();
         *     dialog.show();
         * </pre>
         */
        public PublicPopupWindow showAtLocation(View parent, int gravity, int x, int y) {
            final PublicPopupWindow popupWindow = create();
            popupWindow.showAtLocation(parent, gravity, x, y);
            return popupWindow;
        }
    }
}
