package com.yunbao.main.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 定义水平方向的距离
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private int about;//定义Item左右之间的距离
    private int itemNum;//每列多少个item

    public GridItemDecoration(int about, int itemNum, Context mContext) {
        this.about = dip2px(about,mContext);
        this.itemNum = itemNum;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        if(itemNum == 1){
            outRect.left = about;
            outRect.right = about;
        }

        if(itemNum == 2){
            if(position%itemNum == 0){
                outRect.left = about;
                outRect.right = about / 2;
            }else if(position%itemNum == 1){
                outRect.left = about / 2;
                outRect.right = about;;
            }
        }

        if(itemNum == 3){
            if(position%itemNum == 0){
                outRect.left = 0;
                outRect.right = about / 2;
            }else if(position%itemNum == 1){
                outRect.left = about / 2;
                outRect.right = about / 2;;
            }else if(position%itemNum == 2){
                outRect.left = about / 2;
                outRect.right = 0;;
            }
        }
    }

    public int dip2px(float dpValue,Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}