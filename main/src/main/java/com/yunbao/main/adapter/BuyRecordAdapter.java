package com.yunbao.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunbao.common.bean.BuyRecordBean;
import com.yunbao.common.utils.TimeUtil;
import com.yunbao.main.R;
import com.yunbao.main.utils.MyClickInterface;

import java.util.List;


/**
 * 购买记录
 */

public class BuyRecordAdapter extends RecyclerView.Adapter<BuyRecordAdapter.ViewHolder> {


    private Context mContext;
    private List<BuyRecordBean> list;
    private MyClickInterface myClickInterface;
    public BuyRecordAdapter(Context mContext, final List<BuyRecordBean> list, MyClickInterface myClickInterface) {
        this.mContext = mContext;
        this.list = list;
        this.myClickInterface = myClickInterface;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //设置自适应布局
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buy_record, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {


        viewHolder.tv_order_state.setText(list.get(position).getStatus_name());

        String time = TimeUtil.getTimeYMDHMTwo(list.get(position).getAddtime());
        viewHolder.tv_time.setText(time);
        Glide.with(mContext)
                .load(list.get(position).getSpec_thumb())
                .error(R.mipmap.default_image)
                .placeholder(R.mipmap.default_image)
                .into(viewHolder.iv_picture);

        viewHolder.tv_name.setText(list.get(position).getGoods_name());
        viewHolder.tv_money.setText(list.get(position).getPrice());
        viewHolder.tv_num.setText(list.get(position).getNums());
        viewHolder.tv_goods_sum.setText("共" + list.get(position).getNums() + "件商品，合计");
        viewHolder.tv_money_sum.setText(list.get(position).getTotal());
        viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickInterface.myClick(position,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_item;
        private ImageView iv_picture;
        private TextView tv_name;
        private TextView tv_money;
        private TextView tv_money_sum;
        private TextView tv_goods_sum;
        private TextView tv_order_state;
        private TextView tv_time;
        private TextView tv_num;

        public ViewHolder(View v) {
            super(v);

            iv_picture = v.findViewById(R.id.iv_picture);
            tv_name = v.findViewById(R.id.tv_name);
            tv_money = v.findViewById(R.id.tv_money);
            tv_money_sum = v.findViewById(R.id.tv_money_sum);
            tv_goods_sum = v.findViewById(R.id.tv_goods_sum);
            tv_order_state = v.findViewById(R.id.tv_order_state);
            tv_time = v.findViewById(R.id.tv_time);
            tv_num = v.findViewById(R.id.tv_num);
            ll_item = v.findViewById(R.id.ll_item);
        }

    }



}
