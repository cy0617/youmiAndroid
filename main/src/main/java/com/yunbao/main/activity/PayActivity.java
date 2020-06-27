package com.yunbao.main.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.megvii.meglive_sdk.listener.DetectCallback;
import com.megvii.meglive_sdk.listener.PreCallback;
import com.megvii.meglive_sdk.manager.MegLiveManager;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.HtmlConfig;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.pay.PayCallback;
import com.yunbao.common.pay.PayPresenter;
import com.yunbao.common.pay.ali.AliPayBuilder;
import com.yunbao.common.pay.wx.WxPayBuilder;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.MD5Util;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.im.event.ImUnReadCountEvent;
import com.yunbao.main.R;
import com.yunbao.main.adapter.BuyRecordAdapter;
import com.yunbao.main.adapter.PayTypeAdapter;
import com.yunbao.main.facerecognition.GenerateSign;
import com.yunbao.main.facerecognition.HttpRequestCallBack;
import com.yunbao.main.facerecognition.HttpRequestManager;
import com.yunbao.main.utils.MyClickInterface;
import com.yunbao.mall.adapter.GoodsPayAdapter;
import com.yunbao.mall.bean.GoodsPayBean;
import com.yunbao.mall.http.MallHttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.os.Build.VERSION_CODES.M;


public class PayActivity extends AbsActivity implements MyClickInterface , DetectCallback, PreCallback {

    private TextView tv_money;
    private TextView tv_name;
    private TextView tv_money_mili;
    private TextView btn_submit;
    private ImageView btn_back;

    private String orderid;
    private String money;
    private String name;
    private RecyclerView recyclerView;
    private PayTypeAdapter adapter;
    private List<GoodsPayBean> list;

    private Dialog mDialog;
    private Dialog payDialog;
    private String actType;
    private String realName="";
    private String cerNo="";


    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int EXTERNAL_STORAGE_REQ_WRITE_EXTERNAL_STORAGE_CODE = 101;
    private static final int ACTION_YY = 1;
    private int buttonType;
    private ProgressDialog mProgressDialog;
    private static final String GET_BIZTOKEN_URL = "https://api.megvii.com/faceid/v3/sdk/get_biz_token";
    private static final String VERIFY_URL = "https://api.megvii.com/faceid/v3/sdk/verify";
    private static final String SIGN_VERSION = "hmac_sha1";
    private String sign = "";
    private static final String API_KEY = "XUKr15RvgbZPm9HOCo5EAKHIw1q_boHN";
    private static final String SECRET = "o3zU6YEjYrUT0eTYL_xMo3NBASs20-BJ";
    private MegLiveManager megLiveManager;


    private PayCallback mPayCallback = new PayCallback() {
        @Override
        public void onSuccess() {
            ToastUtil.show("支付成功");
            if(actType.equals("1")){
                //商圈
                EventBus.getDefault().post(new ImUnReadCountEvent("getUserInfo"));
                setResult(333);
                startActivity(new Intent(mContext, BuyRecordingAct.class));
                finish();
            } else if (actType.equals("2")) {
                //认证
                buttonType = ACTION_YY;
                requestCameraPerm();
            }

        }

        @Override
        public void onFailed() {
            ToastUtil.show(com.yunbao.mall.R.string.mall_367);
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    protected void main() {
        list = new ArrayList<>();

        megLiveManager= MegLiveManager.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        long currtTime = System.currentTimeMillis() / 1000;
        long expireTime = (System.currentTimeMillis() + 60 * 60 * 100) / 1000;
        sign = GenerateSign.appSign(API_KEY, SECRET, currtTime, expireTime);


        orderid = getIntent().getStringExtra("orderid");
        money = getIntent().getStringExtra("money");
        name = getIntent().getStringExtra("name");
        actType = getIntent().getStringExtra("type");
        if(actType.equals("2")){
            realName = getIntent().getStringExtra("realName");
            cerNo = getIntent().getStringExtra("cerNo");
        }

        mDialog = DialogUitl.loadingDialog(mContext, "加载中...");
        payDialog = DialogUitl.loadingDialog(mContext, "支付中...");

        btn_back = findViewById(R.id.btn_back);
        btn_submit = findViewById(R.id.btn_submit);
        tv_money = findViewById(R.id.tv_money);
        tv_name = findViewById(R.id.tv_name);
        tv_money_mili = findViewById(R.id.tv_money_mili);
        recyclerView = findViewById(R.id.recyclerView);



        tv_money.setText(money);
        tv_name.setText(name);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PayTypeAdapter(this, list, this);
        recyclerView.setAdapter(adapter);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPay();
            }
        });

