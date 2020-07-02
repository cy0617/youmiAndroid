package com.yunbao.main.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.MyBuddyAdapter;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.main.utils.MyClickInterface;
import com.yunbao.main.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class MyBuddyActivity extends AbsActivity implements MyClickInterface {

    private ImageView btn_back;
    private ImageView iv_title_bg;
    private ImageView avatar;
    private TextView tv_id,tv_phone,tv_wx_code,tv_leve;
    private RecyclerView recyclerView;
    private List<String> list;
    private MyBuddyAdapter adapter;
    private Dialog mDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_buddy;
    }


    @Override
    protected void main() {
        list = new ArrayList<>();
        mDialog = DialogUitl.loadingDialog(mContext, "加载中...");
        btn_back = findViewById(R.id.btn_back);
        iv_title_bg = findViewById(R.id.iv_title_bg);
        avatar = findViewById(R.id.avatar);
        tv_id = findViewById(R.id.tv_id);
        tv_phone = findViewById(R.id.tv_phone);
        tv_wx_code = findViewById(R.id.tv_wx_code);
        tv_leve = findViewById(R.id.tv_leve);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyBuddyAdapter(this, list, this);
        recyclerView.setAdapter(adapter);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        UserBean userBean = CommonAppConfig.getInstance().getUserBean();

        Glide.with(mContext)
                .load(userBean.getAvatar())
                .error(R.mipmap.default_image)
                .placeholder( R.mipmap.default_image)
                .into(iv_title_bg);

        Glide.with(mContext)
                .load(userBean.getAvatar())
                .error(R.mipmap.default_image)
                .placeholder( R.mipmap.default_image)
                .into(avatar);

        tv_id.setText(userBean.getLiangNameTip());
        if(!StringUtil.isEmpty(userBean.getMobile())){
            tv_phone.setText(userBean.getMobile());
        }
        if(!StringUtil.isEmpty(userBean.getWeixin())){
            tv_wx_code.setText(userBean.getWeixin());
        }
        tv_leve.setText(userBean.getGrade()+"级");


        if (mDialog != null) {
            mDialog.show();
        }
        queryData();


    }


    public void queryData(){
        MainHttpUtil.getMyBuddyList(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if (code == 0) {
                    for(int i = 0;i<info.length;i++){
                        list.add(info[i]);
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



    @Override
    public void myClick(int position, int type) {
        Intent intent = new Intent(this,MyBuddySubordinateAct.class);
        intent.putExtra("type",type);
        intent.putExtra("leve",String.valueOf((position+1)));
        startActivity(intent);
    }
}
