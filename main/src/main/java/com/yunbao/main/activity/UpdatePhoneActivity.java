package com.yunbao.main.activity;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.im.event.ImUnReadCountEvent;
import com.yunbao.main.R;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.main.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;

public class UpdatePhoneActivity extends AbsActivity {

    private ImageView btn_back;
    private TextView mBtnCode;
    private EditText editPhone;
    private EditText mEditCode;
    private static final int TOTAL = 60;
    private int mCount = TOTAL;
    private String mGetCodeAgain;
    private String mGetCode;
    private Dialog mDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mCount--;
            if (mCount > 0) {
                mBtnCode.setText(mGetCodeAgain + "(" + mCount + "s)");
                if (mHandler != null) {
                    mHandler.sendEmptyMessageDelayed(0, 1000);
                }
            } else {
                mBtnCode.setText(mGetCode);
                mCount = TOTAL;
                if (mBtnCode != null) {
                    mBtnCode.setEnabled(true);
                }
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_phone;
    }


    @Override
    protected void main() {
        mGetCode = WordUtil.getString(R.string.reg_get_code);
        mGetCodeAgain = WordUtil.getString(R.string.reg_get_code_again);
        mDialog = DialogUitl.loadingDialog(mContext, "绑定中");

        btn_back =  findViewById(R.id.btn_back);
        mBtnCode = (TextView) findViewById(R.id.btn_code);
        editPhone = (EditText) findViewById(R.id.edit_phone);
        mEditCode = (EditText) findViewById(R.id.edit_code);


        editPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s) && s.length() == 11) {
                    mBtnCode.setEnabled(true);
                } else {
                    mBtnCode.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void bdClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_code) {
            //获取验证码
            getCode();
        }else if(i == R.id.btn_register){
            bdPhone();
        }
    }


    /**
     * 获取验证码
     */

    private void getCode() {
        String phoneNum = editPhone.getText().toString().trim();
        if(StringUtil.isEmpty(phoneNum)){
            showToast("请输入手机号码");
            return;
        }

        mEditCode.requestFocus();
        MainHttpUtil.getBdPhoneCode(phoneNum, mGetCodeCallback);
    }


    /**
     * 绑定新手机号
     */
    private void bdPhone() {
        String phoneNum = editPhone.getText().toString().trim();
        String code = mEditCode.getText().toString().trim();
        if(StringUtil.isEmpty(phoneNum)){
            showToast("请输入手机号码");
            return;
        }
        if(StringUtil.isEmpty(code)){
            showToast("请输入验证码");
            return;
        }
        if (mDialog != null) {
            mDialog.show();
        }
        MainHttpUtil.bdPhone(phoneNum, code , new HttpCallback() {
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




    private HttpCallback mGetCodeCallback = new HttpCallback() {
        @Override
        public void onSuccess(int code, String msg, String[] info) {
            if (code == 0) {
                mBtnCode.setEnabled(false);
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(0);
                }
                if (!TextUtils.isEmpty(msg) && msg.contains("123456")) {
                    ToastUtil.show(msg);
                }
            } else {
                ToastUtil.show(msg);
            }
        }
    };


}
