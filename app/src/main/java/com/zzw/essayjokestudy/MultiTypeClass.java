package com.zzw.essayjokestudy;

import android.content.Context;

import com.zzw.framelibray.recyclerview.adapter.CommonRecyclerAdapter;
import com.zzw.framelibray.recyclerview.adapter.MultiTypeSupport;
import com.zzw.framelibray.recyclerview.adapter.ViewHolder;

import java.util.List;

/**
 * Created by zzw on 2017/6/5.
 * Version:
 * Des:
 */

public class MultiTypeClass extends CommonRecyclerAdapter<String> {

    public MultiTypeClass(Context context, List<String> data) {
        super(context, data, new MultiType());
    }

    @Override
    public void convert(ViewHolder holder, String item, int position) {
        int type = getItemViewType(position);

        switch (type) {//type是布局R.layout.xxx
            case 1:
                //....
                break;
        }

    }

    static class MultiType implements MultiTypeSupport<String> {

        @Override
        public int getLayoutId(String item, int position) {

            return 0;
        }
    }
}
