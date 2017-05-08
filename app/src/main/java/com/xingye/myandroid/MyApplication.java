package com.xingye.myandroid;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

/**
 * Created by sunxiquan on 2017/5/7.
 */

public class MyApplication extends Application {
    public static MyApplication mApp;
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        //分包处理
        MultiDex.install(this);

        initOkGo();
    }

    private void initOkGo() {
        OkGo.init(this);
        OkGo.getInstance()
                .addCommonHeaders(new HttpHeaders())
                .addCommonParams(new HttpParams());
    }
}
