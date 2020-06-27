package com.yunbao.main.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunbao.main.R;


/**
 * PackageName : com.ewhale.wantong.widgets
 * Author : Ziwen Lan
 * Date : 2017/11/13
 * Time : 10:17
 * Introduction : 数量控件(只能在xml中添加)
 */

public class QuantityView extends LinearLayout {

    public CheckBox cbSub;
    public CheckBox cbAdd;
    public TextView tvCount;
    private int minNum = 1;
    private int maxNum = 200;
    private int num = 1;

    public QuantityView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public QuantityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public QuantityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(final Context context, AttributeSet attrs, int defStyleAttr) {
        setFocusable(true);
        setFocusableInTouchMode(true);
        LayoutInflater.from(context).inflate(R.layout.widget_quantity_view, this);
        cbSub = findViewById(R.id.cb_sub);
        cbAdd = findViewById(R.id.cb_add);
        tvCount = findViewById(R.id.tv_count);
        cbSub.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //减
                if (num > minNum) {
                    num--;
                    tvCount.setText(String.valueOf(num));
                    if (callBack != null) callBack.sub(num);
                } else {
                    Toast.makeText(context, "不能再减啦~", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cbAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //加
                if (num < maxNum) {
                    num++;
                    tvCount.setText(String.valueOf(num));
                    if (callBack != null) callBack.add(num);
                } else {
                    Toast.makeText(context, String.format("单次最多购买%1$s件", maxNum), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setNum(int num) {
        this.num = num;
        tvCount.setText(String.valueOf(num));
    }

    public int getNum() {
        return num;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMinNum(int minNum) {
        this.minNum = minNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void add(int num);

        void sub(int num);
    }
}
