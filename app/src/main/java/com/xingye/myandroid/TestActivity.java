package com.xingye.myandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.xingye.myandroid.book.slider.MySlider;

/**
 * Created by sunxiquan on 2017/5/16.
 */

public class TestActivity extends AppCompatActivity {

    private MySlider mySlider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mySlider = (MySlider) findViewById(R.id.myslide);
        /*mySlider.setCenterClickListener(new MySlider.CenterClickListener() {
            @Override
            public void onCenterClick(MotionEvent event) {
                Toast.makeText(TestActivity.this, "中间被点击了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLeftClick(MotionEvent event) {
                Toast.makeText(TestActivity.this, "左边被点击了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightClick(MotionEvent event) {
                Toast.makeText(TestActivity.this, "右边被点击了", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public void start(View view) {
        mySlider.startMove();
    }

    public void stop(View view) {
        mySlider.stopMove();
    }

    public void startRightMove(View view) {
        mySlider.startRightMove();

    }
}
