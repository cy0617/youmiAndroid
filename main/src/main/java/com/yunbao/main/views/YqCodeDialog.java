package com.yunbao.main.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.utils.StringUtil;


/**
 * @description 邀请码对话框
 */
public class YqCodeDialog extends Dialog {

    private Context context;

    public YqCodeDialog(Context context) {
        super(context, R.style.custom_dialog);
        this.context = context;
        init();
    }

    public void init() {
        //指定布局
        setContentView(R.layout.item_code_dialog);
        //点击外部不消失
        setCancelable(false);
        final EditText et_code = findViewById(R.id.et_code);
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
                if(StringUtil.isEmpty(et_code.getText().toString())){
                    Toast.makeText(context,"请输入邀请码",Toast.LENGTH_LONG).show();
                    return;
                }
                //取消
                ok(et_code.getText().toString());
            }
        });

    }


    public void ok(String code){}

}
