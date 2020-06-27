package com.yunbao.main.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.utils.StringUtil;
import com.yunbao.mall.http.MallHttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyUserRenZhenAct extends AbsActivity {


    private ImageView btn_back;
    private EditText edit_name;
    private EditText edit_sfz_num;
    private TextView btn_xiayibu;
    private Dialog mDialog;
    private Dialog subDialog;
    private String payMoney="";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_user_ren_zhen;
    }


    @Override
    protected void main() {
        mDialog = DialogUitl.loadingDialog(mContext, "加载中...");
        subDialog = DialogUitl.loadingDialog(mContext, "提交中...");
        btn_back =  findViewById(R.id.btn_back);
        edit_name =  findViewById(R.id.edit_name);
        edit_sfz_num =  findViewById(R.id.edit_sfz_num);
        btn_xiayibu =  findViewById(R.id.btn_xiayibu);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_xiayibu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subDialog != null) {
                    subDialog.show();
                }
                submitOrder();
            }
        });

        if (mDialog != null) {
            mDialog.show();
        }
        isRz();
    }


    /**
     * 提交订单
     */
    public void submitOrder(){

        if(StringUtil.isEmpty(payMoney)){
            ToastUtil.show("金额异常");
            return;
        }

        MallHttpUtil.getSubmitRzOrder(payMoney,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (subDialog != null) {
                    subDialog.dismiss();
                }
                if (code == 0&& info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    Intent intent = new Intent(MyUserRenZhenAct.this,PayActivity.class);
                    intent.putExtra("orderid",obj.getString("orderid"));
                    intent.putExtra("money",payMoney);
                    intent.putExtra("name","");
                    intent.putExtra("realName",edit_name.getText().toString());
                    intent.putExtra("cerNo",edit_sfz_num.getText().toString());
                    intent.putExtra("type","2");
                    startActivityForResult(intent,101);
                } else {
                    ToastUtil.show(msg);
                }
            }
            @Override
            public void onError() {
                if (subDialog != null) {
                    subDialog.dismiss();
                }
            }
        });
    }



    /**
     * 检测是否认证
     */
    public void isRz(){

        OkHttpClient client = new OkHttpClient();
        Request request = new  Request.Builder()
                .get()
                .url("http://cz56.yczbfx.com/appapi/?service=Youmio.Getuserauth&uid="+ CommonAppConfig.getInstance().getUid()+"&token="+CommonAppConfig.getInstance().getToken())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    String string = response.body().string();
                    JSONObject obj = JSON.parseObject(string);
                    JSONObject data = obj.getJSONObject("data");
                    if(data.getString("code").equals("0")){
                        //未认证成功
                        if (btn_xiayibu != null) {
                            btn_xiayibu.post(new Runnable() {
                                @Override
                                public void run() {
                                    btn_xiayibu.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                        payMoney = data.getString("info");
                    }else if(data.getString("code").equals("1")){
                        //认证成功
                        final JSONObject info = data.getJSONObject("info");
                        if (btn_xiayibu != null) {
                            btn_xiayibu.post(new Runnable() {
                                @Override
                                public void run() {
                                    btn_xiayibu.setVisibility(View.GONE);
                                }
                            });
                        }
                        if (edit_name != null) {
                            edit_name.post(new Runnable() {
                                @Override
                                public void run() {
                                    edit_name.setText(info.getString("real_name"));
                                }
                            });
                        }

                        if (edit_sfz_num != null) {
                            edit_sfz_num.post(new Runnable() {
                                @Override
                                public void run() {
                                    edit_sfz_num.setText(info.getString("cer_no"));
                                }
                            });
                        }


                    }else {
                        ToastUtil.show(data.getString("msg"));
                    }
                }
            }

        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            if(resultCode == 333){
                if (mDialog != null) {
                    mDialog.show();
                }
                isRz();
            }
        }
    }


}
