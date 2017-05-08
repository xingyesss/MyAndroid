package com.xingye.myandroid.callback;

import android.app.Activity;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.request.BaseRequest;
import com.xingye.myandroid.R;
import com.xingye.myandroid.widget.LoadingDialog;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by sunxiquan on 2017/5/3.
 */

public abstract class DialogCallback<T> extends JsonCallback<T> {
    private LoadingDialog dialog;
    private int isfirst = 0;
    private Boolean firstLoad = true;   //只有第一次显示弹窗,默认为true


    public DialogCallback(Activity activity) {
        dialog = new LoadingDialog(activity);
        dialog.setText(R.string.loading);

        /*dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("请求网络中...");*/
    }

    public DialogCallback(Activity activity, Boolean firstShow) {
        dialog = new LoadingDialog(activity);
        dialog.setText(R.string.loading);
        this.firstLoad = firstShow;
    }

    public DialogCallback(Activity activity, String msg) {
        dialog = new LoadingDialog(activity);
        dialog.setText(msg);
    }

    public DialogCallback(Activity activity, int resId) {
        dialog = new LoadingDialog(activity);
        dialog.setText(resId);
    }

    public DialogCallback(Activity activity, String msg, Boolean isFirstShow) {
        dialog = new LoadingDialog(activity);
        dialog.setText(msg);
        this.firstLoad = isFirstShow;
    }

    public DialogCallback(Activity activity, int resId, boolean isFirstShow) {
        dialog = new LoadingDialog(activity);
        dialog.setText(resId);
        this.firstLoad = isFirstShow;
    }

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        //网络请求前显示对话框
        if (firstLoad) {
            firstLoad = false;
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    @Override
    public void onAfter(T t, Exception e) {
        super.onAfter(t, e);
        //网络请求结束后关闭对话框
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
