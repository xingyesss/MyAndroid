package com.xingye.myandroid.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by sunxiquan on 2017/5/15.
 */

public class CustomView extends LinearLayout {
    private static final String TAG = "TAG";
    private Scroller mScroller;
    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    /**
     * 滚动的目标位置
     * @param fx
     * @param fy
     */
    public void smoothScrollTo(int fx,int fy){
        //获取滚动距离
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx,dy);
    }

    public void smoothScrollBy(int dx,int dy){
        mScroller.startScroll(mScroller.getFinalX(),mScroller.getFinalY(),dx,dy);
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
        super.computeScroll();

    }
}
