package com.yunbao.main.activity;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meihu.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.yunbao.common.Constants;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.AllMallAdapter;
import com.yunbao.main.adapter.ShopListAdapter;
import com.yunbao.main.adapter.ShopOneClassAdapter;
import com.yunbao.main.bean.ShopBannerBean;
import com.yunbao.main.bean.ShopGoodsTypeBean;
import com.yunbao.main.bean.ShopOneClassBean;
import com.yunbao.main.utils.MyClickInterface;
import com.yunbao.main.views.refreshlayout.RefreshLayout;
import com.yunbao.mall.activity.GoodsDetailActivity;
import com.yunbao.mall.http.MallHttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllMallFragment extends Fragment implements MyClickInterface {

    private String shopType = "hit";//new最新， hot热销，hit首页
    private ArrayList<ShopBannerBean> shopBannerList = new ArrayList<>();//banner
    private ArrayList<ShopBannerBean> shopBannerList2 = new ArrayList<>();//中部图片
    private ArrayList<ShopGoodsTypeBean> shopGoodsTypeBeans = new ArrayList<>();//商品列表
    private RecyclerView recyclerview;
    private RefreshLayout refreshLayout;
    private AllMallAdapter adapter;


    //初始化正则
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_mall, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        recyclerview = view.findViewById(R.id.recyclerView);
        refreshLayout = view.findViewById(R.id.refreshLayout);

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AllMallAdapter(getContext(), getActivity(), shopBannerList, shopBannerList2, shopGoodsTypeBeans, this);
        recyclerview.setAdapter(adapter);

        getBanner();
        getBanner2();
        getRecyData(shopType, null, null);


        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onPullDownToRefresh() {
                refreshLayout.pageNumber = 1;
                adapter.isCreateBanner = false;
                adapter.updUi();
                shopType = "hit";
                getBanner2();
                getRecyData(shopType, null, null);
            }

            @Override
            public void onPullUpToRefresh() {
                refreshLayout.pageNumber++;
                getRecyData(shopType, null, null);
            }
        });

    }

    private void getBanner() {
        MallHttpUtil.getShopBanner(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    shopBannerList.clear();
                    for (int i = 0; i < info.length; i++) {
                        JSONObject obj = JSON.parseObject(info[i]);
                        ShopBannerBean shopBannerBean = new ShopBannerBean();
                        shopBannerBean.setImage(obj.getString("image"));
                        shopBannerBean.setUrl(obj.getString("url"));
                        shopBannerList.add(shopBannerBean);
                    }
                    adapter.isCreateBanner = true;
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void getBanner2() {
        MallHttpUtil.getShopBanner2(new HttpCallback() {

            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    shopBannerList2.clear();
                    for (int i = 0; i < info.length; i++) {
                        JSONObject obj = JSON.parseObject(info[i]);
                        ShopBannerBean shopBannerBean = new ShopBannerBean();
                        shopBannerBean.setImage(obj.getString("image"));
                        shopBannerList2.add(shopBannerBean);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getRecyData(String type, String type_id, String keywords) {

        MallHttpUtil.getShopGoodsType(type, type_id, refreshLayout.pageNumber, keywords, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    if (refreshLayout.pageNumber == 1) {
                        shopGoodsTypeBeans.clear();
                    }
                    for (int i = 0; i < info.length; i++) {
                        JSONObject object = JSON.parseObject(info[i]);
                        JSONArray list = object.getJSONArray("list");
                        for (int j = 0; j < list.size(); j++) {
                            JSONObject jsonObject = list.getJSONObject(j);
                            ShopGoodsTypeBean shopGoodsTypeBean = new ShopGoodsTypeBean();
                            shopGoodsTypeBean.setId(jsonObject.getString("id"));
                            shopGoodsTypeBean.setName(jsonObject.getString("name"));
                            shopGoodsTypeBean.setThumb(jsonObject.getString("thumb"));
                            shopGoodsTypeBean.setPrice(jsonObject.getString("price"));
                            shopGoodsTypeBeans.add(shopGoodsTypeBean);
                        }
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
        switch (type) {
            case 1:
                shopType = "new";
                getRecyData(shopType, null, null);
                break;
            case 2:
                shopType = "hot";
                getRecyData(shopType, null, null);
                break;
        }
    }
}
