package com.zzw.essayjokestudy;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzw.baselibray.base.BaseActivity;
import com.zzw.framelibray.recyclerview.ItemDecoration.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzw on 2017/5/25.
 * Version:
 * Des:
 */

public class RecyclerViewActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_recy;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recy);
    }

    @Override
    protected void initData() {
        List<String> mDatas = new ArrayList<String>();
        for (int i = 1; i < 101; i++) {
            mDatas.add("" + i);
        }

        HomeAdapter adapter = new HomeAdapter(this, mDatas);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));

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
