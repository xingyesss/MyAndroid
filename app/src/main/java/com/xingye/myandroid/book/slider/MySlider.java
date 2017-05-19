package com.xingye.myandroid.book.slider;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.xingye.myandroid.utils.SystemUtil;

/**
 * Created by sunxiquan on 2017/5/16.
 */

public class MySlider extends ViewGroup {
    private static final String TAG = "TAG";
    //默认当前为第0页
    private int curScreen = 0;
    private Scroller mScroller;
    private int screenWidth = 0;
    private int screenHeight = 0;

    public MySlider(Context context) {
        super(context);
        init();
    }

    private void init() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mScroller = new Scroller(getContext());
        LinearLayout ll = new LinearLayout(getContext());
        ll.setBackgroundColor(Color.BLUE);
        TextView tv = new TextView(getContext());
        tv.setLayoutParams(params);
        tv.setText("我是第一页");
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(22);
        tv.setTextColor(Color.WHITE);
        ll.addView(tv);
        addView(ll);

        LinearLayout ll1 = new LinearLayout(getContext());
        ll1.setBackgroundColor(Color.DKGRAY);
        tv = new TextView(getContext());
        tv.setLayoutParams(params);
        tv.setText("我是第二页");
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(22);
        tv.setTextColor(Color.WHITE);
        ll1.addView(tv);
        addView(ll1);

