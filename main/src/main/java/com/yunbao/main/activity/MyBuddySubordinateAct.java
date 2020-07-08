package com.yunbao.main.activity;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.SubordinateAdapter;
import com.yunbao.main.bean.SubordinateBean;
import com.yunbao.main.http.MainHttpUtil;

import java.util.ArrayList;
import java.util.List;

public class MyBuddySubordinateAct extends AbsActivity {

    private RecyclerView recyclerView;
    private List<SubordinateBean> list;
    private ImageView btn_back;
    private TextView tv_title;
    private Dialog mDialog;
    private SubordinateAdapter adapter;
    private SmartRefreshLayout refreshlayout;
    private boolean refreshType;
    private int page;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_buddy_subordinate;
    }


    @Override
    protected void main() {

        final String leve = getIntent().getStringExtra("leve");
        list = new ArrayList<>();
        mDialog = DialogUitl.loadingDialog(mContext, "加载中...");
        btn_back = findViewById(R.id.btn_back);
        tv_title = findViewById(R.id.tv_title);
        recyclerView = findViewById(R.id.recyclerView);
        refreshlayout = findViewById(R.id.refreshLayout);
        tv_title.setText(leve + "网红");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SubordinateAdapter(this, list);
        recyclerView.setAdapter(adapter);

        refreshlayout.setEnableAutoLoadMore(true);
        refreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshType = true;
                        list.clear();
                        adapter.notifyDataSetChanged();
                        queryData(leve,page);
                        refreshLayout.finishRefresh();
                        refreshLayout.resetNoMoreData();
                    }
                }, 2000);
            }
        });

        refreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (page>1){
                            queryData(leve,page++);
                            adapter.notifyDataSetChanged();
                            refreshLayout.finishLoadMore();
                        }else {
                            refreshLayout.finishLoadMore();
                        }
                        refreshLayout.setEnableLoadMore(true);

                    }
                }, 2000);
            }
        });
        refreshlayout.autoRefresh();
        if (mDialog != null) {
            mDialog.show();
        }
        queryData(leve,page);

    }


    public void queryData(final String leve, int page) {
        MainHttpUtil.getSubordinate(page, leve, new HttpCallback() {

            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if (code == 0) {
                    for (int i = 0; i < info.length; i++) {
                        JSONObject obj = JSON.parseObject(info[i]);
                        SubordinateBean goodsBeanTwo = new SubordinateBean();
                        goodsBeanTwo.setId(obj.getString("id"));
                        String id = obj.getString("id");
                        goodsBeanTwo.setGrade(obj.getString("grade"));
                        goodsBeanTwo.setMobile(obj.getString("mobile"));
                        goodsBeanTwo.setWeixin(obj.getString("weixin"));
                        list.add(goodsBeanTwo);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.show(msg);
                }
            }

            @Override
            public void onError() {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            }
        });
    }


}