        if (mDialog != null) {
            mDialog.show();
        }
        queryPayType();
    }


    private String aliapp_partner;
    private String aliapp_seller_id;
    private String aliapp_key_android;
    private String yuEmoney;



    /**
     * 获取支付方式
     */
    public void queryPayType(){
        MallHttpUtil.getPayContentPayList(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);


                    tv_money_mili.setText(obj.getString("ky_score"));
                    adapter.setBalance(obj.getString("balance"));

                    yuEmoney = obj.getString("balance");

                    aliapp_partner = obj.getString("aliapp_partner");
                    aliapp_seller_id = obj.getString("aliapp_seller_id");
                    aliapp_key_android = obj.getString("aliapp_key_android");


                    List<GoodsPayBean> payList = JSON.parseArray(obj.getString("paylist"), GoodsPayBean.class);
                    if (payList != null && payList.size() > 0) {
                        payList.get(0).setChecked(true);
                        list.addAll(payList);
                        adapter.notifyDataSetChanged();
                    }
                }else {
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



    private void showPay() {
        if (TextUtils.isEmpty(orderid)) {
            showToast("订单不存在");
            return;
        }

        GoodsPayBean bean = null;
        Log.e("asfqweqweq","1111111111111111111");
        for(int i = 0;i<list.size();i++){
            if(list.get(i).isChecked()){
                bean = list.get(i);
            }
        }
        if (bean == null) {
            showToast("选择支付方式异常");
            return;
        }
        Log.e("asfqweqweq","22222222222222222");
        String type = bean.getType();
        if(type.equals("3")){
            Log.e("asfqweqweq","33333333333333333");
            String[] strs = yuEmoney.split("￥");
            Log.e("asfqweqweq","4444444444444444444");
            if(strs.length>=2){
                int a = new BigDecimal(strs[1]).compareTo(new BigDecimal(tv_money.getText().toString()));
                if(a == -1){
                    //余额小于商品价格
                    showToast("余额不足");
                    return;
                }
            }
            Log.e("asfqweqweq","555555555555555555");
        }else if(type.equals("4")){
            if(CommonAppConfig.getInstance().getUserBean()==null){
                showToast("米粒不足");
                return;
            }
            if(com.yunbao.main.utils.StringUtil.isEmpty(CommonAppConfig.getInstance().getUserBean().getKy_score())){
                showToast("米粒不足");
                return;
            }

            int b = new BigDecimal(CommonAppConfig.getInstance().getUserBean().getKy_score()).compareTo(new BigDecimal("0"));
            if(b == -1){
                //米粒小于0
                showToast("米粒不足");
                return;
            }
        }
        if (payDialog != null) {
            payDialog.show();
        }
        if(actType.equals("1")){
            balancePay(type,bean.getId());
        }else if(actType.equals("2")){
            balanceRzPay(type,bean.getId());
        }
    }


    /**
     * 商圈支付第一步
     */
    private void balancePay(final String payType, final String id) {
        MallHttpUtil.sqPayOrder(orderid, payType, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (payDialog != null) {
                    payDialog.dismiss();
                }
                if (code == 0) {
                    if(payType.equals("3")||payType.equals("4")){
                        ToastUtil.show("支付成功");
                        EventBus.getDefault().post(new ImUnReadCountEvent("getUserInfo"));
                        setResult(333);
                        startActivity(new Intent(mContext, BuyRecordingAct.class));
                        finish();
                    }else{
                        JSONObject obj = JSON.parseObject(info[0]);
                        String time = String.valueOf(System.currentTimeMillis() / 1000);
                        CommonAppConfig appConfig = CommonAppConfig.getInstance();
                        String uid = appConfig.getUid();
                        String token = appConfig.getToken();
                        String sign = MD5Util.getMD5(StringUtil.contact("orderid=", obj.getString("orderid"), "&time=", time,
                                "&token=", token, "&type=", payType, "&uid=", uid, "&", MallHttpUtil.SALT));
                        String orderParams = StringUtil.contact(
                                "&uid=", uid,
                                "&token=", token,
                                "&time=", time,
                                "&sign=", sign,
                                "&orderid=", obj.getString("orderid"),
                                "&type=", payType);
                        switch (id) {
                            case Constants.PAY_TYPE_ALI://支付宝支付
                                aliPay(obj.getString("orderid"),money, name, orderParams);
                                break;
                            case Constants.PAY_TYPE_WX://微信支付
                                wxPay(obj.getString("appid"),obj.getString("noncestr"),obj.getString("package")
                                        ,obj.getString("partnerid"),obj.getString("prepayid"),obj.getString("sign"),obj.getString("timestamp"));
                                break;
                        }
                    }
                } else {
                    ToastUtil.show(msg);
                }
            }

            @Override
            public void onError() {
                if (payDialog != null) {
                    payDialog.dismiss();
                }
            }
        });
    }



    /**
     * 认证支付第一步
     */
    private void balanceRzPay(final String payType, final String id) {
        MallHttpUtil.rzPayOrder(orderid, payType, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (payDialog != null) {
                    payDialog.dismiss();
                }
                if (code == 0) {
                    if(payType.equals("3")||payType.equals("4")){
                        ToastUtil.show("支付成功");
                        buttonType = ACTION_YY;
                        requestCameraPerm();
                    }else{
                        JSONObject obj = JSON.parseObject(info[0]);
                        String time = String.valueOf(System.currentTimeMillis() / 1000);
                        CommonAppConfig appConfig = CommonAppConfig.getInstance();
                        String uid = appConfig.getUid();
                        String token = appConfig.getToken();
                        String sign = MD5Util.getMD5(StringUtil.contact("orderid=", obj.getString("orderid"), "&time=", time,
                                "&token=", token, "&type=", payType, "&uid=", uid, "&", MallHttpUtil.SALT));
                        String orderParams = StringUtil.contact(
                                "&uid=", uid,
                                "&token=", token,
                                "&time=", time,
                                "&sign=", sign,
                                "&orderid=", obj.getString("orderid"),
                                "&type=", payType);
                        switch (id) {
                            case Constants.PAY_TYPE_ALI://支付宝支付
                                aliPay(obj.getString("orderid"),money, name, orderParams);
                                break;
                            case Constants.PAY_TYPE_WX://微信支付
                                wxPay(obj.getString("appid"),obj.getString("noncestr"),obj.getString("package")
                                        ,obj.getString("partnerid"),obj.getString("prepayid"),obj.getString("sign"),obj.getString("timestamp"));
                                break;
                        }
                    }
                } else {
                    ToastUtil.show(msg);
                }
            }

            @Override
            public void onError() {
                if (payDialog != null) {
                    payDialog.dismiss();
                }
            }
        });
    }



    private void requestCameraPerm() {
        if (android.os.Build.VERSION.SDK_INT >= M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                //进行权限请求
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                requestWriteExternalPerm();
            }
        } else {
            beginDetect(buttonType);
        }
    }


    private void requestWriteExternalPerm() {
        if (android.os.Build.VERSION.SDK_INT >= M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //进行权限请求
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        EXTERNAL_STORAGE_REQ_WRITE_EXTERNAL_STORAGE_CODE);
            } else {
                beginDetect(buttonType);
            }
        } else {
            beginDetect(buttonType);
        }
    }

    private void beginDetect(int type) {
        if (type == ACTION_YY) {
            getBizToken("meglive", 1, realName, cerNo, "aaa", null);
        }
//        else if (type == ACTION_WY) {
//            getBizToken("meglive", 0, "", "", UUID.randomUUID().toString(), imageRef);
//        } else if (type == FMP_YY) {
//            getBizToken("still", 1, "XXX", "xxxxxxxxxxxxxxxx", "", null);
//        } else if (type == FMP_WY) {
//            getBizToken("still", 0, "", "", UUID.randomUUID().toString(), imageRef);
//        }
    }


    private void getBizToken(String livenessType, int comparisonType, String idcardName, String idcardNum, String uuid, byte[] image) {
        mProgressDialog.show();
        HttpRequestManager.getInstance().getBizToken(this, GET_BIZTOKEN_URL, sign, SIGN_VERSION, livenessType, comparisonType, idcardName, idcardNum, uuid, image, new HttpRequestCallBack() {

            @Override
            public void onSuccess(String responseBody) {
                try {
                    org.json.JSONObject json = new org.json.JSONObject(responseBody);
                    String bizToken = json.optString("biz_token");
                    megLiveManager.preDetect(PayActivity.this, bizToken,"zh","https://api.megvii.com", PayActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {
                Log.w("onFailure","statusCode="+statusCode+",responseBody"+new String(responseBody));
            }
        });
    }


    /**
     * 微信支付
     */
    private void wxPay(String appid,String noncestr,String packages,String partnerid,String prepayid,String sign,String timestamp) {

        if (TextUtils.isEmpty(Constants.MALL_PAY_GOODS_ORDER)) {
            return;
        }
        if (!CommonAppConfig.isAppExist(Constants.PACKAGE_NAME_WX)) {
            ToastUtil.show(com.yunbao.common.R.string.coin_wx_not_install);
            return;
        }
        if (TextUtils.isEmpty(appid)) {
            ToastUtil.show(Constants.PAY_WX_NOT_ENABLE);
            return;
        }
        WxPayBuilder builder = new WxPayBuilder(this,appid);
        builder.setPayCallback(mPayCallback);
        builder.payHasOrderId(appid,partnerid,prepayid,packages,noncestr,timestamp,sign);
    }


    /**
     * 支付宝支付
     */
    private void aliPay(String orId,String money, String goodsName, String orderParams) {
        if (TextUtils.isEmpty(Constants.MALL_PAY_GOODS_ORDER)|| TextUtils.isEmpty(HtmlConfig.ALI_PAY_MALL_ORDER)) {
            return;
        }
        if (!CommonAppConfig.isAppExist(Constants.PACKAGE_NAME_ALI)) {
            ToastUtil.show(com.yunbao.common.R.string.coin_ali_not_install);
            return;
        }
        if (TextUtils.isEmpty(aliapp_partner) || TextUtils.isEmpty(aliapp_seller_id) || TextUtils.isEmpty(aliapp_key_android)) {
            ToastUtil.show(Constants.PAY_ALI_NOT_ENABLE);
            return;
        }
        AliPayBuilder builder = new AliPayBuilder(this, aliapp_partner, aliapp_seller_id, aliapp_key_android,orId);
        builder.setMoney(money);
        builder.setGoodsName(goodsName);
        builder.setCallbackUrl(HtmlConfig.ALI_PAY_MALL_ORDER);
        builder.setOrderParams(StringUtil.contact(Constants.MALL_PAY_GOODS_ORDER, orderParams));
        builder.setPayCallback(mPayCallback);
        builder.pay();
    }



    public void initSelectImg(){
        for(int i =0;i<list.size();i++){
            list.get(i).setChecked(false);
        }
    }

    @Override
    public void myClick(int position, int type) {
        if(type == 1){
            initSelectImg();
            list.get(position).setChecked(true);
            adapter.notifyDataSetChanged();
        }
    }

    private void verify(String token, byte[] data) {
        showDialogDismiss();
        HttpRequestManager.getInstance().verify(this, VERIFY_URL, sign, SIGN_VERSION, token, data, new HttpRequestCallBack() {
            @Override
            public void onSuccess(String responseBody) {
                progressDialogDismiss();
                JSONObject obj = JSON.parseObject(responseBody);
                if(obj.getString("result_code").equals("1000")){
                    //识别成功
                    isRzStatus();
                }else{
                    ToastUtil.show("识别失败");
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {
                progressDialogDismiss();
                ToastUtil.show("识别过程异常");
            }
        });
    }


    /**
     * 更改认证状态
     */
    public void isRzStatus(){

        OkHttpClient client = new OkHttpClient();

        Request request = new  Request.Builder()
                .get()
                .url("http://cz56.yczbfx.com/appapi/auth/authsave/?uid="+ CommonAppConfig.getInstance().getUid()+"&token="+CommonAppConfig.getInstance().getToken()+"&real_name="+
                        realName+"&cer_no="+cerNo
                        )
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
                        //更改认证状态成功
                        EventBus.getDefault().post(new ImUnReadCountEvent("getUserInfo"));
                        setResult(333);
                        finish();
                    }else {
                        ToastUtil.show(data.getString("msg"));
                    }
                }
            }

        });


    }

    private void progressDialogDismiss() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    private void showDialogDismiss(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog!=null){
                    mProgressDialog.show();
                }
            }
        });
    }

    @Override
    public void onDetectFinish(String token, int errorCode, String s1, String data) {
        if (errorCode == 1000) {
            verify(token, data.getBytes());
        }
    }

    @Override
    public void onPreStart() {
        showDialogDismiss();
    }

    @Override
    public void onPreFinish(String s, int errorCode, String s1) {
        progressDialogDismiss();
        if (errorCode == 1000) {
            megLiveManager.setVerticalDetectionType(MegLiveManager.DETECT_VERITICAL_FRONT);
            megLiveManager.startDetect(this);
        }
    }
}
