package com.yunbao.main.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.KeyBoardUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.views.AbsMainViewHolder;
import com.yunbao.main.R;
import com.yunbao.main.bean.GetOneGoodsAllBean;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.main.utils.StringUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 商圈
 */
public class MainShopViewHolder extends AbsMainViewHolder {

    //    private RefreshLayout refreshLayout;
//    private RecyclerView recyclerView;
//    private GoodsRingAdapter adapter;
//    private List<GoodsBeanTwo> list;
//    private ImageView iv_title_right;
    private ImageView iv_ym;
    private ImageView iv_tb;
    private ImageView iv_jd;
    private ImageView iv_pdd;
    private ImageView iv_tm;
    private ImageView iv_mgj;

    private List<GetOneGoodsAllBean> mTitleList = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();
    private ViewPager viewPager;
    private FragmentManager fragmentManager;
    private MagicIndicator mIndicator;
    private CommonNavigator commonNavigator;
    private EditText edit;
    private TextView btn_search;


    public MainShopViewHolder(Context context, ViewGroup parentView, FragmentManager fragmentManager) {
        super(context, parentView);
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int getLayoutId() {
        //return R.layout.view_main_shop;
        return R.layout.activity_umi_mall;
    }

    @Override
    public void init() {

        viewPager = findViewById(R.id.view_pager);
        mIndicator = findViewById(R.id.indicator);
        edit = findViewById(R.id.edit);
        btn_search = findViewById(R.id.btn_search);
        getOneGoodsAllClass();

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtil.isEmpty(edit.getText().toString())) {
                    ToastUtil.show(R.string.edit_search);
                    return;
                }
                KeyBoardUtil.closeKeyboard(mContext, edit);
                Intent intent = new Intent(mContext, MallSearchActivity.class);
                intent.putExtra("keywords", edit.getText().toString());
                mContext.startActivity(intent);
                edit.setText(null);
            }
        });

        /*iv_ym = findViewById(R.id.iv_ym);
        iv_tb = findViewById(R.id.iv_tb);
        iv_jd = findViewById(R.id.iv_jd);
        iv_pdd = findViewById(R.id.iv_pdd);
        iv_tm = findViewById(R.id.iv_tm);
        iv_mgj = findViewById(R.id.iv_mgj);

        iv_ym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShangQuanActivity.class);
                mContext.startActivity(intent);
            }
        });
        iv_tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.taobao.com/");//要跳转的网址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
        iv_jd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.jd.com/");//要跳转的网址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
        iv_pdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.pinduoduo.com/");//要跳转的网址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
        iv_tm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.tmall.com/");//要跳转的网址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
        iv_mgj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.mogu.com/");//要跳转的网址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });*/

