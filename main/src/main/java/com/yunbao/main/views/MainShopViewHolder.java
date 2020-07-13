package com.yunbao.main.views;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meihu.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.views.AbsMainViewHolder;
import com.yunbao.main.R;
import com.yunbao.main.activity.GlideImageLoader;
import com.yunbao.main.adapter.ShopOneClassAdapter;
import com.yunbao.main.bean.MyUbBean;
import com.yunbao.main.bean.ShopBannerBean;
import com.yunbao.main.bean.ShopOneClassBean;
import com.yunbao.main.views.refreshlayout.RefreshLayout;
import com.yunbao.mall.http.MallHttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 商圈
 */
public class MainShopViewHolder extends AbsMainViewHolder {
    private Banner banner;
    private ArrayList<String> shopBannerList;
    private ArrayList<ShopBannerBean> shopBannerList2;
    private List<ShopOneClassBean> shopOneClassList;
    private RecyclerView recyclerview;
    private ArrayList<MyUbBean> list;
    private RefreshLayout refreshLayout;
    private Dialog mDialog;
    private RecyclerView one_recy;
    private ShopOneClassAdapter shopOneClassAdapter;
    private ImageView iv_xinpin;
    private ImageView iv_rexiao;
    private RecyclerView shopListRecy;

    private String type = "";
    private String type_id = "";
    private String keywords = "";

    public MainShopViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_shop;
    }

    @Override
    public void init() {
        iv_xinpin = findViewById(R.id.iv_xinpin);
        iv_rexiao = findViewById(R.id.iv_rexiao);
        shopListRecy = findViewById(R.id.recyclerView);
        shopBannerList = new ArrayList<>();
        shopBannerList2 = new ArrayList<>();
        shopOneClassList = new ArrayList<>();
        banner = findViewById(R.id.banner);
        refreshLayout = findViewById(R.id.refreshLayout);
        getBanner();
        getBanner2();

        list = new ArrayList<>();
        mDialog = DialogUitl.loadingDialog(mContext, "加载中...");
        one_recy = findViewById(R.id.oneclassrecy);
        one_recy.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        shopOneClassAdapter = new ShopOneClassAdapter(mContext, shopOneClassList);
        one_recy.setAdapter(shopOneClassAdapter);
        getOneClassData();


        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onPullDownToRefresh() {
                refreshLayout.pageNumber = 1;
                getRecyData(type, type_id, keywords);
            }

            @Override
            public void onPullUpToRefresh() {
                refreshLayout.pageNumber++;
                getRecyData(type, type_id, keywords);
            }
        });

        getRecyData(type, type_id, keywords);
    }


    private void getOneClassData() {
        MallHttpUtil.getOneClass(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    for (int i = 0; i < info.length; i++) {
                        JSONObject obj = JSON.parseObject(info[i]);

                        ShopOneClassBean shopOneClassBean = new ShopOneClassBean();
                        shopOneClassBean.setGc_name(obj.getString("gc_name"));
                        shopOneClassList.add(shopOneClassBean);
                    }
                    shopOneClassAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    private void getBanner2() {
        MallHttpUtil.getShopBanner2(new HttpCallback() {

            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    for (int i = 0; i < info.length; i++) {
                        JSONObject obj = JSON.parseObject(info[i]);
                        ShopBannerBean shopBannerBean = new ShopBannerBean();
                        shopBannerBean.setImage(obj.getString("image"));
                        shopBannerList2.add(shopBannerBean);
                    }
                    Glide.with(mContext).load(shopBannerList2.get(0).getImage()).into(iv_xinpin);
                    Glide.with(mContext).load(shopBannerList2.get(1).getImage()).into(iv_rexiao);
                }
            }
        });
    }

    private void getRecyData(String type, String type_id, String keywords) {

        MallHttpUtil.getShopGoodsType("hit", type_id, refreshLayout.pageNumber, keywords, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    for (int i = 0; i < info.length; i++) {
                        JSONObject object = JSON.parseObject(info[i]);
                        Log.e("eeeeeeeeeeeeeeee", "onSuccess: "+object );
                    }
                }
            }
        });
    }

    private void getBanner() {
        MallHttpUtil.getShopBanner(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    for (int i = 0; i < info.length; i++) {
                        JSONObject obj = JSON.parseObject(info[i]);
                        shopBannerList.add(obj.getString("image"));
                    }
                    banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                    banner.setImages(shopBannerList);
                    banner.setImageLoader(new GlideImageLoader());
                    banner.setBannerAnimation(Transformer.Default);
                    banner.setDelayTime(3000);
                    banner.isAutoPlay(true);
                    banner.setIndicatorGravity(BannerConfig.CENTER);
                    banner.start();
                }
            }
        });
    }

}
