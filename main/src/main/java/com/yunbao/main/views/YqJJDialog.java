package com.yunbao.main.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yunbao.main.R;
import com.yunbao.main.utils.StringUtil;


/**
 * @description 我要晋级对话框
 */
public class YqJJDialog extends Dialog {

    private Context context;

    public YqJJDialog(Context context) {
        super(context, R.style.custom_dialog);
        this.context = context;
        init();
    }

    public void init() {
        //指定布局
        setContentView(R.layout.item_jj_dialog);
        //点击外部不消失
        setCancelable(false);
        TextView tv_close = findViewById(R.id.tv_close);
        TextView tv_yes = findViewById(R.id.tv_yes);


        //设置取消按钮的点击事件
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                cancel();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                ok();
            }
        });

    }



    public void ok(){}

}
