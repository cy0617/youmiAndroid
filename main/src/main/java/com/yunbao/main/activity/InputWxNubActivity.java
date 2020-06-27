package com.yunbao.main.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.activity.AbsActivity;
import com.yunbao.main.R;
import com.yunbao.main.utils.StringUtil;

public class InputWxNubActivity extends AbsActivity {


    private ImageView btn_back;
    private TextView btn_register;
    private EditText edit_wx_code;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_input_wx_nub;
    }


    @Override
    protected void main() {

        btn_back = findViewById(R.id.btn_back);
        btn_register = findViewById(R.id.btn_register);
        edit_wx_code = findViewById(R.id.edit_wx_code);



        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtil.isEmpty(edit_wx_code.getText().toString())){
                    showToast("请输入微信号");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("wxCode",edit_wx_code.getText().toString());
                setResult(5001,intent);
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
