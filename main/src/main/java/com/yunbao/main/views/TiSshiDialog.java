package com.yunbao.main.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yunbao.main.R;


/**
 * @description 提示对话框
 */
public class TiSshiDialog extends Dialog {

    private Context context;
    private TextView tv_desc;
    private TextView tv_title;


    public TiSshiDialog(Context context) {
        super(context, R.style.custom_dialog);
        this.context = context;
        init();
    }

    public void init() {
        //指定布局
        setContentView(R.layout.item_tishi_dialog);
        //点击外部不消失
        setCancelable(false);
        TextView tv_close = findViewById(R.id.tv_close);
        TextView tv_yes = findViewById(R.id.tv_yes);
        tv_desc = findViewById(R.id.tv_desc);
        tv_title = findViewById(R.id.tv_title);

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
                //确认
                ok();
            }
        });

    }

    public void setTitleStr(String title){
        tv_title.setText(title);
    }

    public void setDescStr(String desc){
        tv_desc.setText(desc);
    }


    public void ok(){}

}
