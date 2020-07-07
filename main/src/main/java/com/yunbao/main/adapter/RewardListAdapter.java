package com.yunbao.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunbao.main.R;
import com.yunbao.main.bean.RewardRecordBean;
import com.yunbao.main.utils.MyClickInterface;
import com.yunbao.main.utils.StringUtil;

import java.util.List;


/**
 * 打赏记录
 */

public class RewardListAdapter extends RecyclerView.Adapter<RewardListAdapter.ViewHolder> {


    private Context mContext;
    private List<RewardRecordBean> list;
    private MyClickInterface myClickInterface;

    public RewardListAdapter(Context mContext, List<RewardRecordBean> list, MyClickInterface myClickInterface) {
        this.mContext = mContext;
        this.list = list;
        this.myClickInterface = myClickInterface;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //设置自适应布局
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_raward_record, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.tv_id.setText(list.get(position).getUid());
        viewHolder.tv_money.setText("¥" + list.get(position).getMoney());
        viewHolder.tv_wx_code.setText(list.get(position).getWeixin());
        viewHolder.tv_phone.setText(list.get(position).getMobile());
        if (list.get(position).getStatus().equals("0")) {
            viewHolder.tv_status.setText("等待打赏");
            viewHolder.tv_status.setTextColor(Color.parseColor("#333333"));
            viewHolder.tv_status.setBackgroundResource(R.color.white);
        } else if (list.get(position).getStatus().equals("1")) {
            viewHolder.tv_status.setText("批准");
            viewHolder.tv_status.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.tv_status.setBackgroundResource(R.mipmap.min_btn_img);
            Glide.with(mContext)
                    .load(list.get(position).getImg())
                    .error(R.mipmap.default_image)
                    .placeholder(R.mipmap.default_image)
                    .into(viewHolder.iv_img);
        } else if (list.get(position).getStatus().equals("2")) {
            viewHolder.tv_status.setText("已晋级");
            viewHolder.tv_status.setTextColor(Color.parseColor("#333333"));
            viewHolder.tv_status.setBackgroundResource(R.color.white);
            Glide.with(mContext)
                    .load(list.get(position).getImg())
                    .error(R.mipmap.default_image)
                    .placeholder(R.mipmap.default_image)
                    .into(viewHolder.iv_img);
        }


        viewHolder.tv_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getStatus().equals("1")) {
                    myClickInterface.myClick(position, 1);
                }
            }

        });
        viewHolder.iv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mContext.startActivity(new Intent(mContext, ChatRoomViewHolder.class));
            }
        });
        viewHolder.iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtil.isEmpty(list.get(position).getImg())) {
                    myClickInterface.myClick(position, 2);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_chat;
        TextView tv_id;
        TextView tv_money;
        TextView tv_wx_code;
        TextView tv_phone;
        TextView tv_status;
        ImageView iv_img;

        public ViewHolder(View v) {
            super(v);

            tv_id = v.findViewById(R.id.tv_id);
            tv_money = v.findViewById(R.id.tv_money);
            tv_wx_code = v.findViewById(R.id.tv_wx_code);
            tv_phone = v.findViewById(R.id.tv_phone);
            tv_status = v.findViewById(R.id.tv_status);
            iv_img = v.findViewById(R.id.iv_img);
            iv_chat = v.findViewById(R.id.iv_chat);
        }

    }

}
