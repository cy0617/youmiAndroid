package com.yunbao.main.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
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

public class UsableMiLiActivity extends AbsActivity implements View.OnClickListener, MyClickInterface {

    private TextView tv_mili;
    private RecyclerView recyclerview;
    private ArrayList<LockRiceBean> list;
    private LockRiceAdapter adapter;
    private RefreshLayout refreshLayout;
    private Dialog mDialog;
    private String mType;
    private String ky_score;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_usable_mi_li;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_jieshou) {
            //接收
            Intent intent = new Intent(this, ReceptionActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.ll_suocang) {
//            锁仓
            Intent intent = new Intent(this, MiLiLockActivity.class);
            intent.putExtra("ky_score",ky_score);
            startActivityForResult(intent, 100);
        } else if (view.getId() == R.id.ll_fasong) {
            //发送
            Intent intent = new Intent(this, MiLiSendActivity.class);
            intent.putExtra("ky_score",ky_score);
            startActivity(intent);
        }
    }

    @Override
    protected void main() {
        setTitle("可用米粒");
        tv_mili = findViewById(R.id.tv_mili);
        findViewById(R.id.ll_jieshou).setOnClickListener(this);
        findViewById(R.id.ll_fasong).setOnClickListener(this);
        findViewById(R.id.ll_suocang).setOnClickListener(this);

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
        ky_score = CommonAppConfig.getInstance().getUserBean().getKy_score();
        tv_mili.setText(ky_score);
        MallHttpUtil.getScoreList(refreshLayout.pageNumber, "1", new HttpCallback() {
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
        Intent intent = getIntent();
        intent.putExtra("ky_score",mKy_score);
        setResult(101,intent);
    }

    @Override
    public void myClick(int position, int type) {
        mType = list.get(position).getType();
    }
private String mKy_score;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100){
            if (resultCode==101){
               mKy_score = data.getStringExtra("ky_score");
                tv_mili.setText(mKy_score);
            }
        }
    }
}
