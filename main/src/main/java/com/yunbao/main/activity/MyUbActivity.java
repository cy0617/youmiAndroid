package com.yunbao.main.activity;

import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.MyUbAdapter;
import com.yunbao.main.bean.MyUbBean;
import com.yunbao.main.utils.MyClickInterface;
import com.yunbao.main.views.refreshlayout.RefreshLayout;
import com.yunbao.mall.http.MallHttpUtil;

import java.util.ArrayList;

public class MyUbActivity extends AbsActivity implements MyClickInterface {

    private RecyclerView recyclerview;
    private ArrayList<MyUbBean> list;
    private MyUbAdapter adapter;
    private RefreshLayout refreshLayout;
    private Dialog mDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_ub;
    }

    @Override
    protected void main() {

        list = new ArrayList<>();
        mDialog = DialogUitl.loadingDialog(mContext, "加载中...");
        setTitle("U币流水");
        recyclerview = findViewById(R.id.refreshView);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        adapter = new MyUbAdapter(this, list, this);
        recyclerview.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onPullDownToRefresh() {
                refreshLayout.pageNumber=1;
                getData();
            }

            @Override
            public void onPullUpToRefresh() {
                refreshLayout.pageNumber++;
                getData();
            }
        });
        if (mDialog != null) {
            mDialog.show();
        }
        getData();
    }

    private void getData() {
        MallHttpUtil.GetUbrecoredList(refreshLayout.pageNumber, new HttpCallback() {
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
                            MyUbBean myUbBean = new MyUbBean();
                            myUbBean.setAction(obj.getString("action"));
                            myUbBean.setAddtime(obj.getString("addtime"));
                            myUbBean.setTotalcoin(obj.getString("totalcoin"));
                            myUbBean.setType(obj.getString("type"));
                            list.add(myUbBean);
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
