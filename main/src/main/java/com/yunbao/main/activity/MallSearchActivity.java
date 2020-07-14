package com.yunbao.main.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.KeyBoardUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.MallSearchAdapter;
import com.yunbao.main.bean.MallSearchBean;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.main.utils.GridItemDecoration;
import com.yunbao.main.utils.MyClickInterface;
import com.yunbao.main.utils.StringUtil;
import com.yunbao.main.views.refreshlayout.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 商城搜索
 */
public class MallSearchActivity extends AbsActivity implements MyClickInterface, View.OnClickListener {

    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private EditText etSearch;
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
    private String keywords;//关键词
    private boolean priceAscendDescend = true;//true 价格小到大 false 价格大到小

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mall_search;
    }

    @Override
    protected void main() {
        etSearch = findViewById(R.id.et_search);
        tvSales = findViewById(R.id.tv_sales);
        llPrice = findViewById(R.id.ll_price);
        tvPrice = findViewById(R.id.tv_price);
        ivOn = findViewById(R.id.iv_on);
        ivUnder = findViewById(R.id.iv_under);
        tvNewProduct = findViewById(R.id.tv_new_product);
        tvSelectionGoodProducts = findViewById(R.id.tv_selection_good_products);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etSearch.setText(getIntent().getStringExtra("keywords"));
        tvSales.setOnClickListener(this);
        llPrice.setOnClickListener(this);
        tvNewProduct.setOnClickListener(this);


        initData();
    }

    private void initData() {
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MallSearchAdapter(mContext, list, this);
        GridItemDecoration gridItemDecoration = new GridItemDecoration(10, 2, mContext);
        recyclerView.addItemDecoration(gridItemDecoration);//10表示10dp
        recyclerView.setAdapter(adapter);

        keywords = etSearch.getText().toString();
        if (!StringUtil.isEmpty(keywords)) {
            getShopGoodsByType(type, keywords);
        } else {
            ToastUtil.show(R.string.enter_search_content);
        }

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_SEND) {
                    keywords = etSearch.getText().toString();
                    getShopGoodsByType(type, keywords);
                    KeyBoardUtil.closeKeyboard(MallSearchActivity.this, etSearch);
                }
                return false;
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtil.isEmpty(etSearch.getText().toString())) {
                    tvSales.setTextColor(ContextCompat.getColor(MallSearchActivity.this, R.color.text_color_66));
                    tvPrice.setTextColor(ContextCompat.getColor(MallSearchActivity.this, R.color.text_color_66));
                    ivOn.setVisibility(View.VISIBLE);
                    ivUnder.setVisibility(View.VISIBLE);
                    tvNewProduct.setTextColor(ContextCompat.getColor(MallSearchActivity.this, R.color.text_color_66));
                    type = null;
                    keywords = null;
                    list.clear();
                    adapter.notifyDataSetChanged();
                } else {
                    //KeyBoardUtil.closeKeyboard(MallSearchActivity.this, etSearch);
                    etSearch.requestFocus();
                }
            }
        });

        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onPullDownToRefresh() {
                refreshLayout.pageNumber = 1;
                if (!StringUtil.isEmpty(keywords)) {
                    getShopGoodsByType(type, keywords);
                } else {
                    ToastUtil.show(R.string.enter_search_content);
                }
            }

            @Override
            public void onPullUpToRefresh() {
                refreshLayout.pageNumber++;
                if (!StringUtil.isEmpty(keywords)) {
                    getShopGoodsByType(type, keywords);
                } else {
                    ToastUtil.show(R.string.enter_search_content);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_sales) {
            if (!StringUtil.isEmpty(keywords)) {
                tvSales.setTextColor(ContextCompat.getColor(this, R.color.text_color_eb7946));
                tvPrice.setTextColor(ContextCompat.getColor(this, R.color.text_color_66));
                ivOn.setVisibility(View.VISIBLE);
                ivUnder.setVisibility(View.VISIBLE);
                tvNewProduct.setTextColor(ContextCompat.getColor(this, R.color.text_color_66));

                type = "hot";
                getShopGoodsByType(type, keywords);
            } else {
                ToastUtil.show(R.string.enter_search_content);
            }
        } else if (id == R.id.ll_price) {
            if (!StringUtil.isEmpty(keywords)) {
                tvSales.setTextColor(ContextCompat.getColor(this, R.color.text_color_66));
                tvPrice.setTextColor(ContextCompat.getColor(this, R.color.text_color_eb7946));
                tvNewProduct.setTextColor(ContextCompat.getColor(this, R.color.text_color_66));
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
                getShopGoodsByType(type, keywords);
            } else {
                ToastUtil.show(R.string.enter_search_content);
            }
        } else if (id == R.id.tv_new_product) {
            if (!StringUtil.isEmpty(keywords)) {
                tvSales.setTextColor(ContextCompat.getColor(this, R.color.text_color_66));
                tvPrice.setTextColor(ContextCompat.getColor(this, R.color.text_color_66));
                ivOn.setVisibility(View.VISIBLE);
                ivUnder.setVisibility(View.VISIBLE);
                tvNewProduct.setTextColor(ContextCompat.getColor(this, R.color.text_color_eb7946));

                type = "new";
                getShopGoodsByType(type, keywords);
            } else {
                ToastUtil.show(R.string.enter_search_content);
            }
        }
    }


    public void getShopGoodsByType(String type, String keywords) {
        MainHttpUtil.getShopGoodsByType(type, refreshLayout.pageNumber, keywords, new HttpCallback() {
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
            Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
            intent.putExtra("goodsId", String.valueOf(list.get(position).getId()));
            mContext.startActivity(intent);
        }
    }
}
