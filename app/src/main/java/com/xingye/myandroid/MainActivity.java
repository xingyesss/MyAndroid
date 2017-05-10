package com.xingye.myandroid;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.xingye.myandroid.utils.DensityUtil;
import com.xingye.myandroid.utils.SystemUtil;

public class MainActivity extends AppCompatActivity {
    private GestureDetector detector;
    private int leftToggle = 0;
    private int rightToggle = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.tv);
        ViewFlipper vf = (ViewFlipper) findViewById(R.id.vf);
        int width = SystemUtil.getWindowWidth();
        int height = SystemUtil.getWindowHeight();

        leftToggle = width/6;
        rightToggle = width*5/6;


        String txt = "<img src='"+R.mipmap.ic_launcher+"' width='12' height='12'/><font color='#ccc'>你好啊</font>、" +
                "<font color='#FAC'>啦啦啦</font>、<font color='#AAA'>你好，你师父看到你的咖啡店百分比法规把房东vadfnkafda分van减肥的v</font>";
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                int id = Integer.parseInt(source);
                Drawable d = getResources().getDrawable(id);
                d.setBounds(0,0,50,50);
                return d;
            }
        };
        CharSequence charSequence = Html.fromHtml(txt,imageGetter,null);
        tv.setText(charSequence);

        vf.setInAnimation(this,R.anim.in_right_left);
        //vf.setOutAnimation(this,R.anim.out_right_left);
        vf.setOutAnimation(this,R.anim.hold);
        //vf.startFlipping();

        vf.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return false;
            }
        });

        detector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                //单击
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                int x = (int) (e2.getX() - e1.getX());

                return false;
            }
        });

    }
}
