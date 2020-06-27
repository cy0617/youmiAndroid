package com.yunbao.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunbao.common.bean.BuyRecordBean;
import com.yunbao.common.utils.TimeUtil;
import com.yunbao.main.R;
import com.yunbao.main.utils.MyClickInterface;

import java.util.List;


/**
 * 我的赏金
 */

public class MyBountyAdapter extends RecyclerView.Adapter<MyBountyAdapter.ViewHolder> {


    private Context mContext;
    private List<String> list;

    public MyBountyAdapter(Context mContext, List<String> list) {
        this.mContext = mContext;
        this.list = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //设置自适应布局
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_bounty, parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {


    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View v) {
            super(v);


        }

    }

}
