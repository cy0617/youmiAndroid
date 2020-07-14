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
import com.yunbao.main.R;
import com.yunbao.main.bean.MallSearchBean;
import com.yunbao.main.utils.MyClickInterface;

import java.util.List;


/**
 * 商城搜索列表
 */

public class MallSearchAdapter extends RecyclerView.Adapter<MallSearchAdapter.ViewHolder> {


    private Context mContext;
    private List<MallSearchBean.ListBean> list;
    private MyClickInterface myClickInterface;

    public MallSearchAdapter(Context mContext, List<MallSearchBean.ListBean> list, MyClickInterface myClickInterface) {
        this.mContext = mContext;
        this.list = list;
        this.myClickInterface = myClickInterface;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //设置自适应布局
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list, parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        Glide.with(mContext)
                .load(list.get(position).getThumb())
                .error(R.mipmap.default_image)
                .placeholder( R.mipmap.default_image)
                .into(viewHolder.iv_picture);

        viewHolder.tv_title.setText(list.get(position).getName());
        viewHolder.tv_price.setText(list.get(position).getPrice());

        viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickInterface.myClick(position,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size() > 0 ? list.size() : 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_picture;
        private TextView tv_title;
        private TextView tv_price;
        private LinearLayout ll_item;

        public ViewHolder(View v) {
            super(v);

            ll_item = v.findViewById(R.id.ll_item);
            iv_picture = v.findViewById(R.id.iv_picture);
            tv_title = v.findViewById(R.id.tv_title);
            tv_price = v.findViewById(R.id.tv_price);

        }

    }

}
