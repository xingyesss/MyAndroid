package com.xingye.myandroid.book.slider;

/**
 * Created by sunxiquan on 2017/5/16.
 */

public abstract class BaseSlider implements Slider {
    /**手指移动的方向*/
    public static final int MOVE_TO_LEFT = 0;   //移动到下一页
    public static final int MOVE_TO_RIGHT = 1;  //移动到上一页
    public static final int MOVE_NO_RESULT = 4;

    /**触摸的模式*/
    static final int MODE_NONE = 0;
    static final int MODE_MOVE = 1;

}
