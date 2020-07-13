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

public class MiLiLockActivity extends AbsActivity implements View.OnClickListener {

    private String mMoney;
    private EditText et_money;
    private TextView tv_score;
    private Intent intent;
    private String ky_score;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mi_li_lock;
    }

    @Override
    protected void main() {

        setTitle("锁仓");
        et_money = findViewById(R.id.et_money);
        tv_score = findViewById(R.id.tv_score);
        intent = getIntent();
        ky_score = intent.getStringExtra("ky_score");
        tv_score.setText(ky_score);
        findViewById(R.id.btn_submit).setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit) {
            mMoney = et_money.getText().toString();

            if (mMoney.equals("")&&mMoney==null) {
                ToastUtil.show("请输入金额");
            } else {
                char c = mMoney.charAt(0);
                String s = String.valueOf(c);
                if (s.equals("0")) {
                    ToastUtil.show("请输入正确的金额");
                } else {
                    suocangDialog();
                }
            }
        }
    }

    private void suocangDialog() {

        TiShiDialog tiShiDialog = new TiShiDialog(mContext) {
            @Override
            public void ok() {
                super.ok();
                dismiss();
                getData();
            }
        };
        tiShiDialog.setTitleStr("提示");
        tiShiDialog.setDescStr("是否确认进行锁仓？");
        tiShiDialog.show();
    }

    private void getData() {

        MallHttpUtil.getSendscscore(mMoney, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    et_money.setText("");
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
