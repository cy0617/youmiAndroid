package com.yunbao.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.main.R;
import com.yunbao.main.bean.ShopOneClassBean;
import com.yunbao.main.utils.MyClickInterface;

import java.util.List;

public class ShopOneClassAdapter extends RecyclerView.Adapter<ShopOneClassAdapter.ViewHolder> {
    private Context mContext;
    private List<ShopOneClassBean> list;
    private MyClickInterface myClickInterface;

    public ShopOneClassAdapter(Context mContext, List<ShopOneClassBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ShopOneClassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_shop_one_class, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopOneClassAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tv_name.setText(list.get(i).getGc_name());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
