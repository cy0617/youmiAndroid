package com.yunbao.main.activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.ImageResultCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.ProcessImageUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.main.utils.StringUtil;
import com.yunbao.main.views.MaxImgDialog;
import com.yunbao.main.views.TiShiDialog;
import com.yunbao.mall.http.MallHttpUtil;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class RewardWhActivity extends AbsActivity {

    private ImageView btn_back, iv_wx_sk_code, iv_zfb_sk_code, iv_reward_img, avatar, iv_title_bg,iv_copy;
    private TextView tv_user_name, tv_id, tv_wx_code, tv_phone, tv_hb_money, tv_upload_btn, tv_quxiao, tv_queren, tv_wallet;
    private LinearLayout ll_bottom_btn;
    private ClipboardManager clipboardManager;
    private Dialog mDialog;
    private ProcessImageUtil mImageUtil;
    private String jietuImg = "";
    private String restId = "";
    private String status = "";
    private String wxImgUrl = "";
    private String zfbImgUrl = "";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_reward_wh;
    }


    @Override
    protected void main() {
        mDialog = DialogUitl.loadingDialog(mContext, "加载中...");

        ll_bottom_btn = findViewById(R.id.ll_bottom_btn);
        btn_back = findViewById(R.id.btn_back);
        avatar = findViewById(R.id.avatar);
        iv_title_bg = findViewById(R.id.iv_title_bg);
        tv_quxiao = findViewById(R.id.tv_quxiao);
        tv_queren = findViewById(R.id.tv_queren);
        iv_wx_sk_code = findViewById(R.id.iv_wx_sk_code);
        iv_zfb_sk_code = findViewById(R.id.iv_zfb_sk_code);
        iv_reward_img = findViewById(R.id.iv_reward_img);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_upload_btn = findViewById(R.id.tv_upload_btn);
        tv_id = findViewById(R.id.tv_id);
        tv_wx_code = findViewById(R.id.tv_wx_code);
        tv_phone = findViewById(R.id.tv_phone);
        tv_hb_money = findViewById(R.id.tv_hb_money);
        tv_wallet = findViewById(R.id.tv_wallet);
        iv_copy = findViewById(R.id.iv_copy);
        tv_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消打赏
                showCodeDialog(1, "确认需要取消打赏");
            }
        });
        tv_queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确认打赏
                if (StringUtil.isEmpty(jietuImg)) {
                    ToastUtil.show("请上传打赏截图");
                    return;
                }
                showCodeDialog(2, "确认已经打赏给网红");
            }
        });


        iv_wx_sk_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtil.isEmpty(wxImgUrl)) {
                    showJJDialog(wxImgUrl);
                }
            }
        });
        iv_zfb_sk_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtil.isEmpty(zfbImgUrl)) {
                    showJJDialog(zfbImgUrl);
                }
            }
        });
        iv_reward_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtil.isEmpty(jietuImg)) {
                    showJJDialog(jietuImg);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("0")) {
                    editAvatar();
                }
            }
        });

        mImageUtil = new ProcessImageUtil(this);
        mImageUtil.setImageResultCallback(new ImageResultCallback() {
            @Override
            public void beforeCamera() {

            }

            @Override
            public void onSuccess(File file) {
                if (file != null) {
                    MainHttpUtil.updateImg(file, new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            if (code == 0 && info.length > 0) {
                                JSONObject obj = JSON.parseObject(info[0]);
                                jietuImg = obj.getString("avatar");
                                Glide.with(mContext)
                                        .load(jietuImg)
                                        .error(R.mipmap.default_image)
                                        .placeholder(R.mipmap.default_image)
                                        .into(iv_reward_img);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure() {
            }
        });


        if (mDialog != null) {
            mDialog.show();
        }
        queryData();
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

    private void editAvatar() {
        DialogUitl.showStringArrayDialog(mContext, new Integer[]{
                R.string.camera, R.string.alumb}, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                if (tag == R.string.camera) {
                    mImageUtil.getImageByCamera();
                } else {
                    mImageUtil.getImageByAlumb();
                }
            }
        });
    }


    /**
     * 提示按钮
     */
    private void showCodeDialog(final int type, String desc) {
        TiShiDialog tiShiDialog = new TiShiDialog(mContext) {
            @Override
            public void ok() {
                super.ok();
                dismiss();
                if (type == 1) {
                    //取消打赏
                    showReward(restId, "2", jietuImg);
                } else if (type == 2) {
                    //确认已打赏
                    showReward(restId, "1", jietuImg);
                }
            }
        };
        tiShiDialog.setTitleStr("提示");
        tiShiDialog.setDescStr(desc);
        tiShiDialog.show();
    }


    public void queryData() {
        MallHttpUtil.getUserSjInfo(new HttpCallback() {

            private ClipData clip;
            private String wallet_address;

            @Override
            public void onSuccess(int code, final String msg, String[] info) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if ((code == 0 || code == 22) && info.length > 0) {

                    JSONObject obj = JSON.parseObject(info[0]);
                    restId = obj.getString("id");
                    tv_user_name.setText(obj.getString("user_nicename"));
                    tv_id.setText(obj.getString("pid"));
                    tv_wx_code.setText(obj.getString("weixin"));
                    tv_phone.setText(obj.getString("mobile"));
                    tv_hb_money.setText(obj.getString("money"));
                    tv_wallet.setText(obj.getString("qmoney_code"));
                    wxImgUrl = obj.getString("weixin_img");
                    zfbImgUrl = obj.getString("zfb_img");


                    Glide.with(mContext)
                            .load(obj.getString("weixin_img"))
                            .error(R.mipmap.default_image)
                            .placeholder(R.mipmap.default_image)
                            .into(iv_wx_sk_code);
                    Glide.with(mContext)
                            .load(obj.getString("zfb_img"))
                            .error(R.mipmap.default_image)
                            .placeholder(R.mipmap.default_image)
                            .into(iv_zfb_sk_code);
                    Glide.with(mContext)
                            .load(obj.getString("avatar"))
                            .error(R.mipmap.default_image)
                            .placeholder(R.mipmap.default_image)
                            .into(avatar);
                    Glide.with(mContext)
                            .load(obj.getString("avatar"))
                            .error(R.mipmap.default_image)
                            .placeholder(R.mipmap.default_image)
                            .into(iv_title_bg);

                    status = obj.getString("status");


                    if (obj.getString("status").equals("0")) {
                        tv_upload_btn.setText("上传打赏截图");
                        ll_bottom_btn.setVisibility(View.VISIBLE);
                    } else if (obj.getString("status").equals("1")) {
                        tv_upload_btn.setText("等待网红审核");
                        ll_bottom_btn.setVisibility(View.GONE);
                        jietuImg = obj.getString("img");
                        Glide.with(mContext)
                                .load(obj.getString("img"))
                                .error(R.mipmap.default_image)
                                .placeholder(R.mipmap.default_image)
                                .into(iv_reward_img);
                    } else {
                        tv_upload_btn.setText("审核成功");
                        ll_bottom_btn.setVisibility(View.GONE);
                        Glide.with(mContext)
                                .load(obj.getString("img"))
                                .error(R.mipmap.default_image)
                                .placeholder(R.mipmap.default_image)
                                .into(iv_reward_img);
                    }
                } else {
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            finish();
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, 2000);//3秒后执行TimeTask的run方法
                    ToastUtil.show(msg);
                }
                iv_copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        wallet_address = tv_wallet.getText().toString();
                        clip = ClipData.newPlainText("text", wallet_address);
                        clipboardManager.setPrimaryClip(clip);
                        ToastUtil.show(wallet_address+"已复制");
                    }
                });
            }

            @Override
            public void onError() {
                if (mDialog != null) {
                    mDialog.dismiss();
                }

                finish();
            }
        });
    }


    /**
     * 用户打赏
     */
    public void showReward(String id, final String type, String img) {
        MallHttpUtil.getRewardStart(id, type, img, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if (code == 0) {
                    if (type.equals("1")) {
                        queryData();
                    } else if (type.equals("2")) {
                        //取消打赏
                        finish();
                    }
                } else {
                    ToastUtil.show(msg);
                    finish();
                }
            }

            @Override
            public void onError() {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                finish();
            }
        });
    }


}
