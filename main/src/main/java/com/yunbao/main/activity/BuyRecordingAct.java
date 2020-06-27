package com.yunbao.main.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.bean.BuyRecordBean;
import com.yunbao.common.bean.GoodsBeanTwo;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.BuyRecordAdapter;
import com.yunbao.main.adapter.GoodsRingAdapter;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.main.utils.GridItemDecoration;
import com.yunbao.main.utils.MyClickInterface;
import com.yunbao.main.utils.StringUtil;
import com.yunbao.main.views.refreshlayout.RefreshLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BuyRecordingAct extends AbsActivity implements MyClickInterface {

    private List<BuyRecordBean> list;
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ImageView btn_back;
    private BuyRecordAdapter adapter;
    private Dialog mDialog;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_buy_recording;
    }

    @Override
    protected void main() {
        list = new ArrayList<>();
        mDialog = DialogUitl.loadingDialog(mContext, "加载中...");
        btn_back = findViewById(R.id.btn_back);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BuyRecordAdapter(this, list, this);
        recyclerView.setAdapter(adapter);


        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onPullDownToRefresh() {
                refreshLayout.pageNumber = 1;
                queryData();
            }

            @Override
            public void onPullUpToRefresh() {
                refreshLayout.pageNumber++;
                queryData();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (mDialog != null) {
            mDialog.show();
        }
        queryData();

    }


    public void queryData(){
        MainHttpUtil.getBuyRecordList(refreshLayout.pageNumber,"all",new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if (code == 0) {
                    if (refreshLayout.pageNumber == 1 && info.length > 0) {
                        list.clear();
                    }
                    for(int i = 0;i<info.length;i++){
                        JSONObject obj = JSON.parseObject(info[i]);
                        BuyRecordBean goodsBeanTwo = new BuyRecordBean();
                        goodsBeanTwo.setStatus(obj.getString("status"));
                        goodsBeanTwo.setSpec_thumb(obj.getString("spec_thumb"));
                        goodsBeanTwo.setStatus_name(obj.getString("status_name"));
                        goodsBeanTwo.setNums(obj.getString("nums"));
                        goodsBeanTwo.setAddtime(obj.getLong("addtime"));
                        goodsBeanTwo.setId(obj.getLong("id"));
                        goodsBeanTwo.setGoodsid(obj.getLong("goodsid"));
                        goodsBeanTwo.setGoods_name(obj.getString("goods_name"));
                        goodsBeanTwo.setPrice(StringUtil.subZeroAndDot(new BigDecimal(obj.getString("price"))
                                .setScale(2, BigDecimal.ROUND_DOWN).toPlainString()));
                        goodsBeanTwo.setTotal(StringUtil.subZeroAndDot(new BigDecimal(obj.getString("total"))
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
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                refreshLayout.onLoad(-1);
            }
        });
    }

    @Override
    public void myClick(int position, int type) {

    }
}
