package com.xingye.myandroid.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xingye.myandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by sunxiquan on 2017/5/19.
 */

public class VideoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_focus)
    public TextView tvFocus;
    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.videoplayer)
    public JCVideoPlayerStandard videoplayer;
    @BindView(R.id.tv_admire)
    public TextView tvAdmire;

    public VideoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


}
