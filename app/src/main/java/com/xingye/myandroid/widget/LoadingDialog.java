package com.xingye.myandroid.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xingye.myandroid.R;

/**
 * Created by sunxiquan on 2017/3/22.
 */

public class LoadingDialog extends Dialog {
    private View mView;
    private TextView tv;
    private Context context;
    private String text;
    private int ResId = -1;
    public LoadingDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_loading,null);
        setContentView(mView);
        this.tv = (TextView) mView.findViewById(R.id.tv);

        if(!TextUtils.isEmpty(text)){
            this.tv.setText(text);
        }else if(ResId!=-1){
            this.tv.setText(ResId);
        }else{
            this.tv.setText(getContext().getResources().getString(R.string.loadinNow));
        }

        //LinearLayout ll = (LinearLayout) this.findViewById(R.id.ll_parent);
        //ll.getBackground().setAlpha(210);
        setCanceledOnTouchOutside(false);
    }

    public void setText(String text){
        this.text = text;

    }

    public void setText(int resId){
        this.ResId = resId;

    }

}