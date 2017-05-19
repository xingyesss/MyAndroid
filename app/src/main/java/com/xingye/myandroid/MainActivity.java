package com.xingye.myandroid;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.xingye.myandroid.utils.DensityUtil;
import com.xingye.myandroid.utils.SystemUtil;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private GestureDetector detector;
    private int leftToggle = 0;
    private int rightToggle = 0;
    private Animation leftInAnimation, leftOutAnimation, rightInAnimation, rightOutAnimation;
    private ViewFlipper vf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.tv);
        vf = (ViewFlipper) findViewById(R.id.vf);
        int width = SystemUtil.getWindowWidth();
        int height = SystemUtil.getWindowHeight();

        leftToggle = width / 6;
        rightToggle = width * 5 / 6;
        leftInAnimation = AnimationUtils.loadAnimation(this, R.anim.left_in);
        leftOutAnimation = AnimationUtils.loadAnimation(this, R.anim.left_out);
        rightInAnimation = AnimationUtils.loadAnimation(this, R.anim.right_in);
        rightOutAnimation = AnimationUtils.loadAnimation(this, R.anim.right_out);


        String txt = "<img src='" + R.mipmap.ic_launcher + "' width='12' height='12'/><font color='#ccc'>你好啊</font>、" +
                "<font color='#FAC'>啦啦啦</font>、<font color='#AAA'>你好，你师父看到你的咖啡店百分比法规把房东vadfnkafda分van减肥的v</font>";
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                int id = Integer.parseInt(source);
                Drawable d = getResources().getDrawable(id);
                d.setBounds(0, 0, 50, 50);
                return d;
            }
        };
        CharSequence charSequence = Html.fromHtml(txt, imageGetter, null);
        tv.setText(charSequence);

        vf.setInAnimation(this, R.anim.in_right_left);
        //vf.setOutAnimation(this,R.anim.out_right_left);
        vf.setOutAnimation(this, R.anim.hold);
        //vf.startFlipping();

        vf.setOnTouchListener(this);

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
                if (e1.getX() - e2.getX() > 120) {
                    vf.setInAnimation(leftInAnimation);
                    vf.setOutAnimation(leftOutAnimation);
                    vf.showNext();
                    return true;
                } else if (e1.getX() - e2.getY() < 120) {
                    vf.setInAnimation(rightInAnimation);
                    vf.setOutAnimation(rightOutAnimation);
                    vf.showPrevious();
                    return true;
                }
                return false;
            }
        });

    }
    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.detector.onTouchEvent(event);
    }*/

    private float touchDownX;
    private float touchUpX;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 取得左右滑动时手指按下的X坐标
            touchDownX = event.getX();
            return true;
        } else if(event.getAction() == MotionEvent.ACTION_UP) {
            // 取得左右滑动时手指松开的X坐标
            touchUpX = event.getX();
            // 从左往右，看前一个View
            if (touchUpX - touchDownX > 100) {
                // 显示上一屏动画
                vf.setInAnimation(rightInAnimation);
                vf.setOutAnimation(rightOutAnimation);
                // 显示上一屏的View
                vf.showPrevious();
                // 从右往左，看后一个View
            } else if
                    (touchDownX - touchUpX > 100) {
                //显示下一屏的动画
                vf.setInAnimation(leftInAnimation);
                vf.setOutAnimation(leftOutAnimation);
                // 显示下一屏的View
                vf.showNext();
            }
            return true;
        }
        return false;
    }
}

