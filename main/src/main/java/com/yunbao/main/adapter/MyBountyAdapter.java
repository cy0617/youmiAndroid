package com.yunbao.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.common.utils.TimeUtil;
import com.yunbao.main.R;
import com.yunbao.main.bean.BountyBean;

import java.util.List;


/**
 * 我的赏金
 */

public class MyBountyAdapter extends RecyclerView.Adapter<MyBountyAdapter.ViewHolder> {


    private Context mContext;
    private List<BountyBean> list;

    public MyBountyAdapter(Context mContext, List<BountyBean> list) {
        this.mContext = mContext;
        this.list = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //设置自适应布局
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_bounty, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.iv.setText(list.get(position).getUid());
        viewHolder.money.setText(list.get(position).getMoney());
        long dateByString = TimeUtil.getDateByString(list.get(position).getCreatetime());
        String comTime = TimeUtil.getComTime(dateByString);
        viewHolder.time.setText(comTime);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {


        private TextView iv;
        private TextView money;
        private TextView time;

        public ViewHolder(View v) {
            super(v);
            iv = itemView.findViewById(R.id.id);
            money = itemView.findViewById(R.id.money);
            time = itemView.findViewById(R.id.time);
        }

    }

}
