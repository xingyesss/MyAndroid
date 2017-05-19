package com.xingye.myandroid.book.slider;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.xingye.myandroid.book.SlideLayout;

/**
 * Created by sunxiquan on 2017/5/16.
 */

public abstract class SlideAdapter<T> {
    private View[] mView;
    private int currentViewIndex;
    private SlideLayout slideLayout;


    public SlideAdapter() {
        mView = new View[3];
        currentViewIndex = 0;
    }

    public void setSlideLayout(SlideLayout slideLayout) {
        this.slideLayout = slideLayout;
    }

    public View getUpdatedCurrentView(){
        View curView = mView[currentViewIndex];
        if(curView == null){
            curView = getView(null,getCurrent());
            mView[currentViewIndex] = curView;
        }else{
            View updateView = getView(curView,getCurrent());
            if(curView!=updateView){
                curView = updateView;
                mView[currentViewIndex] = updateView;
            }
        }
        return curView;
    }

    public View getCurrentView(){
        View curView = mView[currentViewIndex];
        if(curView == null){
            curView = getView(null,getCurrent());
            mView[currentViewIndex] = curView;
        }

        return curView;
    }

    public View getView(int index){
        return mView[(index+3)%3];
    }

    private void setView(int index,View view){
        mView[(index+3)%3] = view;
    }

    public View getUpdatedNextView(){
        View nextView = getView(currentViewIndex+1);
        boolean hasNext = hasNext();
        if(nextView == null && hasNext){
            nextView = getView(null,getNext());
            setView(currentViewIndex+1,nextView);
        }else if(hasNext){
            View updatedView = getView(nextView,getNext());
            if(updatedView!=nextView){
                nextView = updatedView;
                setView(currentViewIndex+1,nextView);
            }
        }
        return nextView;
    }

    public View getNextView(){
        View nextView = getView(currentViewIndex+1);
        if(nextView == null && hasNext()){
            nextView = getView(null,getNext());
            setView(currentViewIndex+1,nextView);
        }
        return nextView;
    }

    public View getUpdatedPreviousView(){
        View prevView = getView(currentViewIndex-1);
        boolean hasPrev = hasPrevious();
        if(prevView == null && hasPrev){
            prevView = getView(null,getPrevious());
            setView(currentViewIndex-1,prevView);
        }else if(hasPrev){
            View updatedView = getView(prevView,getPrevious());
            if(updatedView!=prevView){
                prevView = updatedView;
                setView(currentViewIndex-1,prevView);
            }
        }
        return prevView;
    }

    public void setPreviousView(View view){
        setView(currentViewIndex-1,view);
    }

    public void setNextView(View view){
        setView(currentViewIndex+1,view);
    }

    public void setCurrentView(View view){
        setView(currentViewIndex,view);
    }

    public View getPreviousView(){
        View prevView = getView(currentViewIndex-1);
        if(prevView == null && hasPrevious()){
            prevView = getView(null,getPrevious());
            setView(currentViewIndex-1,prevView);
        }

        return prevView;
    }

    public void moveToNext(){
        computeNext();
        currentViewIndex = (currentViewIndex+1)%3;
    }

    public void moveToPrevious(){
        computePrevious();
        currentViewIndex = (currentViewIndex+2) %3;
    }

    public abstract View getView(View contentView, T t);


    public abstract T getCurrent();

    public abstract T getNext();

    public abstract T getPrevious();

    public abstract boolean hasNext();

    public abstract  boolean hasPrevious();

    protected abstract void computeNext();

    protected abstract void computePrevious();

    public Bundle saveState(){
        return null;
    }

    public void restoreState(Parcelable parcelable){
        currentViewIndex = 0;
        if(mView!=null){
            for(int i=0;i<mView.length;i++){
                mView[i] = null;
            }
        }
    }

    public void notifyDataSetChanged(){
        if(slideLayout!=null){
            slideLayout.resetFromAdapter();
            slideLayout.postInvalidate();
        }
    }
}
