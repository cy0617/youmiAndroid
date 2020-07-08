package com.yunbao.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.common.utils.TimeUtil;
import com.yunbao.main.R;
import com.yunbao.main.bean.MyUbBean;
import com.yunbao.main.utils.MyClickInterface;

import java.util.List;

public class MyUbAdapter extends RecyclerView.Adapter<MyUbAdapter.ViewHolder> {

    private Context mContext;
    private List<MyUbBean> list;
    private MyClickInterface myClickInterface;

    public MyUbAdapter(Context mContext, List<MyUbBean> list, MyClickInterface myClickInterface) {
        this.mContext = mContext;
        this.list = list;
        this.myClickInterface = myClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_ub, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String type = list.get(i).getType();
        if (type.equals("0")){
            viewHolder.type.setText("-");
        }else if (type.equals("1")){
            viewHolder.type.setText("+");
        }
        String action = list.get(i).getAction();
        if (action.equals("1")){
            viewHolder.action.setText("赠送礼物");
        }else if (action.equals("2")){
            viewHolder.action.setText("弹幕");
        }else if (action.equals("3")){
            viewHolder.action.setText("登录奖励");
        }else if (action.equals("4")){
            viewHolder.action.setText("购买VIP");
        }else if (action.equals("5")){
            viewHolder.action.setText("购买坐骑");
        }else if (action.equals("6")){
            viewHolder.action.setText("房间扣费");
        }else if (action.equals("7")){
            viewHolder.action.setText("计时扣费");
        }else if (action.equals("8")){
            viewHolder.action.setText("发送红包");
        }
        viewHolder.totalcoin.setText(list.get(i).getTotalcoin()+"U币");
        viewHolder.addtime.setText(TimeUtil.getTimeYMDHMTwo(Long.valueOf(list.get(i).getAddtime())));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private  TextView type;
        private  TextView totalcoin;
        private TextView action;
        private TextView addtime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            action = itemView.findViewById(R.id.ub_action);
            addtime = itemView.findViewById(R.id.ub_addtime);
            type = itemView.findViewById(R.id.type);
            totalcoin = itemView.findViewById(R.id.totalcoin);
        }
    }
}
