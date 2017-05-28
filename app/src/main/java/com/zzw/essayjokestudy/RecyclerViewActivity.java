package com.zzw.essayjokestudy;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zzw.baselibray.base.BaseActivity;
import com.zzw.framelibray.banner.BannerAdapter;
import com.zzw.framelibray.banner.BannerView;
import com.zzw.framelibray.recyclerview.adapter.OnItemClickListener;
import com.zzw.framelibray.recyclerview.view.DefaultLoadCreator;
import com.zzw.framelibray.recyclerview.view.DefaultRefreshCreator;
import com.zzw.framelibray.recyclerview.view.LoadRefreshRecyclerView;
import com.zzw.framelibray.recyclerview.view.LoadViewCreator;
import com.zzw.framelibray.recyclerview.view.RefreshRecyclerView;
import com.zzw.framelibray.recyclerview.view.WrapRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzw on 2017/5/25.
 * Version:
 * Des:
 */

public class RecyclerViewActivity extends BaseActivity {

    private LoadRefreshRecyclerView mRecyclerView;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_recy;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        mRecyclerView = (LoadRefreshRecyclerView) findViewById(R.id.recy);
    }

    List<String> mDatas;
    int i = 0;

    @Override
    protected void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatas = new ArrayList<>();
        for (int i = 1; i < 20; i++) {
            mDatas.add("" + i);
        }
        HomeAdapter adapter = new HomeAdapter(this, mDatas);
        mRecyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(view.getContext(), mDatas.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(adapter);

        LoadViewCreator loadViewCreator = new DefaultLoadCreator();
        
        //手动点击 没有更多了  然后状态重置并且自动加载  触发加载回调
        loadViewCreator.setOnNoLoadMoreClickListener(new LoadViewCreator.OnNoLoadMoreClickListener() {
            @Override
            public void OnNoLoadMoreClick() {
                mRecyclerView.setLoading();
            }
        });
        mRecyclerView.setLoadViewCreator(loadViewCreator);
        mRecyclerView.setRefreshViewCreator(new DefaultRefreshCreator());

        mRecyclerView.setOnLoadMoreListener(new LoadRefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoad() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("zzz", "i    ->" + i);
                        if (i < 2) {
                            mRecyclerView.onStopLoad();
                        } else if (i < 4) {
                            mRecyclerView.setNoLoadMore();
                        }
                        i++;
                        Log.e("zzz", "i++  ->" + i);
                        if (i == 4) {
                            i = 0;
                        }
                    }
                }, 500);
            }
        });

        mRecyclerView.setOnRefreshListener(new RefreshRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.onStopRefresh();
                    }
                }, 2000);
            }
        });

        //放在mRecyclerView.setRefreshViewCreator(new DefaultRefreshCreator());的后面  有一个先后顺序
        mRecyclerView.addHeaderView(getHeaderView());

//        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
    }

    private View getHeaderView() {
        BannerView bannerView = new BannerView(this);
        bannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position, View convertView) {
                ImageView imageView = null;
                if (convertView == null) {
                    imageView = new ImageView(RecyclerViewActivity.this);
                } else {
                    imageView = (ImageView) convertView;
                }

                imageView.setLayoutParams(new ViewGroup.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setImageResource(R.mipmap.img_src);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }

            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public String getBannerDesc(int position) {
                return "这个是标题" + position;

            }
        });
        bannerView.startRoll();
        return bannerView;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_action_gridview:
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                break;
            case R.id.id_action_listview:
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
        }
        return true;
    }


    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        private List<String> mDatas;
        private LayoutInflater mInflater;


        public HomeAdapter(Context context, List<String> datas) {
            mInflater = LayoutInflater.from(context);
            mDatas = datas;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(mInflater.inflate(
                    R.layout.item_home, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.tv.setText(mDatas.get(position));
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.id_num);
            }
        }
    }

}
