package com.yunbao.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.bean.BuyRecordBean;
import com.yunbao.common.utils.TimeUtil;
import com.yunbao.main.R;
import com.yunbao.main.utils.MyClickInterface;
import com.yunbao.main.utils.StringUtil;
import com.yunbao.mall.bean.GoodsPayBean;

import java.util.List;


/**
 * 支付方式
 */

public class PayTypeAdapter extends RecyclerView.Adapter<PayTypeAdapter.ViewHolder> {


    private Context mContext;
    private List<GoodsPayBean> list;
    private MyClickInterface myClickInterface;
    private String balance;

    public PayTypeAdapter(Context mContext, List<GoodsPayBean> list, MyClickInterface myClickInterface) {
        this.mContext = mContext;
        this.list = list;
        this.myClickInterface = myClickInterface;
    }


    public void setBalance(String balance){
        this.balance = balance;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //设置自适应布局
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pay_type, parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {


        viewHolder.tv_title.setText(list.get(position).getName());

        Glide.with(mContext)
                .load(list.get(position).getThumb())
                .error(R.mipmap.default_image)
                .placeholder( R.mipmap.default_image)
                .into(viewHolder.iv_img);
        if(list.get(position).isChecked()){
            viewHolder.iv_select.setVisibility(View.VISIBLE);
        }else{
            viewHolder.iv_select.setVisibility(View.GONE);
        }

        if(list.get(position).getType().equals("1")||list.get(position).getType().equals("2")){
            viewHolder.tv_money.setVisibility(View.GONE);
        }else if(list.get(position).getType().equals("3")){
            if(!StringUtil.isEmpty(balance)){
                viewHolder.tv_money.setVisibility(View.VISIBLE);
                viewHolder.tv_money.setText("（"+balance+"）");
            }
        }else if(list.get(position).getType().equals("4")){
            if(CommonAppConfig.getInstance().getUserBean()!=null&&!StringUtil.isEmpty(CommonAppConfig.getInstance().getUserBean().getKy_score())){
                viewHolder.tv_money.setVisibility(View.VISIBLE);
                viewHolder.tv_money.setText("（"+CommonAppConfig.getInstance().getUserBean().getKy_score()+"）");
            }
        }

        viewHolder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickInterface.myClick(position,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_img;
        private ImageView iv_select;
        private TextView tv_title;
        private RelativeLayout rl_item;
        private TextView tv_money;

        public ViewHolder(View v) {
            super(v);

            iv_img = v.findViewById(R.id.iv_img);
            tv_title = v.findViewById(R.id.tv_title);
            iv_select = v.findViewById(R.id.iv_select);
            rl_item = v.findViewById(R.id.rl_item);
            tv_money = v.findViewById(R.id.tv_money);

        }

    }

}
