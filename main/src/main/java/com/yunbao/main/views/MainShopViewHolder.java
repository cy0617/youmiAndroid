package com.yunbao.main.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.yunbao.common.bean.GoodsBeanTwo;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.SpUtil;
import com.yunbao.common.utils.TimeUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.common.views.AbsMainViewHolder;
import com.yunbao.main.R;
import com.yunbao.main.activity.BuyRecordingAct;
import com.yunbao.main.activity.GoodsDetailsActivity;
import com.yunbao.main.adapter.GoodsRingAdapter;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.main.utils.GridItemDecoration;
import com.yunbao.main.utils.MyClickInterface;
import com.yunbao.main.utils.StringUtil;
import com.yunbao.main.views.refreshlayout.RefreshLayout;
import com.yunbao.mall.http.MallHttpUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainShopViewHolder extends AbsMainViewHolder implements MyClickInterface {

    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private GoodsRingAdapter adapter;
    private List<GoodsBeanTwo> list;
    private ImageView iv_title_right;


    public MainShopViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_shop;
    }

    @Override
    public void init() {
        list = new ArrayList<>();
        iv_title_right = findViewById(R.id.iv_title_right);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GoodsRingAdapter(mContext, list, this);
        GridItemDecoration gridItemDecoration = new GridItemDecoration(10, 2, mContext);
        recyclerView.addItemDecoration(gridItemDecoration);//10表示10dp
        recyclerView.setAdapter(adapter);
        queryGoodsList();


        iv_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, BuyRecordingAct.class));
            }
        });

        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onPullDownToRefresh() {
                refreshLayout.pageNumber = 1;
                queryGoodsList();
            }

            @Override
            public void onPullUpToRefresh() {
                refreshLayout.pageNumber++;
                queryGoodsList();
            }
        });


    }


    public void queryGoodsList(){
        MainHttpUtil.getGoodsList(refreshLayout.pageNumber,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    if (refreshLayout.pageNumber == 1 && info.length > 0) {
                        list.clear();
                    }
                    for(int i = 0;i<info.length;i++){
                        JSONObject obj = JSON.parseObject(info[i]);
                        GoodsBeanTwo goodsBeanTwo = new GoodsBeanTwo();
                        goodsBeanTwo.setDesc(obj.getString("desc"));
                        goodsBeanTwo.setTitle(obj.getString("title"));
                        goodsBeanTwo.setId(obj.getLong("id"));
                        goodsBeanTwo.setImg(obj.getString("img"));
                        goodsBeanTwo.setMoney(StringUtil.subZeroAndDot(new BigDecimal(obj.getString("money"))
                                .setScale(2, BigDecimal.ROUND_DOWN).toPlainString()));
                        list.add(goodsBeanTwo);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.show(msg);
                }
                refreshLayout.onLoad(info.length);
            }
            @Override
            public void onError() {
                refreshLayout.onLoad(-1);
            }
        });
    }

    @Override
    public void myClick(int position, int type) {
        if(type == 1){
            Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
            intent.putExtra("goodsId",String.valueOf(list.get(position).getId()));
            mContext.startActivity(intent);
        }
    }
}