        LinearLayout ll2 = new LinearLayout(getContext());
        ll2.setBackgroundColor(Color.GREEN);
        tv = new TextView(getContext());
        tv.setLayoutParams(params);
        tv.setText("我是第三页");
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(22);
        tv.setTextColor(Color.WHITE);
        ll2.addView(tv);
        addView(ll2);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        screenWidth = SystemUtil.getWindowWidth();
        screenHeight = SystemUtil.getWindowHeight();
    }

    public MySlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void startMove() {
        if (curScreen < getChildCount()) {
            curScreen++;
            Log.d("TAG", "start move" + curScreen);
            mScroller.startScroll((curScreen - 1) * getWidth(), 0, getWidth(), 0, 1000);
            invalidate();
        }
    }

    public void slideNext(){

    }

    public void startRightMove() {
        if (curScreen != 0) {
            curScreen--;
            Log.d("TAG", "start right move");
            mScroller.startScroll((curScreen + 1) * getWidth(), 0, -getWidth(), 0, 1000);
            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        Log.e("TAG", "computeScroll");
        if (mScroller.computeScrollOffset()) {
            Log.e("TAG", mScroller.getCurrX() + "===" + mScroller.getCurrY());
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            Log.e("TAG", "左偏移量:" + getLeft() + "====右偏移量:" + getRight());

            //此时同样需要刷新view，否则效果有可能有误差
            postInvalidate();
        } else {
            Log.d("TAG", "scroller have done");
        }
    }

    //停止移动，如果已经超过下一屏一半，强制滑到下一屏
    public void stopMove() {
        Log.d("TAG", "------stop move-----");
        if (mScroller != null) {
            if (!mScroller.isFinished()) {
                int scrollCurX = mScroller.getCurrX();
                //判断是否超过下一屏的中间位置，如果达到就抵达下一屏，否则退回原屏幕
                int descScreen = (scrollCurX + getWidth() / 2) / getWidth();
                Log.i(TAG, "-mScroller.is not finished scrollCurX +" + scrollCurX);
                Log.i(TAG, "-mScroller.is not finished descScreen +" + descScreen);
                mScroller.abortAnimation();

                //停止动画，马上滑到目标位置
                scrollTo(descScreen * getWidth(), 0);
                curScreen = descScreen;
            } else {
                Log.i(TAG, "stopMove: is finished");
            }
        }
    }


    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SCROLLING = 1;
    private int mTouchState = TOUCH_STATE_REST;

    //处理触摸事件 ~
    public static int SNAP_VELOCITY = 600;
    private int mTouchSlop = 0;
    private float mLastionMotionX = 0;
    private float mLastMotionY = 0;
    //处理触摸的速率
    private VelocityTracker mVelocityTracker = null;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_MOVE && mTouchState != TOUCH_STATE_REST) {
            return true;
        }

        float x = ev.getX();
        float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastionMotionX = x;
                mLastMotionY = y;
                mTouchState = mScroller.isFinished()?TOUCH_STATE_REST:TOUCH_STATE_SCROLLING;
                break;
            case MotionEvent.ACTION_MOVE:
                int xDeff = (int) Math.abs(mLastionMotionX - x);
                if(xDeff>mTouchSlop){
                    mTouchState = TOUCH_STATE_SCROLLING;
                }
                break;
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //如果屏幕的动画还没结束，你就按下了，我们就结束该动画
                if(mScroller!=null){
                    if(!mScroller.isFinished()){
                        mScroller.abortAnimation();
                    }
                }
                mLastionMotionX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                int detaX = (int) (mLastionMotionX - x);
                scrollBy(detaX,0);
                mLastionMotionX = x;
                break;
            case MotionEvent.ACTION_UP:
                VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int velocityX = (int) velocityTracker.getXVelocity();

                //滑动速率达到了一个标准(快速向右滑屏，返回上一个屏幕) 马上进行切屏处理
                if(velocityX>SNAP_VELOCITY && curScreen>0){
                    Log.d("TAG", "snap left");
                    snapToScreen(curScreen-1,true);
                }else if(velocityX<-SNAP_VELOCITY && (curScreen<getChildCount()-1)){
                    Log.d("TAG", "snap right");
                    snapToScreen(curScreen+1,true);

                }//以上是快速滑动
                else{
                    snapToDestination();
                }

                if(mVelocityTracker!=null){
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }

                mTouchState = TOUCH_STATE_REST;


                break;
            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_REST;
                break;

        }
        return true;
    }
    //缓慢移动
    private void snapToDestination() {
        //当前的偏移位置
        int scrollX = getScrollX() ;
        int scrollY = getScrollY() ;

        Log.e(TAG, "### onTouchEvent snapToDestination ### scrollX is " + scrollX);

        //判断是否超过下一屏的中间位置，如果达到就抵达下一屏，否则保持在原屏幕
        //直接使用这个公式判断是哪一个屏幕 前后或者自己
        //判断是否超过下一屏的中间位置，如果达到就抵达下一屏，否则保持在原屏幕
        // 这样的一个简单公式意思是：假设当前滑屏偏移值即 scrollCurX 加上每个屏幕一半的宽度，除以每个屏幕的宽度就是
        //  我们目标屏所在位置了。 假如每个屏幕宽度为320dip, 我们滑到了500dip处，很显然我们应该到达第二屏
        int destScreen = (getScrollX() + getWidth() / 2 ) / getWidth() ;


        Log.e(TAG, "### onTouchEvent  ACTION_UP### dx destScreen " + destScreen);

        snapToScreen(destScreen,false);
    }

    private void snapToScreen(int whichScreen,boolean isFast) {
        curScreen = whichScreen ;

        if(curScreen > getChildCount() - 1)
            curScreen = getChildCount() - 1 ;
        int time = isFast?500:1500;
        int dx = curScreen*getWidth() - getScrollX() ;

        Log.e(TAG, "### onTouchEvent  ACTION_UP### dx is " + dx);
        //Math.abs(dx) * 2
        mScroller.startScroll(getScrollX(), 0, dx, 0,time*Math.abs(dx)/screenWidth);
        mScroller.startScroll(0,0,0,12);

        //此时需要手动刷新View 否则没效果
        invalidate();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            //设置每个视图的大小为全屏
            child.measure(getWidth(), SystemUtil.getWindowHeight());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int startLeft = 0;
        int startTop = 10;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                child.layout(startLeft, startTop, startLeft + getWidth(), startTop + SystemUtil.getWindowHeight());
                startLeft += getWidth();
            }
        }
        //绘制三个布局的位置[0,1080],[1080,2160],[2160,3240]
    }

    public interface CenterClickListener {
        void onCenterClick(MotionEvent event);

        void onLeftClick(MotionEvent event);

        void onRightClick(MotionEvent event);
    }

}
