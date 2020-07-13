package com.yunbao.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.meihu.glide.Glide;
import com.yunbao.main.R;
import com.yunbao.main.bean.ShopBannerBean;
import com.yunbao.main.utils.MyClickInterface;

import java.util.List;

public class ShopBanner2Adapter extends RecyclerView.Adapter<ShopBanner2Adapter.ViewHolder> {

    private Context mContext;
    private List<ShopBannerBean> list;
    private MyClickInterface myClickInterface;

    public ShopBanner2Adapter(Context mContext, List<ShopBannerBean> list, MyClickInterface myClickInterface) {
        this.mContext = mContext;
        this.list = list;
        this.myClickInterface = myClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_shop_banner2, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(mContext).load(list.get(i).getImage()).into(viewHolder.iv);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
        }
    }
}
