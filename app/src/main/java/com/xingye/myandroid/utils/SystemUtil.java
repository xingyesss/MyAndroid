package com.xingye.myandroid.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.xingye.myandroid.MyApplication;
import com.xingye.myandroid.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sunxiquan on 2017/3/15.
 */

public class SystemUtil {
    private static final String YEAR = "yyyy";
    private static final String MONTH = "MM";
    private static final String DAY = "dd";
    private static final String HH = "HH";
    /**
     * 检测系统语言，自动切换中英文
     */
    public static void initSystemLanguage(Context context) {
        String able = context.getResources().getConfiguration().locale.getCountry();
        Configuration config = context.getResources().getConfiguration();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        if (TextUtils.equals(able.toUpperCase(), "CN")) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else {
            config.locale = Locale.ENGLISH;
        }
        context.getResources().updateConfiguration(config, dm);
    }

    /**
     * 是否是中文
     */
    public static boolean isChinese(Context context) {
        String able = context.getResources().getConfiguration().locale.getCountry();
        Configuration config = context.getResources().getConfiguration();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        if (TextUtils.equals(able.toUpperCase(), "CN")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 显示软键盘
     *
     * @param etContent
     * @param time
     */
    public static void showSoftKeyBorad(final EditText etContent, int time) {
        if (time == 0) {
            time = 200;
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) etContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etContent, 0);
            }
        }, time);
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoftBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && activity.getCurrentFocus() != null) {
            if (activity.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 隐藏应用到后台
     */
    public static void hideApp(Context context) {
        PackageManager pm = context.getPackageManager();
        ResolveInfo homeInfo =
                pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
        ActivityInfo ai = homeInfo.activityInfo;
        Intent startIntent = new Intent(Intent.ACTION_MAIN);
        startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
        startActivitySafely(context, startIntent);
    }

    private static void startActivitySafely(Context context, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "null",
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(context, "null",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取当前应用版本号
     *
     * @return 当前版本号
     */
    public static String getVersion() {
        try {
            PackageManager manager = MyApplication.mApp.getPackageManager();
            PackageInfo info = manager.getPackageInfo(MyApplication.mApp.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    /**
     * 获取屏幕的宽度
     *
     * @return
     */
    public static int getWindowWidth() {
        WindowManager windowManager = (WindowManager) MyApplication.mApp.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕的高度
     *
     * @return
     */
    public static int getWindowHeight() {
        WindowManager windowManager = (WindowManager) MyApplication.mApp.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getHeight();
    }

    /**
     * 格式化时间
     */
    public static String formatTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        return sdf.format(new Date(time));
    }

    public static String chatTime(long time){
        SimpleDateFormat sdf = null;
        Calendar cal = Calendar.getInstance();
        Date date = new Date(time);
        int year_t = Integer.parseInt(getFormatDate(time,YEAR));
        if(cal.get(Calendar.YEAR)-year_t< 0){
            sdf = new SimpleDateFormat("yy-MM-dd");
            return sdf.format(date);
        }
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int day_t = Integer.parseInt(getFormatDate(time,DAY));
        int month_t = Integer.parseInt(getFormatDate(time,MONTH));

        if(month_t < month){
            sdf = new SimpleDateFormat("MM-dd");
            return sdf.format(date);
        }else if(month_t == month && day_t<day-1){
            sdf = new SimpleDateFormat("MM-dd");
            return sdf.format(date);
        }
        if(month_t == month && day_t == day-1){
            return MyApplication.mApp.getResources().getString(R.string.yesterday);
        }

        sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    /**
     * 将时间转换成以秒为单位的时间戳
     */
    public static long transLongTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        try {
            Date date = format.parse(time);
            long value = date.getTime();
            return value / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public static void startPhotoZoom(Activity activity,Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, 111);
    }

    /**获取应用名称*/
    public static String getApplicationName(Context context){
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;

        try{
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(),0);
        }catch (Exception e){
            e.printStackTrace();
            applicationInfo = null;
        }

        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);

        return applicationName;
    }

    public static String getFormatDate(long time, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(time));
    }


}
