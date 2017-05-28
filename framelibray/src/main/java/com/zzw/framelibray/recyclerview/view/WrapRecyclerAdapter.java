package com.zzw.framelibray.recyclerview.view;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.zzw.framelibray.recyclerview.adapter.OnItemClickListener;
import com.zzw.framelibray.recyclerview.adapter.OnLongClickListener;


/**
 * Created by zzw on 2017/05/28.
 * Description: 可以添加头部和底部的Adapter
 */
public class WrapRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "WrapRecyclerAdapter";

    /**
     * SparseArrays map integers to Objects.  Unlike a normal array of Objects,
     * there can be gaps in the indices.  It is intended to be more memory efficient
     * than using a HashMap to map Integers to Objects, both because it avoids
     * auto-boxing keys and its data structure doesn't rely on an extra entry object
     * for each mapping.
     *
     * SparseArray是一个<int , Object>的HashMap  比HashMap更高效
     */
    private SparseArray<View> mHeaderViews;
    private SparseArray<View> mFooterViews;

    // 基本的头部类型开始位置  用于viewType
    private static int BASE_ITEM_TYPE_HEADER = 10000000;
    // 基本的底部类型开始位置  用于viewType
    private static int BASE_ITEM_TYPE_FOOTER = 20000000;

    /**
     * 数据列表的Adapter
     */
    private RecyclerView.Adapter mAdapter;

    public WrapRecyclerAdapter(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // viewType 可能就是 SparseArray 的key
        if (isHeaderViewType(viewType)) {
            View headerView = mHeaderViews.get(viewType);
            return createHeaderFooterViewHolder(headerView);
        }

        if (isFooterViewType(viewType)) {
            View footerView = mFooterViews.get(viewType);
            return createHeaderFooterViewHolder(footerView);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    /**
     * 是不是底部类型
     */
    private boolean isFooterViewType(int viewType) {
        int position = mFooterViews.indexOfKey(viewType);
        return position >= 0;
    }

    /**
     * 创建头部或者底部的ViewHolder
     */
    private RecyclerView.ViewHolder createHeaderFooterViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {

        };
    }

    /**
     * 是不是头部类型
     */
    private boolean isHeaderViewType(int viewType) {
        int position = mHeaderViews.indexOfKey(viewType);
        return position >= 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return;
        }

        // 计算一下位置
        final int adapterPosition = position - mHeaderViews.size();
        mAdapter.onBindViewHolder(holder, adapterPosition);

        // 设置点击和长按事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v, adapterPosition);
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mLongClickListener.onLongClick(v, adapterPosition);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            // 直接返回position位置的key
            return mHeaderViews.keyAt(position);
        }
        if (isFooterPosition(position)) {
            // 直接返回position位置的key
            position = position - mHeaderViews.size() - mAdapter.getItemCount();
            return mFooterViews.keyAt(position);
        }
        // 返回列表Adapter的getItemViewType
        position = position - mHeaderViews.size();
        return mAdapter.getItemViewType(position);
    }

    /**
     * 是不是底部位置
     */
    private boolean isFooterPosition(int position) {
        return position >= (mHeaderViews.size() + mAdapter.getItemCount());
    }

    /**
     * 是不是头部位置
     */
    private boolean isHeaderPosition(int position) {
        return position < mHeaderViews.size();
    }

    @Override
    public int getItemCount() {
        // 条数三者相加 = 底部条数 + 头部条数 + Adapter的条数
        return mAdapter.getItemCount() + mHeaderViews.size() + mFooterViews.size();
    }

    /**
     * 获取列表的Adapter
     */
    private RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    // 添加头部
    public void addHeaderView(View view) {
        int position = mHeaderViews.indexOfValue(view);
        if (position < 0) {
            mHeaderViews.put(BASE_ITEM_TYPE_HEADER++, view);
        }
        notifyDataSetChanged();
    }

    // 添加底部
    public void addFooterView(View view) {
        int position = mFooterViews.indexOfValue(view);
        if (position < 0) {
            mFooterViews.put(BASE_ITEM_TYPE_FOOTER++, view);
        }
        notifyDataSetChanged();
    }

    // 移除头部
    public void removeHeaderView(View view) {
        int index = mHeaderViews.indexOfValue(view);
        if (index < 0) return;
        mHeaderViews.removeAt(index);
        notifyDataSetChanged();
    }

    // 移除底部
    public void removeFooterView(View view) {
        int index = mFooterViews.indexOfValue(view);
        if (index < 0) return;
        mFooterViews.removeAt(index);
        notifyDataSetChanged();
    }

    /**
     * 解决GridLayoutManager添加头部和底部不占用一行的问题
     *
     * @param recycler
     * @version 1.0
     */
    public void adjustSpanSize(RecyclerView recycler) {
        if (recycler.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) recycler.getLayoutManager();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter =
                            isHeaderPosition(position) || isFooterPosition(position);
                    return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    /***************
     * 给条目设置点击和长按事件
     *********************/
    public OnItemClickListener mItemClickListener;
    public OnLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;
    }
}
