package com.yunbao.main.activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.bean.BuyRecordBean;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.BuyRecordAdapter;
import com.yunbao.main.adapter.MyBountyAdapter;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.main.utils.StringUtil;
import com.yunbao.main.views.refreshlayout.RefreshLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * 我的赏金
 */
public class MyBountyActivity extends AbsActivity {


    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ImageView btn_back;
    private Dialog mDialog;
    private List<String> list;
    private MyBountyAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_bounty;
    }


    @Override
    protected void main() {
        list = new ArrayList<>();
        btn_back = findViewById(R.id.btn_back);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);

        mDialog = DialogUitl.loadingDialog(mContext, "加载中...");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyBountyAdapter(this, list);
        recyclerView.setAdapter(adapter);


//        if (mDialog != null) {
//            mDialog.show();
//        }
//        queryData();


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


}
