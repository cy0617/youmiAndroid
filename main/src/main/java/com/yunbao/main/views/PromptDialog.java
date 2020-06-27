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
 * @description 提示对话框
 */
public class PromptDialog extends Dialog {

    private Context context;
    private String level;


    public PromptDialog(Context context,String level) {
        super(context, R.style.custom_dialog);
        this.context = context;
        this.level = level;
        init();
    }

    public void init() {
        //指定布局
        setContentView(R.layout.item_prompt_dialog);
        //点击外部不消失
        setCancelable(false);
        TextView tv_close = findViewById(R.id.tv_close);
        TextView tv_yes = findViewById(R.id.tv_yes);
        TextView tv_desc = findViewById(R.id.tv_desc);

        tv_desc.setText("你需要购买"+level+"级商品包，才可以继续接收"+level+"层个人网红打赏，否则你的打赏将跳至其他网红。");

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


    public void ok(){}

}
