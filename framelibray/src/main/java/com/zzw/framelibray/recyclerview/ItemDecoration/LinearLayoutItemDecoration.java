package com.zzw.framelibray.recyclerview.ItemDecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zzw on 2017/5/25.
 * Version:
 * Des:
 */

public class LinearLayoutItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDriver;


    public LinearLayoutItemDecoration(Context context, int drawableResId) {
        mDriver = ContextCompat.getDrawable(context, drawableResId);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int pos = parent.getChildAdapterPosition(view);//当前item的位置  view当前item
        //留出分割线的位置
        //如果不是第0个位置  就在每个item上面留出mDriver的高度 然后在onDraw里面画出mDriver
        if (pos != 0) {
            outRect.top = mDriver.getIntrinsicHeight();
        }
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //得到item数量
        int childCount = parent.getChildCount();


        Rect rect = new Rect();
        rect.left = parent.getPaddingLeft();
        rect.right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 1; i < childCount; i++) {//从下标为1的位置开始画
            rect.bottom = parent.getChildAt(i).getTop();
            rect.top = rect.bottom - mDriver.getIntrinsicHeight();
            mDriver.setBounds(rect);
            mDriver.draw(c);
        }
    }
}
