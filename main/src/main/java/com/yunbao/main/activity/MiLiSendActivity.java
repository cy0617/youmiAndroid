package com.yunbao.main.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.views.TiShiDialog;
import com.yunbao.mall.http.MallHttpUtil;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

public class MiLiSendActivity extends AbsActivity implements View.OnClickListener {
    private String mMoney;
    private String mCode;
    private EditText et_money;
    private TextView tv_score;
    private Intent intent;
    private String ky_score;
    private TextView et_address;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mi_li_send;
    }

    @Override
    protected void main() {
        setTitle("发送");

        et_money = findViewById(R.id.et_money);
        tv_score = findViewById(R.id.tv_score);
        et_address = findViewById(R.id.et_address);
        intent = getIntent();
        ky_score = intent.getStringExtra("ky_score");
        tv_score.setText(ky_score);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.iv_saoyisao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MiLiSendActivity.this, CaptureActivity.class);
                /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
                 * 也可以不传这个参数
                 * 不传的话  默认都为默认不震动  其他都为true
                 * */

                //ZxingConfig config = new ZxingConfig();
                //config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
                //config.setPlayBeep(true);//是否播放提示音
                //config.setShake(true);//是否震动
                //config.setShowAlbum(true);//是否显示相册
                //config.setShowFlashLight(true);//是否显示闪光灯
                //intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit) {
            mMoney = et_money.getText().toString();
            char c = mMoney.charAt(0);
            String s = String.valueOf(c);
            if (s.equals("0")){
                ToastUtil.show("请输入正确的金额");
            }else {
                sendDialog();
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                et_address.setText(content);
            }
        }
    }

    private void sendDialog() {
        TiShiDialog tiShiDialog = new TiShiDialog(mContext) {
            @Override
            public void ok() {
                super.ok();
                dismiss();
                sendMoney();
            }
        };
        tiShiDialog.setTitleStr("提示");
        tiShiDialog.setDescStr("是否确认发送米粒给对方？");
        tiShiDialog.show();
    }

    private void sendMoney() {

        mMoney = et_money.getText().toString();
        mCode = et_address.getText().toString();
        MallHttpUtil.getSendkycode(mMoney, mCode, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    et_money.setText("");
                    et_address.setText("");
                    ToastUtil.show(msg);
                    Double aDouble1 = Double.valueOf(ky_score);
                    Double aDouble = Double.valueOf(mMoney);
                    double v = aDouble1 - aDouble;
                    String s = String.valueOf(v);
                    tv_score.setText(s);
                    intent.putExtra("ky_score", s);
                    setResult(101, intent);
                }
            }
        });
    }
}
