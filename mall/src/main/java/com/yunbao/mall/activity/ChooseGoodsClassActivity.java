package com.yunbao.mall.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.adapter.GoodsClassLeftAdapter;
import com.yunbao.mall.adapter.GoodsClassRightAdapter;
import com.yunbao.mall.bean.GoodsClassBean;
import com.yunbao.mall.bean.GoodsClassTitleBean;
import com.yunbao.mall.http.MallHttpUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 选择商品类别
 */
public class ChooseGoodsClassActivity extends AbsActivity implements OnItemClickListener<GoodsClassBean> {

    private RecyclerView mRecyclerViewLeft;
    private RecyclerView mRecyclerViewRight;
    private GoodsClassLeftAdapter mLeftAdapter;
    private GoodsClassRightAdapter mRightAdapter;
    private GoodsClassTitleBean titleBean;
    private String twoClassId;
    private String oneClassId;

    private ArrayList<GoodsClassBean> list;
    private List<GoodsClassTitleBean> titleList;
    private String name;
    private String id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_goods_class;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_077));
        mRecyclerViewLeft = findViewById(R.id.recyclerView_left);
        mRecyclerViewLeft.setHasFixedSize(true);
        mRecyclerViewLeft.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewRight = findViewById(R.id.recyclerView_right);
        mRecyclerViewRight.setHasFixedSize(true);
        mRecyclerViewRight.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        MallHttpUtil.getGoodsClass(new HttpCallback() {


            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONArray array = JSON.parseArray(Arrays.toString(info));
                    titleList = new ArrayList<>();
                    for (int i = 0, size = array.size(); i < size; i++) {
                        JSONObject obj = array.getJSONObject(i);
                        titleBean = new GoodsClassTitleBean();
                        titleBean.setName(obj.getString("gc_name"));
                        titleBean.setId(obj.getString("gc_id"));
                        oneClassId = obj.getString("gc_id");
                        list = new ArrayList<>();
                        JSONArray twoArray = obj.getJSONArray("two_list");
                        for (int j = 0, size1 = twoArray.size(); j < size1; j++) {
                            JSONObject obj1 = twoArray.getJSONObject(j);
                            GoodsClassBean bean = new GoodsClassBean();
                            bean.setTitle(true);
                            bean.setId(obj1.getString("gc_id"));
                            bean.setName(obj1.getString("gc_name"));
                            twoClassId = obj1.getString("gc_id");
                            list.add(bean);
                            JSONArray threeArray = obj1.getJSONArray("three_list");
                            for (int k = 0, size2 = threeArray.size(); k < size2; k++) {
                                JSONObject obj2 = threeArray.getJSONObject(k);
                                GoodsClassBean subBean = new GoodsClassBean();
                                subBean.setTitle(false);
                                subBean.setId(obj2.getString("gc_id"));
                                subBean.setName(obj2.getString("gc_name"));
                                subBean.setOneClassId(oneClassId);
                                subBean.setTwoClassId(twoClassId);
                                list.add(subBean);
                            }
                        }
                        titleBean.setList(list);
                        titleList.add(titleBean);

                    }
                    if (titleList.size() > 0) {
                        GoodsClassTitleBean bean0 = titleList.get(0);
                        bean0.setChecked(true);
                        if (mRecyclerViewLeft != null) {
                            mLeftAdapter = new GoodsClassLeftAdapter(mContext, titleList);
                            mLeftAdapter.setOnItemClickListener(new OnItemClickListener<GoodsClassTitleBean>() {
                                @Override
                                public void onItemClick(GoodsClassTitleBean bean, int position) {

                                    titleBean=bean;

                                    if (mRightAdapter != null) {

                                        mRightAdapter.refreshData(bean.getList());
                                    }
                                    if (mRecyclerViewRight != null) {
                                        mRecyclerViewRight.scrollToPosition(0);
                                    }
                                }
                            });
                            mRecyclerViewLeft.setAdapter(mLeftAdapter);
                        }
                        if (mRecyclerViewRight != null) {
                            mRightAdapter = new GoodsClassRightAdapter(mContext);
                            mRightAdapter.setList(bean0.getList());
                            mRightAdapter.setOnItemClickListener(ChooseGoodsClassActivity.this);
                            mRecyclerViewRight.setAdapter(mRightAdapter);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(GoodsClassBean bean, int position) {
        Intent intent = new Intent();

        name = titleBean.getName();
        id = titleBean.getId();

        intent.putExtra("oneName",name );
        intent.putExtra("oneClassId", id);

        Log.e("eeeeeeeeeeeee", "onItemClick: "+ name);
        Log.e("eeeeeeeeeeeee", "onItemClick: "+ id);
        intent.putExtra(Constants.MALL_GOODS_CLASS, bean);
        setResult(RESULT_OK, intent);
        finish();
    }
}
