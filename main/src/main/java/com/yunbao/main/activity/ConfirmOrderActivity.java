package com.yunbao.main.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.utils.StringUtil;
import com.yunbao.main.views.QuantityView;
import com.yunbao.mall.activity.BuyerAddressActivity;
import com.yunbao.mall.bean.BuyerAddressBean;
import com.yunbao.mall.http.MallHttpUtil;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class ConfirmOrderActivity extends AbsActivity {

    private ImageView iv_picture;
    private ImageView btn_back;
    private TextView tv_name, tv_money, tv_num, tv_goods_sum, tv_money_sum, tv_addrs_name, tv_addrs_phone, tv_addrs_text, tv_no_addrs, tv_submit;
    private LinearLayout ll_addrs;
    private QuantityView quantityView;
    private Dialog mDialog;
    private String addrsId = "";
    private String goodsId;
    private RelativeLayout rl_add_address;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_confirm_order;
    }

    @Override
    protected void main() {

        goodsId = getIntent().getStringExtra("goodsId");

        mDialog = DialogUitl.loadingDialog(mContext, "加载中...");

        btn_back = findViewById(R.id.btn_back);
        ll_addrs = findViewById(R.id.ll_addrs);
        tv_no_addrs = findViewById(R.id.tv_no_addrs);
        tv_addrs_name = findViewById(R.id.tv_addrs_name);
        tv_submit = findViewById(R.id.tv_submit);
        tv_addrs_phone = findViewById(R.id.tv_addrs_phone);
        tv_addrs_text = findViewById(R.id.tv_addrs_text);
        rl_add_address = findViewById(R.id.rl_add_address);

        iv_picture = findViewById(R.id.iv_picture);
        tv_name = findViewById(R.id.tv_name);
        tv_money = findViewById(R.id.tv_money);
        tv_num = findViewById(R.id.tv_num);
        tv_goods_sum = findViewById(R.id.tv_goods_sum);
        tv_money_sum = findViewById(R.id.tv_money_sum);
        quantityView = findViewById(R.id.quantityView);
        rl_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ConfirmOrderActivity.this, BuyerAddressActivity.class),101);
//                BuyerAddressActivity.forward(mContext);
            }
        });

        quantityView.setCallBack(new QuantityView.CallBack() {
            @Override
            public void add(int numTwo) {
                tv_num.setText("x" + numTwo);
                tv_goods_sum.setText("共" + numTwo + "件，合计");
                String money = getIntent().getStringExtra("money");
                DecimalFormat format = new DecimalFormat("#.##");
                float v = Float.parseFloat(money) * numTwo;
                String money_sum = format.format(v);
                tv_money_sum.setText(money_sum);
            }

            @Override
            public void sub(int numTwo) {
                tv_num.setText("x" + numTwo);
                tv_goods_sum.setText("共" + numTwo + "件，合计");
                String money = getIntent().getStringExtra("money");
                DecimalFormat format = new DecimalFormat("#.##");
                float v = Float.parseFloat(money) * numTwo;
                String money_sum = format.format(v);
                tv_money_sum.setText(money_sum);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        String img = getIntent().getStringExtra("img");
        String title = getIntent().getStringExtra("title");
        String money = getIntent().getStringExtra("money");
        int num = getIntent().getIntExtra("num", 0);


        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(addrsId)) {
                    showToast("请选择收货地址");
                    return;
                }
                if (mDialog != null) {
                    mDialog.show();
                }
                submitOrder(addrsId, goodsId, String.valueOf(quantityView.getNum()), "");
            }
        });


        Glide.with(mContext)
                .load(img)
                .error(R.mipmap.default_image)
                .placeholder(R.mipmap.default_image)
                .into(iv_picture);

        tv_name.setText(title);
        tv_money.setText(money);
        tv_num.setText("x" + num);
        tv_goods_sum.setText("共" + num + "件，合计");
        tv_money_sum.setText(money);
        quantityView.setNum(num);
        if (mDialog != null) {
            mDialog.show();
        }
        queryAddrsData();

    }


    public void showAddress(BuyerAddressBean bean) {
        ll_addrs.setVisibility(View.VISIBLE);
        tv_no_addrs.setVisibility(View.GONE);
        addrsId = bean.getId();
        tv_addrs_name.setText(bean.getName());
        tv_addrs_phone.setText(bean.getPhoneNum());
        tv_addrs_text.setText(bean.getProvince() + bean.getCity() + bean.getZone() + bean.getAddress());

    }

    /**
     * 查询收货地址
     */
    public void queryAddrsData() {
        MallHttpUtil.getBuyerAddress(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if (code == 0 && info.length > 0) {
                    List<BuyerAddressBean> addressList = JSON.parseArray(Arrays.toString(info), BuyerAddressBean.class);
                    for (final BuyerAddressBean bean : addressList) {
                        if (bean.getIsDefault() == 1) {
                            showAddress(bean);
                            break;
                        }
                    }
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


    /**
     * 提交订单
     */
    public void submitOrder(String adId, String gdId, String nums, String message) {
        MallHttpUtil.getSubmitOrder(adId, gdId, nums, message, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }

                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    Intent intent = new Intent(ConfirmOrderActivity.this, PayActivity.class);
                    intent.putExtra("orderid", obj.getString("orderid"));
                    intent.putExtra("money", tv_money_sum.getText().toString());
                    intent.putExtra("name", tv_name.getText().toString());
                    intent.putExtra("type", "1");
                    startActivityForResult(intent, 101);
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

        if (requestCode == 101) {
            if (resultCode == 333) {
                setResult(333);
                finish();
            }else if(resultCode == 334){

                String name = data.getStringExtra("name");
                String phonenum = data.getStringExtra("phonenum");
                String address = data.getStringExtra("address");
                tv_addrs_name.setText(name);
                tv_addrs_phone.setText(phonenum);
                tv_addrs_text.setText(address);
            }
        }

    }
}
