package com.yunbao.main.activity;

import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.SubordinateAdapter;
import com.yunbao.main.bean.SubordinateBean;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.main.views.refreshlayout.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class MyBuddySubordinateAct extends AbsActivity {

    private RecyclerView recyclerView;
    private List<SubordinateBean> list;
    private ImageView btn_back;
    private TextView tv_title;
    private Dialog mDialog;
    private SubordinateAdapter adapter;
    private RefreshLayout refreshLayout;
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
        recyclerView = findViewById(R.id.refreshView);
        refreshLayout = findViewById(R.id.refreshLayout);
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
        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onPullDownToRefresh() {
                refreshLayout.pageNumber = 1;
                queryData(leve);
            }

            @Override
            public void onPullUpToRefresh() {
                refreshLayout.pageNumber++;
                queryData(leve);
            }
        });
        if (mDialog != null) {
            mDialog.show();
        }
        if (mDialog != null) {
            mDialog.show();
        }
        queryData(leve);

    }


    public void queryData(final String leve) {
        MainHttpUtil.getSubordinate(refreshLayout.pageNumber, leve, new HttpCallback() {

            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if (code == 0) {
                    if (refreshLayout.pageNumber == 1 && info.length > 0) {
                        list.clear();
                    }
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
