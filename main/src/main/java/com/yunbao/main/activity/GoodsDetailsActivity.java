package com.yunbao.main.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.bean.BuyRecordBean;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.main.utils.StringUtil;
import com.yunbao.main.views.QuantityView;
import com.yunbao.mall.http.MallHttpUtil;

import java.math.BigDecimal;

public class GoodsDetailsActivity extends AbsActivity {

    private ImageView iv_back;
    private ImageView iv_goods_img;
    private TextView tv_name,tv_money,tv_desc,btn_register;
    private QuantityView quantityView;
    private String goodsId;
    private Dialog mDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_details;
    }

    @Override
    protected void main() {
        goodsId = getIntent().getStringExtra("goodsId");
        iv_back = findViewById(R.id.iv_back);
        iv_goods_img = findViewById(R.id.iv_goods_img);
        tv_name = findViewById(R.id.tv_name);
        tv_money = findViewById(R.id.tv_money);
        tv_desc = findViewById(R.id.tv_desc);
        btn_register = findViewById(R.id.btn_register);
        quantityView = findViewById(R.id.quantityView);

        mDialog = DialogUitl.loadingDialog(mContext, "加载中...");


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsDetailsActivity.this,ConfirmOrderActivity.class);
                intent.putExtra("img",img);
                intent.putExtra("title",tv_name.getText().toString());
                intent.putExtra("money",tv_money.getText().toString());
                intent.putExtra("num",quantityView.getNum());
                intent.putExtra("goodsId",goodsId);
                startActivityForResult(intent,101);
            }
        });


        if (mDialog != null) {
            mDialog.show();
        }
        queryData();
    }

    private String img;

    public void queryData(){
        MallHttpUtil.getGoodsPackageInfo(goodsId,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if (code == 0&& info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    img = obj.getString("img");
                    Glide.with(mContext)
                            .load(img)
                            .error(R.mipmap.default_image)
                            .placeholder( R.mipmap.default_image)
                            .into(iv_goods_img);
                    tv_name.setText(obj.getString("title"));
                    tv_money.setText(obj.getString("money"));
                    tv_desc.setText(obj.getString("desc"));
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101){
            if(resultCode == 333){
                setResult(333);
                finish();
            }
        }

    }


}
