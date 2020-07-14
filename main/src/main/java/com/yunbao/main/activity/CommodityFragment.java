package com.yunbao.main.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.MallSearchAdapter;
import com.yunbao.main.bean.MallSearchBean;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.main.utils.GridItemDecoration;
import com.yunbao.main.utils.MyClickInterface;
import com.yunbao.main.views.refreshlayout.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * 商城分类商品
 */
public class CommodityFragment extends Fragment implements View.OnClickListener, MyClickInterface {


    private View view;

    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private TextView tvSales;
    private TextView tvPrice;
    private ImageView ivOn;
    private LinearLayout llPrice;
    private TextView tvSelectionGoodProducts;
    private ImageView ivUnder;
    private TextView tvNewProduct;
    private List<MallSearchBean.ListBean> list = new ArrayList<>();
    private MallSearchAdapter adapter;
    private String type;//new最新， hot热销，hit首页（点击量）,pricemin价格小到大，pricemax价格大到小
    private boolean priceAscendDescend = true;//true 价格小到大 false 价格大到小
    private String typeId;

    public static CommodityFragment getInstance(String typeId) {
        CommodityFragment fragment = new CommodityFragment();
        Bundle bundle = new Bundle();
        bundle.putString("typeId", typeId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_commodity, container, false);
            initView();
        }
        return view;
    }

    private void initView() {
        Bundle bundle = getArguments();
        typeId = bundle.getString("typeId");


        tvSales = view.findViewById(R.id.tv_sales);
        llPrice = view.findViewById(R.id.ll_price);
        tvPrice = view.findViewById(R.id.tv_price);
        ivOn = view.findViewById(R.id.iv_on);
        ivUnder = view.findViewById(R.id.iv_under);
        tvNewProduct = view.findViewById(R.id.tv_new_product);
        tvSelectionGoodProducts = view.findViewById(R.id.tv_selection_good_products);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);

        tvSales.setOnClickListener(this);
        llPrice.setOnClickListener(this);
        tvNewProduct.setOnClickListener(this);


        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MallSearchAdapter(getActivity(), list, this);
        GridItemDecoration gridItemDecoration = new GridItemDecoration(10, 2, getActivity());
        recyclerView.addItemDecoration(gridItemDecoration);//10表示10dp
        recyclerView.setAdapter(adapter);

        getShopGoodsByType(type, typeId);

        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onPullDownToRefresh() {
                refreshLayout.pageNumber = 1;
                getShopGoodsByType(type, typeId);
            }

            @Override
            public void onPullUpToRefresh() {
                refreshLayout.pageNumber++;
                getShopGoodsByType(type, typeId);
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_sales) {

            tvSales.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_eb7946));
            tvPrice.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_66));
            ivOn.setVisibility(View.VISIBLE);
            ivUnder.setVisibility(View.VISIBLE);
            tvNewProduct.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_66));

            type = "hot";
            getShopGoodsByType(type, typeId);

        } else if (id == R.id.ll_price) {

            tvSales.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_66));
            tvPrice.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_eb7946));
            tvNewProduct.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_66));
            if (priceAscendDescend) {
                ivOn.setVisibility(View.VISIBLE);
                ivUnder.setVisibility(View.GONE);
                type = "pricemin";
            } else {
                ivOn.setVisibility(View.GONE);
                ivUnder.setVisibility(View.VISIBLE);
                type = "pricemax";
            }
            priceAscendDescend = !priceAscendDescend;
            getShopGoodsByType(type, typeId);

        } else if (id == R.id.tv_new_product) {
            tvSales.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_66));
            tvPrice.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_66));
            ivOn.setVisibility(View.VISIBLE);
            ivUnder.setVisibility(View.VISIBLE);
            tvNewProduct.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_eb7946));

            type = "new";
            getShopGoodsByType(type, typeId);
        }
    }

    public void getShopGoodsByType(String type, String typeId) {
        MainHttpUtil.getShopGoodsByType(type, typeId, refreshLayout.pageNumber, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    if (refreshLayout.pageNumber == 1) {
                        list.clear();
                    }
                    MallSearchBean searchBean = JSON.parseObject(info[0], MallSearchBean.class);
                    if (searchBean != null) {
                        if (searchBean.getList().size() > 0) {
                            list.addAll(searchBean.getList());
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.show(msg);
                }
                refreshLayout.onLoad(list.size());
            }

            @Override
            public void onError() {
                refreshLayout.onLoad(-1);
            }
        });
    }

    @Override
    public void myClick(int position, int type) {
        if (type == 1) {
            Intent intent = new Intent(getActivity(), GoodsDetailsActivity.class);
            intent.putExtra("goodsId", String.valueOf(list.get(position).getId()));
            getActivity().startActivity(intent);
        }
    }

}
