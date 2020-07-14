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
import com.yunbao.main.adapter.ShopListAdapter;
import com.yunbao.main.adapter.ShopOneClassAdapter;
import com.yunbao.main.bean.ShopBannerBean;
import com.yunbao.main.bean.ShopGoodsTypeBean;
import com.yunbao.main.bean.ShopOneClassBean;
import com.yunbao.main.views.refreshlayout.RefreshLayout;
import com.yunbao.mall.activity.GoodsDetailActivity;
import com.yunbao.mall.http.MallHttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllMallFragment extends Fragment implements View.OnClickListener {

    private Banner banner;
    private ArrayList<ShopBannerBean> shopBannerList;
    private ArrayList<ShopBannerBean> shopBannerList2;
    private List<ShopOneClassBean> shopOneClassList;
    private RecyclerView recyclerview;
    private RefreshLayout refreshLayout;
    private RecyclerView one_recy;
    private ShopOneClassAdapter shopOneClassAdapter;
    private ImageView iv_xinpin;
    private ImageView iv_rexiao;

    private String type = "";
    private String type_id = "";
    private String keywords = "";
    private ArrayList<ShopGoodsTypeBean> shopGoodsTypeBeans;
    private ShopListAdapter shopListAdapter;
    private Pattern httpPattern;

    //初始化正则
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_mall, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        iv_xinpin = view.findViewById(R.id.iv_xinpin);
        iv_rexiao = view.findViewById(R.id.iv_rexiao);
        recyclerview = view.findViewById(R.id.recyclerView);
        view.findViewById(R.id.rl_hot).setOnClickListener(this);
        view.findViewById(R.id.rl_new).setOnClickListener(this);
        banner = view.findViewById(R.id.banner);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        one_recy = view.findViewById(R.id.oneclassrecy);
        LinearLayout llBusinessDistrict = view.findViewById(R.id.ll_business_district);

        llBusinessDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ShangQuanActivity.class));
            }
        });

        shopBannerList = new ArrayList<>();
        shopBannerList2 = new ArrayList<>();
        shopOneClassList = new ArrayList<>();
        shopGoodsTypeBeans = new ArrayList<>();


        getOneClassData();
        getBanner();
        getBanner2();
        /**
         * 轮播图点击
         */
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

                String url = shopBannerList.get(position).getUrl();
                httpPattern = Pattern
                        .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$");
                if (httpPattern.matcher(url).matches()) {
                    Uri uri = Uri.parse(url);//要跳转的网址
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(),GoodsDetailsActivity.class);
                    intent.putExtra("goodsId",url);
                    startActivity(intent);
                }

            }
        });
        shopListAdapter = new ShopListAdapter(shopGoodsTypeBeans, getActivity());
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerview.setAdapter(shopListAdapter);
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


        getRecyData("hit", type_id, keywords);
        shopListAdapter.setActionListener(new ShopListAdapter.ActionListener() {
            @Override
            public void onClick(int position) {
                String id = shopGoodsTypeBeans.get(position).getId();
                Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                intent.putExtra(Constants.MALL_GOODS_ID,id);
                startActivity(intent);
            }
        });
    }

    private void getOneClassData() {
        one_recy.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        shopOneClassAdapter = new ShopOneClassAdapter(getContext(), shopOneClassList);
        one_recy.setAdapter(shopOneClassAdapter);
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
                    Glide.with(getContext()).load(shopBannerList2.get(0).getImage()).into(iv_xinpin);
                    Glide.with(getContext()).load(shopBannerList2.get(1).getImage()).into(iv_rexiao);
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
                        shopListAdapter.notifyDataSetChanged();
                    }
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

    private void getBanner() {
        MallHttpUtil.getShopBanner(new HttpCallback() {

            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    for (int i = 0; i < info.length; i++) {
                        JSONObject obj = JSON.parseObject(info[i]);
                        ShopBannerBean shopBannerBean = new ShopBannerBean();
                        shopBannerBean.setImage(obj.getString("image"));
                        shopBannerBean.setUrl(obj.getString("url"));
                        shopBannerList.add(shopBannerBean);
                    }
                    banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                    banner.setImages(shopBannerList);
                    banner.setImageLoader(new ImageLoader() {
                        @Override
                        public void displayImage(Context context, Object path, ImageView imageView) {
                            ImgLoader.display(getActivity(), ((ShopBannerBean) path).getImage(), imageView);
                        }
                    });
                    banner.setBannerAnimation(Transformer.Default);
                    banner.setDelayTime(3000);
                    banner.isAutoPlay(true);
                    banner.setIndicatorGravity(BannerConfig.CENTER);
                    banner.start();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_hot) {
            getRecyData("hot", type_id, keywords);
        } else if (view.getId() == R.id.rl_new) {
            getRecyData("new", type_id, keywords);
        }
    }

}
