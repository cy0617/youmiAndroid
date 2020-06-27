package com.yunbao.main.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunbao.main.R;


/**
 * @description 大图对话框
 */
public class MaxImgDialog extends Dialog {

    private Context context;
    private ImageView iv_max_img;

    public MaxImgDialog(Context context) {
        super(context, R.style.custom_dialog);
        this.context = context;
        init();
    }

    public void init() {
        //指定布局
        setContentView(R.layout.item_maximg_dialog);
        //点击外部不消失
        setCancelable(false);
        iv_max_img = findViewById(R.id.iv_max_img);
        LinearLayout ll_item = findViewById(R.id.ll_item);

        ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                ok();
            }
        });
        iv_max_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                ok();
            }
        });
    }

    public void setMaxImg(String imgUrl){
        Glide.with(context)
                .load(imgUrl)
                .error(R.mipmap.default_image)
                .placeholder( R.mipmap.default_image)
                .into(iv_max_img);
    }




    public void ok(){}

}
