package com.xingye.myandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xingye.myandroid.book.SlideLayout;
import com.xingye.myandroid.book.slider.PageSlider;
import com.xingye.myandroid.book.slider.SlideAdapter;

/**
 * Created by sunxiquan on 2017/5/16.
 */

public class BookActivity extends AppCompatActivity {
    private BookActivity context;
    private SlideLayout slideLayout;
    private MySlidePageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        slideLayout = (SlideLayout) findViewById(R.id.slideLayout);
        slideLayout.setSlider(new PageSlider());
        adapter = new MySlidePageAdapter();
        slideLayout.setAdapter(adapter);
        slideLayout.setCenterClickListener(new SlideLayout.CenterClickListener() {
            @Override
            public void onClick(MotionEvent event) {
                Toast.makeText(context, "我被点击了", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private class MySlidePageAdapter extends SlideAdapter<TextView>{
        TextView tvNextPage;
        TextView tvPrevPage;

        @Override
        public View getView(View contentView, TextView textView) {
            if(contentView == null){
                contentView = LayoutInflater.from(context).inflate(R.layout.item_bookpage,null);
                RelativeLayout rl = (RelativeLayout) contentView.findViewById(R.id.page_holder);
                rl.removeAllViews();
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                rl.addView(textView,params);
            }
            return contentView;
        }

        @Override
        public TextView getCurrent() {
            TextView tv = new TextView(context);
            tv.setText("我是当前界面");
            tv.setTextSize(22);
            return tv;
        }

        @Override
        public TextView getNext() {
            tvNextPage = new TextView(context);
            tvNextPage.setText("我是最后一个界面");
            return tvNextPage;
        }

        @Override
        public TextView getPrevious() {
            tvPrevPage = new TextView(context);
            tvPrevPage.setText("我是前一个界面");
            return tvPrevPage;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public boolean hasPrevious() {
            return true;
        }

        @Override
        protected void computeNext() {

        }

        @Override
        protected void computePrevious() {

        }
    }
}
