package com.yunbao.main.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meihu.glide.Glide;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;

public class ReceptionActivity extends AbsActivity implements View.OnClickListener {


    private TextView tv_address;
    private ImageView iv_shoukuanma;
    private String qmoney_code;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reception;
    }

    @Override
    protected void main() {
        setTitle("接收");
        tv_address = findViewById(R.id.tv_address);
        iv_shoukuanma = findViewById(R.id.iv_shoukuanma);

        findViewById(R.id.btn_submit).setOnClickListener(this);

        qmoney_code = CommonAppConfig.getInstance().getUserBean().getQian_code();
        String qr = CommonAppConfig.getInstance().getUserBean().getQr();
        Glide.with(mContext).load(qr).into(iv_shoukuanma);
        tv_address.setText(qmoney_code);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit) {
            ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(tv_address.getText().toString().trim());
            ToastUtil.show("复制成功");
        }
    }
}
