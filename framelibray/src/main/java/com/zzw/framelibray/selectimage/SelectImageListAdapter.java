package com.zzw.framelibray.selectimage;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.zzw.framelibray.recyclerview.adapter.GlideHolderImageLoader;
import com.zzw.framelibray.R;
import com.zzw.framelibray.recyclerview.adapter.CommonRecyclerAdapter;
import com.zzw.framelibray.recyclerview.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzw on 2017/5/10.
 * Version 1.0
 * Description:
 */
public class SelectImageListAdapter extends CommonRecyclerAdapter<String> {
    // 选择图片的集合
    private ArrayList<String> mResultImageList;
    private int mMaxCount;
    private int mModel;

    public SelectImageListAdapter(Context context, List<String> data, ArrayList<String> imageList, int maxCount, int model) {
        super(context, data, R.layout.media_chooser_item);
        this.mResultImageList = imageList;
        this.mMaxCount = maxCount;
        this.mModel = model;
    }

    @Override
    public void convert(ViewHolder holder, final String item, final int position) {
        if (TextUtils.isEmpty(item)) {
            // 显示拍照
            holder.setViewVisibility(R.id.camera_ll, View.VISIBLE);
            holder.setViewVisibility(R.id.media_selected_indicator, View.INVISIBLE);
            holder.setViewVisibility(R.id.image, View.INVISIBLE);

            holder.setOnIntemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 调用拍照，6.0以上要处理
                    // http://www.jianshu.com/p/823360bb183f
                    mListener.select(true);
                }
            });
        } else {
            // 显示图片
            holder.setViewVisibility(R.id.camera_ll, View.INVISIBLE);
            holder.setViewVisibility(R.id.image, View.VISIBLE);
            if (mModel == SelectImageActivity.MODE_MULTI) {//多选
                holder.setViewVisibility(R.id.media_selected_indicator, View.VISIBLE);
            } else {//单选
                holder.setViewVisibility(R.id.media_selected_indicator, View.INVISIBLE);
            }

            holder.setImageByUrl(R.id.image, new GlideHolderImageLoader(item));

            // 显示图片利用Glide
//            ImageView imageView = holder.getView(R.id.image);
//            Glide.with(mContext).load(item)
//                    .centerCrop().into(imageView);

            if (mModel == SelectImageActivity.MODE_MULTI) {//多选
                ImageView selectIndicatorIv = holder.getView(R.id.media_selected_indicator);
                if (mResultImageList.contains(item)) {
                    // 点亮选择勾住图片 并且变暗
                    selectIndicatorIv.setSelected(true);
                    holder.setViewVisibility(R.id.mask, View.VISIBLE);
                } else {
                    selectIndicatorIv.setSelected(false);
                    holder.setViewVisibility(R.id.mask, View.GONE);
                }

                // 给条目增加点击事件
                holder.setOnIntemClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 没有就加入集合，有就移除集合
                        if (!mResultImageList.contains(item)) {
                            // 不能大于最大的张数
                            if (mResultImageList.size() >= mMaxCount) {
                                // 自定义Toast
                                Toast.makeText(mContext, "最多只能选取" + mMaxCount + "张图片"
                                        , Toast.LENGTH_SHORT).show();
                                return;
                            }
                            mResultImageList.add(item);
                        } else {
                            mResultImageList.remove(item);
                        }
                        notifyItemChanged(position);
//                    notifyDataSetChanged();

                        // 通知显示布局
                        if (mListener != null) {
                            mListener.select(false);
                        }
                    }
                });
            } else {//单选
                // 给条目增加点击事件
                holder.setOnIntemClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 通知显示布局
                        if (mListener != null) {
                            mListener.select(false);
                        }
                    }
                });
            }


        }
    }


    // 设置选择图片监听
    private SelectImageListener mListener;

    public void setOnSelectImageListener(SelectImageListener listener) {
        this.mListener = listener;
    }
}
