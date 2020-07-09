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
import com.yunbao.main.adapter.LockRiceAdapter;
import com.yunbao.main.bean.LockRiceBean;
import com.yunbao.main.utils.MyClickInterface;
import com.yunbao.main.views.refreshlayout.RefreshLayout;
import com.yunbao.mall.http.MallHttpUtil;

import java.util.ArrayList;

public class LockRiceActivity extends AbsActivity implements MyClickInterface {

    private RecyclerView recyclerview;
    private ArrayList<LockRiceBean> list;
    private LockRiceAdapter adapter;
    private RefreshLayout refreshLayout;
    private Dialog mDialog;
    private String mType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lock_rice;
    }

    @Override
    protected void main() {
        setTitle("锁仓米粒流水");
        list = new ArrayList<>();

        mDialog = DialogUitl.loadingDialog(mContext, "加载中...");
        recyclerview = findViewById(R.id.refreshView);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        adapter = new LockRiceAdapter(this, list, this);
        recyclerview.setAdapter(adapter);
        if (mDialog != null) {
            mDialog.show();
        }
        getData();
        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onPullDownToRefresh() {
                refreshLayout.pageNumber = 1;
                getData();
            }

            @Override
            public void onPullUpToRefresh() {
                refreshLayout.pageNumber++;
                getData();
            }
        });

    }

    private void getData() {
        MallHttpUtil.getScoreList(refreshLayout.pageNumber, "2", new HttpCallback() {
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
                        LockRiceBean lockRiceBean = new LockRiceBean();
                        lockRiceBean.setMemo(obj.getString("memo"));
                        lockRiceBean.setTime(obj.getString("time"));
//                        mType = obj.getString("type");
                        lockRiceBean.setType(obj.getString("type"));
                        lockRiceBean.setScore(obj.getString("score"));
                        lockRiceBean.setAfter(obj.getString("after"));
                        list.add(lockRiceBean);
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
        mType = list.get(position).getType();
    }
}
