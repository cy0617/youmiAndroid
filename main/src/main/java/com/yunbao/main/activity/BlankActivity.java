package com.yunbao.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.SpUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.main.views.PromptDialog;
import com.yunbao.main.views.YqJJDialog;
import com.yunbao.mall.http.MallHttpUtil;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 我的-菜单
 */
public class BlankActivity extends AbsActivity implements View.OnClickListener {

    private ImageView btn_back;
    private ImageView mAvatar;
    private TextView tv_wh_leve;
    private TextView tv_dl_leve;
    private TextView tv_contribution;
    private TextView tv_area;
    private TextView tv_bounty;
    private TextView tv_mili;
    private TextView tv_up_mili;
    private TextView tv_wh_id;
    private UserBean userBean;
    private TextView tv_guanyun;
    private ImageView iv_qiandao;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_me_blank;
    }


    @Override
    protected void main() {

        /**
         * 用户是否需要购买商品包
         */
        queryUserLeve();


        btn_back = findViewById(R.id.btn_back);
        mAvatar = (ImageView) findViewById(R.id.avatar);
        tv_wh_leve = findViewById(R.id.tv_wh_leve);
        tv_dl_leve = findViewById(R.id.tv_dl_leve);
        tv_contribution = findViewById(R.id.tv_contribution);
        tv_area = findViewById(R.id.tv_area);
        tv_wh_id = findViewById(R.id.tv_wh_id);
        tv_bounty = findViewById(R.id.tv_bounty);
        tv_mili = findViewById(R.id.tv_mili);
        tv_up_mili = findViewById(R.id.tv_up_mili);
        tv_guanyun = findViewById(R.id.tv_guanyun);
        findViewById(R.id.ll_shangjin).setOnClickListener(this);
        findViewById(R.id.ll_personal_information).setOnClickListener(this);
        findViewById(R.id.ll_advance).setOnClickListener(this);
        findViewById(R.id.ll_reward_promotion).setOnClickListener(this);
        findViewById(R.id.ll_buddy).setOnClickListener(this);
        findViewById(R.id.ll_otc).setOnClickListener(this);
        findViewById(R.id.ll_accelerator).setOnClickListener(this);
        findViewById(R.id.ll_suocang).setOnClickListener(this);
        findViewById(R.id.ll_keyongmili).setOnClickListener(this);
        iv_qiandao = findViewById(R.id.iv_qiandao);

        userBean = CommonAppConfig.getInstance().getUserBean();


        Glide.with(mContext)
                .load(userBean.getAvatar())
                .error(R.mipmap.default_image)
                .placeholder(R.mipmap.default_image)
                .into(mAvatar);
        tv_wh_leve.setText(userBean.getGrade());
        tv_contribution.setText(userBean.getGxz());
        tv_bounty.setText(userBean.getShangjin());
        tv_mili.setText(userBean.getKy_score());
        tv_up_mili.setText(userBean.getSc_score());
        tv_wh_id.setText(userBean.getLiangNameTip());
        tv_guanyun.setText(userBean.getGygp());
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_qiandao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainHttpUtil.getDayQianDao(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0) {
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .get()
                                    .url("https://youmi.telemsoft.net/appapi/?service=Youmio.Getuserauth&uid=" + CommonAppConfig.getInstance().getUid() + "&token=" + CommonAppConfig.getInstance().getToken())
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                }

                                @Override
                                public void onResponse(final Call call, Response response) throws IOException {
                                    String string = response.body().string();
                                    JSONObject obj = JSON.parseObject(string);
                                    final JSONObject data = obj.getJSONObject("data");
                                    if (data.getString("code").equals("0")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.show("请先实名认证");
                                            }
                                        });
                                    } else if (data.getString("code").equals("1")) {
                                        OkHttpClient client1 = new OkHttpClient();
                                        Request request1 = new Request.Builder()
                                                .get()
                                                .url("https://youmi.telemsoft.net/appapi/?service=Youmio.Getshipin")
                                                .build();
                                        client1.newCall(request1)
                                                .enqueue(new Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {

                                                    }

                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {
                                                        String string = response.body().string();
                                                        JSONObject obj = JSON.parseObject(string);
                                                        JSONObject data = obj.getJSONObject("data");
                                                        JSONObject date = data.getJSONObject("data");
                                                        Intent intent = new Intent(BlankActivity.this, QianDaoPlayActivity.class);
                                                        intent.putExtra("min", date.getString("min"));
                                                        intent.putExtra("thumb", date.getString("thumb"));
                                                        startActivityForResult(intent, 1);


                                                    }
                                                });

                                    }

                                }
                            });
                        } else {
                            ToastUtil.show(msg);
                        }
                    }
                });
            }
        });
        queryUserLeveTwo();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 100) {
                Bundle extras = data.getExtras();
                String code1 = extras.getString("code");
                String msg1 = extras.getString("msg");
                if (Integer.valueOf(code1) == 0) {
                    ToastUtil.show(msg1);
                }
            } else if (resultCode == 101) {
                String ky_score = data.getStringExtra("ky_score");
                tv_mili.setText(ky_score);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_personal_information) {
            //个人资料
            Intent intent = new Intent(this, UserInfoActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.ll_advance) {
            //我要晋级
            showJJDialog();
        } else if (view.getId() == R.id.ll_reward_promotion) {
            //打赏晋级
            Intent intent = new Intent(BlankActivity.this, RewardForPromotionAct.class);
            startActivity(intent);
        } else if (view.getId() == R.id.ll_buddy) {
            //我的好友
            Intent intent = new Intent(this, MyBuddyActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.ll_otc) {
            //otc
        } else if (view.getId() == R.id.ll_accelerator) {
            //加速器
        } else if (view.getId() == R.id.ll_shangjin) {
            //赏金
            startActivity(new Intent(BlankActivity.this, MyBountyActivity.class));
        } else if (view.getId() == R.id.ll_suocang) {
            //锁仓米粒
            startActivity(new Intent(BlankActivity.this, LockRiceActivity.class));
        } else if (view.getId() == R.id.ll_keyongmili) {
            //可用米粒
//            Intent intent = new Intent(BlankActivity.this, UsableMiLiActivity.class);
//            intent.putExtra("ky_score",tv_mili.getText().toString());
//            startActivityForResult(intent,1);
        }
    }


    /**
     * 判断用户目前需要购买多少级的商品包
     */
    public void queryUserLeveTwo() {
        MallHttpUtil.getCheckyggoodsid(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    //不需要
                } else if (code == 11) {
                    //需要购买权限包

                    JSONObject obj = JSON.parseObject(info[0]);
                    long oneDayTime = new Date().getTime() + 86500000;
                    long savaTime = SpUtil.getInstance().getLongValue("savatimeuser");
                    if (savaTime != 0) {
                        long differenceTime = oneDayTime - savaTime;
                        if (differenceTime > 86400000) {
                            //相差一天
                            SpUtil.getInstance().setLongValue("savatimeuser", oneDayTime);
                            showCodeDialog(obj.getString("level"), obj.getString("id"));
                        }
                    } else {
                        SpUtil.getInstance().setLongValue("savatimeuser", oneDayTime);
                        showCodeDialog(obj.getString("level"), obj.getString("id"));
                    }

                } else {
                    ToastUtil.show(msg);
                }
            }

            @Override
            public void onError() {

            }
        });
    }


    /**
     * 判断用户目前需要购买多少级的商品包
     */
    public void queryUserLeve() {
        MallHttpUtil.getCheckyggoodsid(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    //不需要
                } else if (code == 11) {
                    //需要购买权限包
                    JSONObject obj = JSON.parseObject(info[0]);
                    String level = obj.getString("level");
                    showCodeDialog(obj.getString("level"), obj.getString("id"));
                } else {
                    ToastUtil.show(msg);
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 打赏规则
     */
    private void showCodeDialog(String level, final String id) {
        PromptDialog promptDialog = new PromptDialog(mContext, level) {
            @Override
            public void ok() {
                super.ok();
                dismiss();
                Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
                intent.putExtra("goodsId", id);
                mContext.startActivity(intent);
            }
        };
        promptDialog.show();
    }


    /**
     * 我要晋级弹框
     */
    private void showJJDialog() {
        YqJJDialog yqJJDialog = new YqJJDialog(mContext) {
            @Override
            public void ok() {
                dismiss();
                //跳转
                Intent intent = new Intent(BlankActivity.this, RewardWhActivity.class);
                startActivity(intent);
            }
        };
        yqJJDialog.show();
    }
}
