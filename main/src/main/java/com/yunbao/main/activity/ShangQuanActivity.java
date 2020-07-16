package com.yunbao.main.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.bean.GoodsBeanTwo;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.GoodsRingAdapter;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.main.utils.GridItemDecoration;
import com.yunbao.main.utils.MyClickInterface;
import com.yunbao.main.utils.StringUtil;
import com.yunbao.main.views.refreshlayout.RefreshLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShangQuanActivity extends AbsActivity implements MyClickInterface {

    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private GoodsRingAdapter adapter;
    private List<GoodsBeanTwo> list;
    private ImageView iv_title_right;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_shang_quan;
    }

    @Override
    public void main() {
        list = new ArrayList<>();
        iv_title_right = findViewById(R.id.iv_title_right);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        setTitle("老百姓的12987");
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GoodsRingAdapter(mContext, list, this);
        GridItemDecoration gridItemDecoration = new GridItemDecoration(10, 2, mContext);
        recyclerView.addItemDecoration(gridItemDecoration);//10表示10dp
        recyclerView.setAdapter(adapter);
        queryGoodsList();


        iv_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, BuyRecordingAct.class));
            }
        });

        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onPullDownToRefresh() {
                refreshLayout.pageNumber = 1;
                queryGoodsList();
            }

            @Override
            public void onPullUpToRefresh() {
                refreshLayout.pageNumber++;
                queryGoodsList();
            }
        });


    }


    public void queryGoodsList(){
        MainHttpUtil.getGoodsList(refreshLayout.pageNumber,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    if (refreshLayout.pageNumber == 1 && info.length > 0) {
                        list.clear();
                    }
                    for(int i = 0;i<info.length;i++){
                        JSONObject obj = JSON.parseObject(info[i]);
                        GoodsBeanTwo goodsBeanTwo = new GoodsBeanTwo();
                        goodsBeanTwo.setDesc(obj.getString("desc"));
                        goodsBeanTwo.setTitle(obj.getString("title"));
                        goodsBeanTwo.setId(obj.getLong("id"));
                        goodsBeanTwo.setImg(obj.getString("img"));
                        goodsBeanTwo.setMoney(StringUtil.subZeroAndDot(new BigDecimal(obj.getString("money"))
                                .setScale(2, BigDecimal.ROUND_DOWN).toPlainString()));
                        list.add(goodsBeanTwo);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.show(msg);
                }
                refreshLayout.onLoad(info.length);
            }
            @Override
            public void onError() {
                refreshLayout.onLoad(-1);
            }
        });
    }

    @Override
    public void myClick(int position, int type) {
        if(type == 1){
            Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
            intent.putExtra("goodsId",String.valueOf(list.get(position).getId()));
            mContext.startActivity(intent);
        }
    }

}
