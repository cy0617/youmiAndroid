package com.yunbao.main.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.main.R;


/**
 * @description 绑定手机对话框
 */
public class BdPhoneDialog extends Dialog {

    private Context context;

    public BdPhoneDialog(Context context) {
        super(context, R.style.custom_dialog);
        this.context = context;
        init();
    }

    public void init() {
        //指定布局
        setContentView(R.layout.item_bdphone_dialog);
        //点击外部不消失
        setCancelable(false);
        ImageView iv_close = findViewById(R.id.iv_close);
        TextView tv_bd = findViewById(R.id.tv_bd);


        //设置取消按钮的点击事件
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                cancel();
            }
        });
        tv_bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                ok();
            }
        });

    }


    public void ok(){}

}
