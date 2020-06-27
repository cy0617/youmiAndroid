package com.yunbao.main.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.google.gson.jpush.Gson;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.event.UpdateFieldEvent;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.ImageResultCallback;
import com.yunbao.common.upload.UploadBean;
import com.yunbao.common.upload.UploadCallback;
import com.yunbao.common.upload.UploadQnImpl;
import com.yunbao.common.upload.UploadStrategy;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.ProcessImageUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.im.event.ImUnReadCountEvent;
import com.yunbao.main.R;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.main.utils.ImageBitmapUtil;
import com.yunbao.main.utils.StringUtil;
import com.yunbao.mall.http.MallHttpUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 个人资料
 */
public class UserInfoActivity extends AbsActivity implements View.OnClickListener{

    private ImageView btn_back;
    private ImageView iv_wx_img;
    private ImageView iv_zfb_img;
    private TextView tv_phone;
    private TextView tv_wx_code;
    private TextView tv_wx_img;
    private TextView tv_zfb_img;
    private Dialog mDialog;

    private UserBean userBean;
    private ProcessImageUtil mImageUtil;
    private int clickType = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }


    @Override
    protected void main() {
        mDialog = DialogUitl.loadingDialog(mContext, "提交中...");

        btn_back = findViewById(R.id.btn_back);
        iv_wx_img = findViewById(R.id.iv_wx_img);
        iv_zfb_img = findViewById(R.id.iv_zfb_img);
        tv_phone = findViewById(R.id.tv_phone);
        tv_wx_code = findViewById(R.id.tv_wx_code);
        tv_wx_img = findViewById(R.id.tv_wx_img);
        tv_zfb_img = findViewById(R.id.tv_zfb_img);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.ll_phone).setOnClickListener(this);
        findViewById(R.id.ll_wx_num).setOnClickListener(this);
        findViewById(R.id.rl_wx_img).setOnClickListener(this);
        findViewById(R.id.rl_zfb_img).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);

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
                                    if(clickType ==1){
                                        userBean.setWeixin_img(obj.getString("avatar"));
                                        Glide.with(mContext)
                                                .load(obj.getString("avatar"))
                                                .error(R.mipmap.default_image)
                                                .placeholder( R.mipmap.default_image)
                                                .into(iv_wx_img);
                                    }else if(clickType ==2){
                                        userBean.setZfb_img(obj.getString("avatar"));
                                        Glide.with(mContext)
                                                .load(obj.getString("avatar"))
                                                .error(R.mipmap.default_image)
                                                .placeholder( R.mipmap.default_image)
                                                .into(iv_zfb_img);
                                    }

//                                    bean.setAvatar(obj.getString("avatar"));
//                                    bean.setAvatarThumb(obj.getString("avatarThumb"));
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure() {
            }
        });


        userBean = CommonAppConfig.getInstance().getUserBean();
            if(userBean!=null){
                if(StringUtil.isEmpty(userBean.getMobile())){
                    //未绑定手机
                    tv_phone.setText("未绑定");
                }else{
                    tv_phone.setText(userBean.getMobile());
                }

                if(StringUtil.isEmpty(userBean.getWeixin())){
                    //未绑定微信名称
                    tv_wx_code.setText("未绑定");
                }else{
                    tv_wx_code.setText(userBean.getWeixin());
                }

                if(StringUtil.isEmpty(userBean.getWeixin_img())){
                    //未上传微信收款码
                    tv_wx_img.setVisibility(View.VISIBLE);
                    iv_wx_img.setVisibility(View.GONE);
                }else{
                    tv_wx_img.setVisibility(View.GONE);
                    iv_wx_img.setVisibility(View.VISIBLE);
                    Glide.with(mContext)
                            .load(userBean.getWeixin_img())
                            .error(R.mipmap.default_image)
                            .placeholder( R.mipmap.default_image)
                            .into(iv_wx_img);
                }
                if(StringUtil.isEmpty(userBean.getZfb_img())){
                    //未上传支付宝收款码
                    tv_zfb_img.setVisibility(View.VISIBLE);
                    iv_zfb_img.setVisibility(View.GONE);
                }else{
                    tv_zfb_img.setVisibility(View.GONE);
                    iv_zfb_img.setVisibility(View.VISIBLE);
                    Glide.with(mContext)
                            .load(userBean.getZfb_img())
                            .error(R.mipmap.default_image)
                            .placeholder( R.mipmap.default_image)
                            .into(iv_zfb_img);
                }
            }




    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ll_phone){
            //手机号
            if(StringUtil.isEmpty(userBean.getMobile())){
                //未绑定手机
                Intent intent = new Intent(mContext, BdPhoneActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(mContext, UpdatePhoneActivity.class);
                startActivity(intent);
            }
        }else if(view.getId() == R.id.ll_wx_num){
            //修改微信号
            Intent intent = new Intent(mContext, InputWxNubActivity.class);
            startActivityForResult(intent,102);
        }else if(view.getId() == R.id.rl_wx_img){
            //上传微信收款码
            clickType = 1;
            editAvatar();
        }else if(view.getId() == R.id.rl_zfb_img){
            //上传支付宝收款码
            clickType = 2;
            editAvatar();
        }else if(view.getId() == R.id.btn_submit){
            //提交
            submit();
        }
    }


    /**
     * 提交
     */
    private void submit() {
        if (mDialog != null) {
            mDialog.show();
        }
        MainHttpUtil.submitUinfo(tv_wx_code.getText().toString(), userBean.getWeixin_img(),userBean.getZfb_img() , new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    ToastUtil.show(JSON.parseObject(info[0]).getString("msg"));
                    EventBus.getDefault().post(new ImUnReadCountEvent("getUserInfo"));
                    finish();
                } else {
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
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




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 102){
            if(resultCode == 5001){
                //输入微信号
                tv_wx_code.setText(data.getStringExtra("wxCode"));
            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
