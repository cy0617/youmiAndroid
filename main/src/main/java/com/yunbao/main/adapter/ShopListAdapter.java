package com.yunbao.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meihu.glide.Glide;
import com.yunbao.main.R;
import com.yunbao.main.bean.ShopGoodsTypeBean;

import java.util.List;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ViewHolder> {
    private List<ShopGoodsTypeBean> list;
    private Context mContext;

    public ShopListAdapter(List<ShopGoodsTypeBean> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ShopListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_shop_goods_type, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopListAdapter.ViewHolder viewHolder, int i) {
        Glide.with(mContext).load(list.get(i).getThumb()).into(viewHolder.iv_image);
        viewHolder.tv_name.setText(list.get(i).getName());
        viewHolder.tv_price.setText("￥"+list.get(i).getPrice());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_image;
        private TextView tv_name;
        private TextView tv_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.iv_image);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
        }
    }
}
