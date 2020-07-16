package com.yunbao.main.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meihu.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.yunbao.common.Constants;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.main.R;
import com.yunbao.main.activity.GoodsDetailsActivity;
import com.yunbao.main.activity.ShangQuanActivity;
import com.yunbao.main.bean.ShopBannerBean;
import com.yunbao.main.bean.ShopGoodsTypeBean;
import com.yunbao.main.utils.GridItemDecoration;
import com.yunbao.main.utils.MyClickInterface;
import com.yunbao.mall.activity.GoodsDetailActivity;

import java.util.ArrayList;
import java.util.regex.Pattern;


/**
 * 商城首页
 */

public class AllMallAdapter extends RecyclerView.Adapter {


    private LayoutInflater inflater;
    private static final int TITLE = 0;
    private static final int MAIN = 1;

    private Context mContext;
    private Activity mActivity;
    private TitleHolder titleHolder;
    private MainHolder mainHolder;
    private Pattern httpPattern;

    private ArrayList<ShopGoodsTypeBean> shopGoodsTypeBeans;//商品列表
    private ArrayList<ShopBannerBean> shopBannerList;//banner
    private ArrayList<ShopBannerBean> shopBannerList2;//中午图片
    private MyClickInterface myClickInterface;
    public static boolean isCreateBanner = false;  //标识是否重新创建Banner图


    public AllMallAdapter(Context mContext, Activity mActivity,
                          ArrayList<ShopBannerBean> shopBannerList,
                          ArrayList<ShopBannerBean> shopBannerList2,
                          ArrayList<ShopGoodsTypeBean> shopGoodsTypeBeans,
                          MyClickInterface myClickInterface) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.shopBannerList = shopBannerList;
        this.shopBannerList2 = shopBannerList2;
        this.shopGoodsTypeBeans = shopGoodsTypeBeans;
        this.myClickInterface = myClickInterface;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TITLE;
        } else if (position > 0) {
            return MAIN;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TITLE:
                View itemTitle = inflater.inflate(R.layout.item_all_mall_title, parent, false);
                return new TitleHolder(itemTitle);
            case MAIN:
                View itemMain = inflater.inflate(R.layout.item_all_mall_bottom, parent, false);
                return new MainHolder(itemMain);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleHolder) {
            setTitle((TitleHolder) holder);
        } else if (holder instanceof MainHolder) {
            setMain((MainHolder) holder, position);
        }
    }


    public class TitleHolder extends RecyclerView.ViewHolder {

        private ImageView iv_xinpin;
        private ImageView iv_rexiao;
        private RelativeLayout rlHot;
        private RelativeLayout rlNew;
        private Banner banner;
        private LinearLayout llBusinessDistrict;

        TitleHolder(View itemView) {
            super(itemView);

            iv_xinpin = itemView.findViewById(R.id.iv_xinpin);
            iv_rexiao = itemView.findViewById(R.id.iv_rexiao);
            rlHot = itemView.findViewById(R.id.rl_hot);
            rlNew = itemView.findViewById(R.id.rl_new);
            banner = itemView.findViewById(R.id.banner);
            llBusinessDistrict = itemView.findViewById(R.id.ll_business_district);
        }
    }

    public class MainHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerView;

        public MainHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }

    private void setTitle(final TitleHolder holder) {
        titleHolder = holder;

        if (shopBannerList2 != null) {
            if (shopBannerList2.size() > 0) {
                Glide.with(mContext).load(shopBannerList2.get(0).getImage()).into(holder.iv_xinpin);
                Glide.with(mContext).load(shopBannerList2.get(1).getImage()).into(holder.iv_rexiao);
            }
        }

        if (isCreateBanner) {
            if (shopBannerList != null) {
                if (shopBannerList.size() > 0) {
                    holder.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                    holder.banner.setImages(shopBannerList);
                    holder.banner.setImageLoader(new ImageLoader() {
                        @Override
                        public void displayImage(Context context, Object path, ImageView imageView) {
                            ImgLoader.display(mActivity, ((ShopBannerBean) path).getImage(), imageView);
                        }
                    });
                    holder.banner.setBannerAnimation(Transformer.Default);
                    holder.banner.setDelayTime(3000);
                    holder.banner.isAutoPlay(true);
                    holder.banner.setIndicatorGravity(BannerConfig.CENTER);
                    holder.banner.start();
                    isCreateBanner = !isCreateBanner;
                }
            }
        }

        holder.llBusinessDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, ShangQuanActivity.class));
            }
        });
        holder.rlNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickInterface.myClick(0, 1);
                holder.rlNew.setBackgroundResource(R.drawable.shape_home_intermediate_bg);
                holder.rlHot.setBackgroundResource(R.drawable.bg_xinpin);
            }
        });
        holder.rlHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickInterface.myClick(0, 2);
                holder.rlHot.setBackgroundResource(R.drawable.shape_home_intermediate_bg);
                holder.rlNew.setBackgroundResource(R.drawable.bg_xinpin);
            }
        });

        /**
         * 轮播图点击
         */
        holder.banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                String url = shopBannerList.get(position).getUrl();
                httpPattern = Pattern
                        .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$");
                if (httpPattern.matcher(url).matches()) {
                    Uri uri = Uri.parse(url);//要跳转的网址
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    mActivity.startActivity(intent);
                } else {
                    Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
                    intent.putExtra(Constants.MALL_GOODS_ID, url);
                    mActivity.startActivity(intent);
                }
            }
        });

    }

    private void setMain(MainHolder holder, final int position) {
        mainHolder = holder;

        ShopListAdapter shopListAdapter = new ShopListAdapter(shopGoodsTypeBeans, mActivity);
        holder.recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        holder.recyclerView.setAdapter(shopListAdapter);


        shopListAdapter.setActionListener(new ShopListAdapter.ActionListener() {
            @Override
            public void onClick(int position) {
                String id = shopGoodsTypeBeans.get(position).getId();
                Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
                intent.putExtra(Constants.MALL_GOODS_ID, id);
                mActivity.startActivity(intent);
            }
        });
    }

    public void updUi(){
        titleHolder.rlHot.setBackgroundResource(R.drawable.bg_xinpin);
        titleHolder.rlNew.setBackgroundResource(R.drawable.bg_xinpin);
    }
}
