package com.yunbao.main.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.megvii.meglive_sdk.listener.DetectCallback;
import com.megvii.meglive_sdk.listener.PreCallback;
import com.megvii.meglive_sdk.manager.MegLiveManager;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.im.event.ImUnReadCountEvent;
import com.yunbao.main.R;
import com.yunbao.main.facerecognition.GenerateSign;
import com.yunbao.main.facerecognition.HttpRequestCallBack;
import com.yunbao.main.facerecognition.HttpRequestManager;
import com.yunbao.main.utils.StringUtil;
import com.yunbao.mall.http.MallHttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.os.Build.VERSION_CODES.M;

public class MyUserRenZhenAct extends AbsActivity implements DetectCallback, PreCallback {


    private ImageView btn_back;
    private EditText edit_name;
    private EditText edit_sfz_num;
    private TextView btn_xiayibu;
    private Dialog mDialog;
    private Dialog subDialog;
    private String payMoney = "";

    private String isZf = "10";


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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_user_ren_zhen;
    }


    @Override
    protected void main() {
        mDialog = DialogUitl.loadingDialog(mContext, "加载中...");
        subDialog = DialogUitl.loadingDialog(mContext, "提交中...");
        btn_back = findViewById(R.id.btn_back);
        edit_name = findViewById(R.id.edit_name);
        edit_sfz_num = findViewById(R.id.edit_sfz_num);
        btn_xiayibu = findViewById(R.id.btn_xiayibu);





        megLiveManager = MegLiveManager.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        long currtTime = System.currentTimeMillis() / 1000;
        long expireTime = (System.currentTimeMillis() + 60 * 60 * 100) / 1000;
        sign = GenerateSign.appSign(API_KEY, SECRET, currtTime, expireTime);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_xiayibu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCerNoExist();
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
    public void submitOrder() {

        if (StringUtil.isEmpty(payMoney)) {
            ToastUtil.show("金额异常");
            return;
        }

        MallHttpUtil.getSubmitRzOrder(payMoney, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (subDialog != null) {
                    subDialog.dismiss();
                }
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    Intent intent = new Intent(MyUserRenZhenAct.this, PayActivity.class);
                    intent.putExtra("orderid", obj.getString("orderid"));
                    intent.putExtra("money", payMoney);
                    intent.putExtra("name", "");
                    intent.putExtra("realName", edit_name.getText().toString());
                    intent.putExtra("cerNo", edit_sfz_num.getText().toString());
                    intent.putExtra("type", "2");
                    startActivityForResult(intent, 101);
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
     * 检测身份证是否存在
     */
    public void isCerNoExist() {

        if (StringUtil.isEmpty(edit_sfz_num.getText().toString())) {
            ToastUtil.show("请填写身份信息");
            return;
        }

        MallHttpUtil.getisCerNoExist(edit_sfz_num.getText().toString(), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (subDialog != null) {
                    subDialog.dismiss();
                }
                if (code == 0) {
                    //需要认证
                    if (isZf.equals("10")) {
                        buttonType = ACTION_YY;
                        requestCameraPerm();
                    } else {
                        if (subDialog != null) {
                            subDialog.show();
                        }
                        submitOrder();
                    }
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
    public void isRz() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url("http://cz56.yczbfx.com/appapi/?service=Youmio.Getuserauth&uid=" + CommonAppConfig.getInstance().getUid() + "&token=" + CommonAppConfig.getInstance().getToken())
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
                if (response.isSuccessful()) {
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    String string = response.body().string();
                    JSONObject obj = JSON.parseObject(string);
                    JSONObject data = obj.getJSONObject("data");
                    if (data.getString("code").equals("0")) {
                        //未认证成功
                        isZf = data.getString("zf");
                        if (btn_xiayibu != null) {
                            btn_xiayibu.post(new Runnable() {
                                @Override
                                public void run() {
                                    btn_xiayibu.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                        payMoney = data.getString("info");
                    } else if (data.getString("code").equals("1")) {
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
                                    edit_name.setEnabled(false);
                                    edit_name.setFocusableInTouchMode(false);

                                }
                            });
                        }

                        if (edit_sfz_num != null) {
                            edit_sfz_num.post(new Runnable() {
                                @Override
                                public void run() {
                                    edit_sfz_num.setText(info.getString("cer_no"));
                                    edit_sfz_num.setEnabled(false);
                                    edit_sfz_num.setFocusableInTouchMode(false);
                                }
                            });
                        }
                    } else {
                        ToastUtil.show(data.getString("msg"));
                    }
                }
            }

        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == 333) {
                if (mDialog != null) {
                    mDialog.show();
                }
                isRz();
            }
        }
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
            getBizToken("meglive", 1, edit_name.getText().toString(), edit_sfz_num.getText().toString(), "aaa", null);
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
                    megLiveManager.preDetect(MyUserRenZhenAct.this, bizToken, "zh", "https://api.megvii.com", MyUserRenZhenAct.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialogDismiss();
                    ToastUtil.show("初始化异常");
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {
                progressDialogDismiss();
                ToastUtil.show("请填写正确的身份信息");
                Log.w("onFailure", "statusCode=" + statusCode + ",responseBody" + new String(responseBody));
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

    private void showDialogDismiss() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null) {
                    mProgressDialog.show();
                }
            }
        });
    }


    @Override
    public void onDetectFinish(String token, int errorCode, String s1, String data) {
        if (errorCode == 1000) {
            verify(token, data.getBytes());
        } else {
            ToastUtil.show("识别失败");
        }
    }

    private void verify(String token, byte[] data) {
        showDialogDismiss();
        HttpRequestManager.getInstance().verify(this, VERIFY_URL, sign, SIGN_VERSION, token, data, new HttpRequestCallBack() {
            @Override
            public void onSuccess(String responseBody) {
                progressDialogDismiss();
                JSONObject obj = JSON.parseObject(responseBody);
                if (obj.getString("result_code").equals("1000")) {
                    //识别成功
                    JSONObject verification = obj.getJSONObject("verification");
                    JSONObject idcard = verification.getJSONObject("idcard");
                    double confidence = idcard.getDouble("confidence");
                    if (confidence >= 80) {
                        isRzStatus();
                    } else {
                        ToastUtil.show("识别失败,请按照提示动作重新识别");
                    }
                } else {
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
    public void isRzStatus() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .get()
                .url("http://cz56.yczbfx.com/appapi/auth/authsave/?uid=" + CommonAppConfig.getInstance().getUid() + "&token=" + CommonAppConfig.getInstance().getToken() + "&real_name=" +
                        edit_name.getText().toString() + "&cer_no=" + edit_sfz_num.getText().toString()
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
                if (response.isSuccessful()) {
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    String string = response.body().string();
                    JSONObject obj = JSON.parseObject(string);
                    JSONObject data = obj.getJSONObject("data");
                    if (data.getString("code").equals("0")) {
                        //更改认证状态成功
                        EventBus.getDefault().post(new ImUnReadCountEvent("getUserInfo"));
                        if (btn_xiayibu != null) {
                            btn_xiayibu.post(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show("认证成功");
                                    btn_xiayibu.setVisibility(View.GONE);
                                }
                            });
                        }
                    } else {
                        ToastUtil.show(data.getString("msg"));
                    }
                }
            }
        });
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
