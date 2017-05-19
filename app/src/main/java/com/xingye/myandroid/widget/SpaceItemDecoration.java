package com.xingye.myandroid.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 设置recyclerView行间距类
 * Created by ruhuakeji-ios on 16/10/5.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    /**行间距高度*/
    private int space;
    private boolean isVertival = false;
    private boolean isHeader = false;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    public SpaceItemDecoration(int space, boolean isVertival, boolean isHeader) {
        this.space = space;
        this.isVertival = isVertival;
        this.isHeader = isHeader;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        /*if(parent.getChildPosition(view)>1){
            outRect.top = space;
        }*/
        if(isHeader){
            if(parent.getChildPosition(view) == 1){
                outRect.bottom = space/2;
            }
            if(parent.getChildPosition(view)>1){
                outRect.top = space/2;
                outRect.bottom = space/2;
            }
            if(isVertival){
                if(parent.getChildPosition(view)>1 && parent.getChildPosition(view) % 2 !=0){
                    outRect.left = space/2;
                }else if(parent.getChildPosition(view)>1 && parent.getChildPosition(view) % 2 ==0){
                    outRect.right = space/2;
                }
                /*if(parent.getChildPosition(view)>1 && parent.getChildPosition(view) % 2 ==0){
                    outRect.right = space;
                }*/
            }
        }else{
            if(parent.getChildPosition(view)>0){
                outRect.top = space;
            }
            if(isVertival){
                if(parent.getChildPosition(view) == 0){
                    outRect.left = space;
                }
            }
        }


        /*if(isVertival){
            if(parent.getChildPosition(view)%2 != 0){
                outRect.left = space;
            }
        }*/

    }
}
