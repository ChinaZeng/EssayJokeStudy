package com.zzw.essayjokestudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.zzw.framelibray.banner.BannerAdapter;
import com.zzw.framelibray.banner.BannerView;
import com.zzw.framelibray.banner.BannerViewPager;

public class BannerActivity extends AppCompatActivity {

    private BannerView mBannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        mBannerView = (BannerView) findViewById(R.id.bv);
        mBannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position, View convertView) {
                ImageView imageView = null;
                if (convertView == null) {
                    imageView = new ImageView(BannerActivity.this);
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
        mBannerView.startRoll();
    }


}
