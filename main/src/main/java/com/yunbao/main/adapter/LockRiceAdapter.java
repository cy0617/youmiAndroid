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
import com.yunbao.main.bean.LockRiceBean;
import com.yunbao.main.utils.MyClickInterface;

import java.util.List;

public class LockRiceAdapter extends RecyclerView.Adapter<LockRiceAdapter.ViewHolder> {

    private Context mContext;
    private List<LockRiceBean> list;
    private MyClickInterface myClickInterface;

    public LockRiceAdapter(Context mContext, List<LockRiceBean> list, MyClickInterface myClickInterface) {
        this.mContext = mContext;
        this.list = list;
        this.myClickInterface = myClickInterface;
    }

    @NonNull
    @Override
    public LockRiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_lockrice, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LockRiceAdapter.ViewHolder viewHolder, int i) {
        viewHolder.memo.setText(list.get(i).getMemo());
        viewHolder.time.setText(TimeUtil.getTime(Long.valueOf(list.get(i).getTime())));
        viewHolder.money.setText(list.get(i).getAfter());
        String score = list.get(i).getScore();
        viewHolder.score.setText(list.get(i).getScore()+"米粒");
        Double aDouble = Double.valueOf(score);
        if (aDouble>0){
            viewHolder.jiajian.setText("+");
        }else if (aDouble<0){
            viewHolder.jiajian.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView money;
        private  TextView time;
        private TextView memo;
        private TextView jiajian;
        private TextView score;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            money = itemView.findViewById(R.id.money);
            jiajian = itemView.findViewById(R.id.jiajian);
            memo = itemView.findViewById(R.id.memo);
            score = itemView.findViewById(R.id.score);
        }
    }
}
