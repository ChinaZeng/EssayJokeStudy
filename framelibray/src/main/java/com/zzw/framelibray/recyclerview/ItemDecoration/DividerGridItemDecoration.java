package com.zzw.framelibray.recyclerview.ItemDecoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zzw on 2017/5/25.
 * Version:
 * Des:GridLayoutManager使用
 */

public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int[] attrs = new int[]{
            android.R.attr.listDivider
    };

    public DividerGridItemDecoration(Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        mDivider = typedArray.getDrawable(0);
        typedArray.recycle();
    }

    public DividerGridItemDecoration(Context context, int drawableResId) {
        mDivider = ContextCompat.getDrawable(context, drawableResId);
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVertical(c, parent);
        drawHorizontal(c, parent);
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        // 绘制水平间隔线
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            int right = child.getRight() + params.rightMargin + mDivider.getIntrinsicWidth();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        //绘制垂直间隔线(垂直的矩形)
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + mDivider.getIntrinsicWidth();
            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // 四个方向的偏移值
        int right = mDivider.getIntrinsicWidth();
        int bottom = mDivider.getIntrinsicHeight();

        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();

        if (isLastColumn(position, parent)) {// 是否是最后一列
            right = 0;
        }
        if (isLastRow(position, parent)) {// 是否是最后一行
            bottom = 0;
        }
        outRect.set(0, 0, right, bottom);
    }

    /**
     * 是否是最后一列
     */
    public boolean isLastColumn(int itemPosition, RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        if ((itemPosition + 1) % spanCount == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否是最后一行
     */
    public boolean isLastRow(int itemPosition, RecyclerView parent) {

        int spanCount = getSpanCount(parent);

        int childCount = parent.getAdapter().getItemCount();

        int rowNumber = childCount % spanCount == 0 ? childCount / spanCount : (childCount / spanCount) + 1;

        if (itemPosition > ((rowNumber - 1) * spanCount - 1)) {
            return true;
        }

        return false;
    }

    /**
     * 获取一行有多少列
     */
    public int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager) {
            // 获取一行的spanCount
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            int spanCount = gridLayoutManager.getSpanCount();
            return spanCount;
        }
        return 1;
    }
}
