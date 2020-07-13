package com.yunbao.main.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yunbao.common.views.AbsMainViewHolder;
import com.yunbao.main.R;
import com.yunbao.main.activity.ShangQuanActivity;

/**
 * 商圈
 */
public class MainShopViewHolder extends AbsMainViewHolder {

    private ImageView iv_ym;
    private ImageView iv_tb;
    private ImageView iv_jd;
    private ImageView iv_pdd;
    private ImageView iv_tm;
    private ImageView iv_mgj;


    public MainShopViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_shop;
    }

    @Override
    public void init() {

        iv_ym = findViewById(R.id.iv_ym);
        iv_tb = findViewById(R.id.iv_tb);
        iv_jd = findViewById(R.id.iv_jd);
        iv_pdd = findViewById(R.id.iv_pdd);
        iv_tm = findViewById(R.id.iv_tm);
        iv_mgj = findViewById(R.id.iv_mgj);

        iv_ym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShangQuanActivity.class);
                mContext.startActivity(intent);
            }
        });
        iv_tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.taobao.com/");//要跳转的网址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
        iv_jd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.jd.com/");//要跳转的网址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
        iv_pdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.pinduoduo.com/");//要跳转的网址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
        iv_tm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.tmall.com/");//要跳转的网址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
        iv_mgj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.mogu.com/");//要跳转的网址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
    }
}