//        list = new ArrayList<>();
//        iv_title_right = findViewById(R.id.iv_title_right);
//        refreshLayout = findViewById(R.id.refreshLayout);
//        recyclerView = findViewById(R.id.recyclerView);
//
//        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new GoodsRingAdapter(mContext, list, this);
//        GridItemDecoration gridItemDecoration = new GridItemDecoration(10, 2, mContext);
//        recyclerView.addItemDecoration(gridItemDecoration);//10表示10dp
//        recyclerView.setAdapter(adapter);
//        queryGoodsList();
//
//
//        iv_title_right.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.startActivity(new Intent(mContext, BuyRecordingAct.class));
//            }
//        });
//
//        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
//            @Override
//            public void onPullDownToRefresh() {
//                refreshLayout.pageNumber = 1;
//                queryGoodsList();
//            }
//
//            @Override
//            public void onPullUpToRefresh() {
//                refreshLayout.pageNumber++;
//                queryGoodsList();
//            }
//        });


    }

    public void getOneGoodsAllClass() {
        MainHttpUtil.getOneGoodsAllClass(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    for (int i = 0; i < info.length; i++) {
                        JSONObject obj = JSON.parseObject(info[i]);
                        GetOneGoodsAllBean goodsAllBean = new GetOneGoodsAllBean();
                        goodsAllBean.setGc_id(obj.getString("gc_id"));
                        goodsAllBean.setGc_name(obj.getString("gc_name"));
                        mTitleList.add(goodsAllBean);
                    }

                    for (int i = 0; i < mTitleList.size(); i++) {
                        if (i == 0) {
                            mFragments.add(new AllMallFragment());
                        } else {
                            mFragments.add(CommodityFragment.getInstance(mTitleList.get(i).getGc_id()));
                        }
                    }

                    FragmentPagerAdapter adapter = new FragmentPagerAdapter(fragmentManager) {
                        @Override
                        public Fragment getItem(int position) {
                            return mFragments.get(position);
                        }

                        @Override
                        public int getCount() {
                            return mFragments.size();
                        }

                        @Override
                        public CharSequence getPageTitle(int position) {
                            return mTitleList.get(position).getGc_name();
                        }
                    };

                    viewPager.setAdapter(adapter);

                    commonNavigator = new CommonNavigator(mContext);
                    commonNavigator.setAdapter(new CommonNavigatorAdapter() {

                        @Override
                        public int getCount() {
                            return mTitleList == null ? 0 : mTitleList.size();
                        }

                        @Override
                        public IPagerTitleView getTitleView(Context context, final int index) {
                            SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                            //simplePagerTitleView.getPaint().setFakeBoldText(true);
                            simplePagerTitleView.setText(mTitleList.get(index).getGc_name());
                            simplePagerTitleView.setNormalColor(ContextCompat.getColor(mContext, R.color.black_text));
                            simplePagerTitleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.text_color_eb7946));
                            simplePagerTitleView.setTextSize(16);
                            simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    viewPager.setCurrentItem(index);
                                }
                            });
                            return simplePagerTitleView;
                        }

                        @Override
                        public IPagerIndicator getIndicator(Context context) {
                            return null;
                        }
                    });

                    mIndicator.setNavigator(commonNavigator);
                    ViewPagerHelper.bind(mIndicator, viewPager);
                } else {
                    ToastUtil.show(msg);
                }
            }

            @Override
            public void onError() {

            }
        });
    }


//    public void queryGoodsList(){
//        MainHttpUtil.getGoodsList(refreshLayout.pageNumber,new HttpCallback() {
//            @Override
//            public void onSuccess(int code, String msg, String[] info) {
//                if (code == 0) {
//                    if (refreshLayout.pageNumber == 1 && info.length > 0) {
//                        list.clear();
//                    }
//                    for(int i = 0;i<info.length;i++){
//                        JSONObject obj = JSON.parseObject(info[i]);
//                        GoodsBeanTwo goodsBeanTwo = new GoodsBeanTwo();
//                        goodsBeanTwo.setDesc(obj.getString("desc"));
//                        goodsBeanTwo.setTitle(obj.getString("title"));
//                        goodsBeanTwo.setId(obj.getLong("id"));
//                        goodsBeanTwo.setImg(obj.getString("img"));
//                        goodsBeanTwo.setMoney(StringUtil.subZeroAndDot(new BigDecimal(obj.getString("money"))
//                                .setScale(2, BigDecimal.ROUND_DOWN).toPlainString()));
//                        list.add(goodsBeanTwo);
//                    }
//                    adapter.notifyDataSetChanged();
//                } else {
//                    ToastUtil.show(msg);
//                }
//                refreshLayout.onLoad(info.length);
//            }
//            @Override
//            public void onError() {
//                refreshLayout.onLoad(-1);
//            }
//        });
//    }

//    @Override
//    public void myClick(int position, int type) {
//        if(type == 1){
//            Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
//            intent.putExtra("goodsId",String.valueOf(list.get(position).getId()));
//            mContext.startActivity(intent);
//        }
//    }
}
