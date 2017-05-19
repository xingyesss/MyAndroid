package com.xingye.myandroid.book;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xingye.myandroid.book.slider.SlideAdapter;
import com.xingye.myandroid.book.slider.Slider;

/**
 * Created by sunxiquan on 2017/5/16.
 */

public class SlideLayout extends ViewGroup {
    //记录点击事件的位置
    private int mDownMotionX,mDownMotionY;
    //记录点击事件
    private long mDownMotionTime;
    private CenterClickListener centerClickListener;
    private OnSlideChangeListener onSlideChangeListener;
    private Slider mSlider;
    private SlideAdapter adapter;

    public void setOnSlideChangeListener(OnSlideChangeListener onSlideChangeListener) {
        this.onSlideChangeListener = onSlideChangeListener;
    }

    public void setCenterClickListener(CenterClickListener centerClickListener) {
        this.centerClickListener = centerClickListener;
    }

    public void setSlider(Slider slider) {
        this.mSlider = slider;
        slider.init(this);
        resetFromAdapter();
    }

    public void setAdapter(SlideAdapter adapter) {
        this.adapter = adapter;
    }

    public void resetFromAdapter() {
        removeAllViews();
        if(mSlider!=null && adapter !=null){
            mSlider.resetFromAdapter();
        }
    }

    public SlideLayout(Context context) {
        super(context);
    }

    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for(int i=0;i<getChildCount();i++){
            View child = getChildAt(i);
            int height = child.getMeasuredHeight();
            int width = child.getMeasuredWidth();
            child.layout(0,0,width,height);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width,height);

        for(int i=0;i < getChildCount();i++){
            getChildAt(i).measure(widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownMotionX = (int) event.getX();
                mDownMotionY = (int) event.getY();
                mDownMotionTime = System.currentTimeMillis();

            break;
            case MotionEvent.ACTION_UP:
                computeTapMotion(event);
            break;
        }
        return mSlider.onTouchEvent(event) || super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //完成滑动
        mSlider.computeScroll();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    /**滑动到下一页*/
    public void slideNext(){
        mSlider.slideNext();
    }

    public void slidePrevious(){
        mSlider.slidePrevious();
    }

    private void computeTapMotion(MotionEvent event) {
        int xDiff = (int) Math.abs(event.getX()-mDownMotionX);
        int yDiff = (int) Math.abs(event.getY()-mDownMotionY);
        long timeDiff =  System.currentTimeMillis()-mDownMotionTime;
        //当抬起时满足一下条件，认为用户为单击事件
        if(xDiff<5 && yDiff<5 && timeDiff<200){
            if(centerClickListener!=null){
                centerClickListener.onClick(event);
            }
        }
    }

    public void slideScrollStateChanged(int moveDirection){
        if(onSlideChangeListener!=null){
            onSlideChangeListener.onSlideScrollStateChanged(moveDirection);
        }
    }
    /**选中某一页*/
    public void slideSelected(Object obj){
        if(onSlideChangeListener!=null){
            onSlideChangeListener.onSelected(obj);
        }
    }

    public SlideAdapter getAdapter() {
        return adapter;
    }

    public interface CenterClickListener{
        void onClick(MotionEvent event);
    }

    public interface OnSlideChangeListener{
        void onSlideScrollStateChanged(int touchResult);
        void onSelected(Object obj);
    }
}
