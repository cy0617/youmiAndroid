package com.yunbao.main.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.RewardListAdapter;
import com.yunbao.main.bean.RewardRecordBean;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.main.utils.MyClickInterface;
import com.yunbao.main.views.MaxImgDialog;
import com.yunbao.main.views.TiShiDialog;
import com.yunbao.main.views.refreshlayout.RefreshLayout;
import com.yunbao.mall.http.MallHttpUtil;

import java.util.ArrayList;
import java.util.List;

public class RewardForPromotionAct extends AbsActivity implements View.OnClickListener, MyClickInterface {


    private TextView tv_no_advance;
    private TextView tv_yes_advance;
    private EditText edit_search;
    private View link_no_advance;
    private View link_yes_advance;
    private ImageView btn_back;
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private List<RewardRecordBean> list;
    private RewardListAdapter adapter;
    private Dialog mDialog;
    private String type = "1";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reward_for_promotion;
    }


    @Override
    protected void main() {
        list = new ArrayList<>();
        mDialog = DialogUitl.loadingDialog(mContext, "加载中...");

        btn_back = findViewById(R.id.btn_back);
        tv_no_advance = findViewById(R.id.tv_no_advance);
        edit_search = findViewById(R.id.edit_search);
        tv_yes_advance = findViewById(R.id.tv_yes_advance);
        link_no_advance = findViewById(R.id.link_no_advance);
        link_yes_advance = findViewById(R.id.link_yes_advance);

        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);

        findViewById(R.id.ll_no_advance).setOnClickListener(this);
        findViewById(R.id.ll_yes_advance).setOnClickListener(this);
        findViewById(R.id.ll_sousuo).setOnClickListener(this);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RewardListAdapter(this, list,this);
        recyclerView.setAdapter(adapter);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onPullDownToRefresh() {
                refreshLayout.pageNumber = 1;
                queryData(type,edit_search.getText().toString());
            }

            @Override
            public void onPullUpToRefresh() {
                refreshLayout.pageNumber++;
                queryData(type,edit_search.getText().toString());
            }
        });



        if (mDialog != null) {
            mDialog.show();
        }
        queryData(type,edit_search.getText().toString());

    }


    public void queryData(String type,String keyword){
        MainHttpUtil.getRewardRecordList(refreshLayout.pageNumber,type,keyword,new HttpCallback() {
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
                        RewardRecordBean rewardRecordBean = new RewardRecordBean();
                        rewardRecordBean.setId(obj.getString("id"));
                        rewardRecordBean.setPid(obj.getString("pid"));
                        rewardRecordBean.setMoney(obj.getString("money"));
                        rewardRecordBean.setWxCode(obj.getString("weixin"));
                        rewardRecordBean.setPhone(obj.getString("mobile"));
                        rewardRecordBean.setImg(obj.getString("img"));
                        rewardRecordBean.setStatus(obj.getString("status"));
                        list.add(rewardRecordBean);
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



    public void initTitleStatus(){
        tv_no_advance.setTextColor(Color.parseColor("#999999"));
        tv_yes_advance.setTextColor(Color.parseColor("#999999"));

        link_no_advance.setVisibility(View.GONE);
        link_yes_advance.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ll_no_advance){
            //待晋级
            type = "1";
            initTitleStatus();
            tv_no_advance.setTextColor(Color.parseColor("#333333"));
            link_no_advance.setVisibility(View.VISIBLE);

            refreshLayout.pageNumber = 1;
            queryData(type,edit_search.getText().toString());
        }else if(v.getId() == R.id.ll_yes_advance){
            //已晋级
            type = "2";
            initTitleStatus();
            tv_yes_advance.setTextColor(Color.parseColor("#333333"));
            link_yes_advance.setVisibility(View.VISIBLE);


            refreshLayout.pageNumber = 1;
            queryData(type,edit_search.getText().toString());
        }else if(v.getId() == R.id.ll_sousuo){
            //搜索
            refreshLayout.pageNumber = 1;
            if (mDialog != null) {
                mDialog.show();
            }
            queryData(type,edit_search.getText().toString());
        }
    }

    @Override
    public void myClick(int position, int type) {
        if(type ==1){
            //批准
            showCodeDialog("是否确认已收到该打赏，确认后不可取消？",list.get(position).getId());
        }else if(type == 2){
            //放大图片
            showJJDialog(list.get(position).getImg());
        }
    }

    /**
     * 提示按钮
     */
    private void showCodeDialog(String desc, final String id) {
        TiShiDialog tiShiDialog = new TiShiDialog(mContext) {
            @Override
            public void ok() {
                super.ok();
                dismiss();
                if (mDialog != null) {
                    mDialog.show();
                }
                approveUpgrade(id);
            }
        };
        tiShiDialog.setTitleStr("提示");
        tiShiDialog.setDescStr(desc);
        tiShiDialog.show();
    }

    /**
     * 我要晋级弹框
     */
    private void showJJDialog(String ImgUrl) {
        MaxImgDialog yqJJDialog = new MaxImgDialog(this) {
            @Override
            public void ok() {
                dismiss();
            }
        };
        yqJJDialog.setMaxImg(ImgUrl);
        yqJJDialog.show();
    }



    /**
     * 批准升级
     */
    public void approveUpgrade(String id){
        MallHttpUtil.getPiZhunUpgrade(id,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if (code == 0) {
                    ToastUtil.show("确认成功");
                    refreshLayout.pageNumber = 1;
                    queryData(type,edit_search.getText().toString());
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
