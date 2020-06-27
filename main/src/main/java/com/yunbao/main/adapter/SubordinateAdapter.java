package com.yunbao.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.main.R;
import com.yunbao.main.bean.SubordinateBean;

import java.util.List;


/**
 * 层级好友列表
 */

public class SubordinateAdapter extends RecyclerView.Adapter<SubordinateAdapter.ViewHolder> {


    private Context mContext;
    private List<SubordinateBean> list;

    public SubordinateAdapter(Context mContext, List<SubordinateBean> list) {
        this.mContext = mContext;
        this.list = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //设置自适应布局
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subordinate, parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.tv_id.setText(list.get(position).getId());
        viewHolder.tv_leve.setText(list.get(position).getGrade()+"级");
        viewHolder.tv_phone.setText(list.get(position).getMobile());
        viewHolder.tv_wx_code.setText(list.get(position).getWeixin());

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_id;
        TextView tv_leve;
        TextView tv_phone;
        TextView tv_wx_code;

        public ViewHolder(View v) {
            super(v);

            tv_id = v.findViewById(R.id.tv_id);
            tv_leve = v.findViewById(R.id.tv_leve);
            tv_phone = v.findViewById(R.id.tv_phone);
            tv_wx_code = v.findViewById(R.id.tv_wx_code);


        }

    }

}
