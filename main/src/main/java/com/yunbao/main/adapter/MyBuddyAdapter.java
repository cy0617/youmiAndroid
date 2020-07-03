package com.yunbao.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunbao.main.R;
import com.yunbao.main.utils.MyClickInterface;

import java.util.List;


/**
 * 我的好友
 */

public class MyBuddyAdapter extends RecyclerView.Adapter<MyBuddyAdapter.ViewHolder> {


    private Context mContext;
    private List<String> list;
    private MyClickInterface myClickInterface;

    public MyBuddyAdapter(Context mContext, List<String> list, MyClickInterface myClickInterface) {
        this.mContext = mContext;
        this.list = list;
        this.myClickInterface = myClickInterface;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //设置自适应布局
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mybuddy, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.tv_leve.setText((position + 1) + "网红");
        viewHolder.tv_num.setText(list.get(position));
        String s = list.get(position);

        viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickInterface.myClick(position, 1);
            }
        });
        if (viewHolder.tv_num.getText().toString().equals("0")) {
            viewHolder.tv_num.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_num;
        TextView tv_leve;
        RelativeLayout ll_item;

        public ViewHolder(View v) {
            super(v);

            tv_num = v.findViewById(R.id.tv_num);
            tv_leve = v.findViewById(R.id.tv_leve);
            ll_item = v.findViewById(R.id.ll_item);

        }

    }

}
