package com.xingye.myandroid.book.slider;

import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.xingye.myandroid.book.SlideLayout;

/**
 * Created by sunxiquan on 2017/5/16.
 */

public class PageSlider extends BaseSlider {
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private int mVelocityValue = 0;

    /**
     * 滑动的有效距离
     */
    private int limitDistance = 0;
    /**
     * 屏幕宽度
     */
    private int screenWidth = 0;
    /**
     * 最后触摸的方向
     */
    private int mTouchResult = MOVE_NO_RESULT;
    /**
     * 开始的方向
     */
    private int mDirection = MOVE_NO_RESULT;
    private int mMode = MODE_NONE;

    private boolean mMoveLastPage, mMoveFirstPage;

    private int startX = 0;
    /**
     * 滑动的view
     */
    private View mLeftScrollerView = null;
    private View mRightScrollerView = null;

    private SlideLayout mSlideLayout;

    public SlideAdapter getAdapter() {
        return mSlideLayout.getAdapter();
    }

    @Override
    public void init(SlideLayout slideLayout) {
        mSlideLayout = slideLayout;
        mScroller = new Scroller(slideLayout.getContext());
        screenWidth = slideLayout.getContext().getResources().getDisplayMetrics().widthPixels;
        //设置有效距离为屏幕的1/3宽度
        limitDistance = screenWidth / 3;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        obtainVelocitracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    break;
                }
                startX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()) {
                    return false;
                }
                if (startX == 0) {
                    startX = (int) event.getX();
                }

                final int distance = (int) (startX - event.getX());
                if (mDirection == MOVE_NO_RESULT) {
                    if (distance > 0) {
                        mDirection = MOVE_TO_LEFT;
                        mMoveLastPage = !getAdapter().hasNext();
                        mMoveFirstPage = false;

                        mSlideLayout.slideScrollStateChanged(MOVE_TO_LEFT);
                    } else if (distance < 0) {
                        mDirection = MOVE_TO_RIGHT;
                        mMoveFirstPage = !getAdapter().hasPrevious();
                        mMoveLastPage = false;
                        mSlideLayout.slideScrollStateChanged(MOVE_TO_RIGHT);
                    }
                }
                //当在移动时，改变模式为在移动
                if(mMode == MODE_NONE && ((mDirection == MOVE_TO_LEFT) || (mDirection == MOVE_TO_RIGHT))){
                    mMode = MODE_MOVE;
                }

                if(mMode == MODE_MOVE){
                    if((mDirection == MOVE_TO_LEFT && distance<=0) || (mDirection == MOVE_TO_RIGHT && mDirection>=0)){
                        mMode = MODE_NONE;
                    }
                }

                if(mDirection!=MOVE_NO_RESULT){
                    if(mDirection == MOVE_TO_LEFT){
                        mLeftScrollerView = getCurrentShowView();
                        if(!mMoveLastPage)
                            mRightScrollerView = getBottomView();
                        else
                            mRightScrollerView = null;
                    }else{
                        mRightScrollerView = getCurrentShowView();
                        if(!mMoveFirstPage){
                            mLeftScrollerView = getTopView();
                        }else
                            mLeftScrollerView = null;
                    }

                    if(mMode == MODE_MOVE){
                        mVelocityTracker.computeCurrentVelocity(1000, ViewConfiguration.getMaximumFlingVelocity());
                        if(mDirection == MOVE_TO_LEFT){
                            if(mMoveLastPage){
                                mLeftScrollerView.scrollTo(distance/2,0);
                            }else {
                                mLeftScrollerView.scrollTo(distance,0);
                                mRightScrollerView.scrollTo(-screenWidth+distance,0);
                            }
                        }else{
                            if(mMoveFirstPage){
                                mRightScrollerView.scrollTo(distance/2,0);
                            }else{
                                mLeftScrollerView.scrollTo(screenWidth+distance,0);
                                mRightScrollerView.scrollTo(distance,0);
                            }
                        }

                    }else{
                        int scrollX = 0;
                        if(mLeftScrollerView!=null){
                            scrollX = mLeftScrollerView.getScrollX();
                        }else if(mRightScrollerView != null){
                            scrollX = mRightScrollerView.getScrollX();
                        }

                        if(mDirection == MOVE_TO_LEFT && scrollX!=0 && getAdapter().hasNext()){
                            mLeftScrollerView.scrollTo(0,0);
                            if(mRightScrollerView!=null)
                                mRightScrollerView.scrollTo(screenWidth,0);
                        }else if(mDirection == MOVE_TO_RIGHT && getAdapter().hasPrevious() && screenWidth!= Math.abs(scrollX)){
                            if(mLeftScrollerView!=null){
                                mLeftScrollerView.scrollTo(-screenWidth,0);
                            }

                            mRightScrollerView.scrollTo(0,0);
                        }
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if(mLeftScrollerView == null && mDirection == MOVE_TO_LEFT){
                    //第一页
                    return false;
                }
                if(mRightScrollerView == null && mDirection == MOVE_TO_RIGHT){
                    //最后一页
                    return false;
                }

                int time = 500;

                if(mMoveFirstPage && mRightScrollerView!=null){
                    final int rscrollx = mRightScrollerView.getScrollX();
                    mScroller.startScroll(rscrollx,0,-rscrollx,0,time*Math.abs(rscrollx)/screenWidth);
                    mTouchResult = MOVE_NO_RESULT;
                }

                if(mMoveLastPage && mLeftScrollerView !=null){
                    final int lscrollX = mLeftScrollerView.getScrollX();
                    mScroller.startScroll(lscrollX, 0, -lscrollX, 0, time * Math.abs(lscrollX)/screenWidth);
                    mTouchResult = MOVE_NO_RESULT;
                }

                if (!mMoveLastPage && !mMoveFirstPage && mLeftScrollerView != null) {
                    final int scrollX = mLeftScrollerView.getScrollX();
                    mVelocityValue = (int) mVelocityTracker.getXVelocity();

                    if(mMode == MODE_MOVE && mDirection == MOVE_TO_LEFT){
                        if(scrollX>limitDistance || mVelocityValue < -time){
                            //手指向左移动，可以翻屏幕
                            mTouchResult = MOVE_TO_LEFT;
                            if(mVelocityValue<-time){
                                int tmpTime = 1000*1000/Math.abs(mVelocityValue);
                                time = tmpTime>500?500:tmpTime;
                            }

                            mScroller.startScroll(scrollX,0,screenWidth-scrollX,0,time);
                        }else{
                            mTouchResult = MOVE_NO_RESULT;
                            mScroller.startScroll(scrollX,0,-scrollX,0,time);
                        }
                    }else if(mMode == MODE_MOVE && mDirection == MOVE_TO_RIGHT){
                        if((screenWidth-scrollX)>limitDistance|| mVelocityValue>time){
                            //手指向右移动，可以翻屏幕
                            mTouchResult = MOVE_TO_RIGHT;
                            if(mVelocityValue>time){
                                int tmpTime = 1000*1000/Math.abs(mVelocityValue);
                                time = tmpTime>500?500:tmpTime;
                            }
                            mScroller.startScroll(scrollX,0,-scrollX,0,time);
                        }else {
                            mTouchResult = MOVE_NO_RESULT;
                            mScroller.startScroll(scrollX,0,screenWidth-scrollX,0,time);
                        }
                    }
                }
                resetVariables();
                invalidate();
                break;
        }
        return false;
    }

    private void resetVariables() {
        mDirection = MOVE_NO_RESULT;
        mMode = MODE_NONE;
        startX = 0;
        releaseVelocityTracker();
    }

    private void releaseVelocityTracker() {
        if(mVelocityTracker!=null){
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void invalidate() {
        mSlideLayout.postInvalidate();
    }

    private boolean moveToNext(){
        if(!getAdapter().hasNext()){
            return false;
        }
        View prevView = getAdapter().getPreviousView();
        if(prevView!=null){
            mSlideLayout.removeView(prevView);
        }
        View newNextView = prevView;

        getAdapter().moveToNext();

        mSlideLayout.slideSelected(getAdapter().getCurrent());

        if(getAdapter().hasNext()){
            if(newNextView!=null){
                View updateNextView = getAdapter().getView(newNextView,getAdapter().getNext());
                if(updateNextView!=newNextView){
                    getAdapter().setNextView(updateNextView);
                    newNextView = updateNextView;
                }
            }else{
                newNextView = getAdapter().getNextView();
            }

            newNextView.scrollTo(-screenWidth,0);
            mSlideLayout.addView(newNextView);
        }
        return true;

    }

    private boolean moveToPreVious(){
        if(!getAdapter().hasPrevious()){
            return false;
        }

        View nextView = getAdapter().getNextView();
        if(nextView!=null){
            mSlideLayout.removeView(nextView);
        }
        View newPrevView = nextView;

        getAdapter().moveToPrevious();

        mSlideLayout.slideSelected(getAdapter().getCurrent());
        if(getAdapter().hasPrevious()){
            if(newPrevView!=null){
               View updatePrevView = getAdapter().getView(newPrevView,getAdapter().getPrevious());
                if(newPrevView!=updatePrevView){
                    getAdapter().setPreviousView(updatePrevView);
                    newPrevView = updatePrevView;
                }
            }else{
                newPrevView = getAdapter().getPreviousView();
            }

            newPrevView.scrollTo(screenWidth,0);
            mSlideLayout.addView(newPrevView);

        }

        return true;

    }

    private View getTopView() {
        return getAdapter().getPreviousView();
    }

    private View getBottomView() {
        return getAdapter().getNextView();
    }

    private View getCurrentShowView() {
        return getAdapter().getCurrentView();
    }

    private void obtainVelocitracker(MotionEvent event) {
        if(mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            if(mLeftScrollerView!=null){
                mLeftScrollerView.scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            }

            if(mRightScrollerView!=null){
                if(mMoveFirstPage){
                    mRightScrollerView.scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
                }else {
                    mRightScrollerView.scrollTo(mScroller.getCurrX()-screenWidth,mScroller.getCurrY());
                }
            }

            invalidate();
        }else if(mScroller.isFinished()){
            if(mTouchResult!=MOVE_NO_RESULT){
                if (mTouchResult == MOVE_TO_LEFT) {
                    moveToNext();
                } else {
                    moveToPreVious();
                }
                mTouchResult = MOVE_NO_RESULT;

                mSlideLayout.slideScrollStateChanged(MOVE_NO_RESULT);

                invalidate();
            }
        }
    }

    @Override
    public void slideNext() {
        if(!getAdapter().hasNext() || mScroller.isFinished()){
            return;
        }

        mLeftScrollerView = getCurrentShowView();
        mRightScrollerView = getBottomView();

        mScroller.startScroll(0,0,screenWidth,0,500);
        mTouchResult = MOVE_TO_LEFT;

        mSlideLayout.slideScrollStateChanged(MOVE_TO_LEFT);
        invalidate();
    }

    @Override
    public void slidePrevious() {
        if (!getAdapter().hasPrevious() || !mScroller.isFinished())
            return;

        mLeftScrollerView = getTopView();
        mRightScrollerView = getCurrentShowView();

        mScroller.startScroll(screenWidth, 0, -screenWidth, 0, 500);
        mTouchResult = MOVE_TO_RIGHT;

        mSlideLayout.slideScrollStateChanged(MOVE_TO_RIGHT);

        invalidate();
    }

    @Override
    public void resetFromAdapter() {
        View curView = getAdapter().getUpdatedCurrentView();
        mSlideLayout.addView(curView);
        curView.scrollTo(0, 0);

        if (getAdapter().hasPrevious()) {
            View prevView = getAdapter().getUpdatedPreviousView();
            mSlideLayout.addView(prevView);
            prevView.scrollTo(screenWidth, 0);
        }

        if (getAdapter().hasNext()) {
            View nextView = getAdapter().getUpdatedNextView();
            mSlideLayout.addView(nextView);
            nextView.scrollTo(-screenWidth, 0);
        }

        mSlideLayout.slideSelected(getAdapter().getCurrent());
    }
}
