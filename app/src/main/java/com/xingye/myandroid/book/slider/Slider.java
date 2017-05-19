package com.xingye.myandroid.book.slider;

import android.view.MotionEvent;

import com.xingye.myandroid.book.SlideLayout;

/**
 * Created by sunxiquan on 2017/5/16.
 */

public interface Slider {
    /**
     * 初始化
     * @param slideLayout
     */
    public void init(SlideLayout slideLayout);
    //public void resetFromAdapter()

    /**
     * 触摸事件
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event);

    /**
     * 完成滑动
     */
    public void computeScroll();

    /**
     * 滑动到下一页
     */
    public void slideNext();

    /**
     * 滑动到上一页
     */
    public void slidePrevious();


    public void resetFromAdapter();
}
