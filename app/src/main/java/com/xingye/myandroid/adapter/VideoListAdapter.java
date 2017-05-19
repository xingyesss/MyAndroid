package com.xingye.myandroid.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xingye.myandroid.R;
import com.xingye.myandroid.bean.Video;
import com.xingye.myandroid.holder.VideoViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by sunxiquan on 2017/5/19.
 */

public class VideoListAdapter extends ListBaseAdapter<Video> {

    public VideoListAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_video_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        VideoViewHolder holder = (VideoViewHolder) viewHolder;
        Video video = mDataList.get(position);
        holder.tvTitle.setText(video.getContent());
        holder.videoplayer.setUp(video.getVideoPath(),JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"");
        holder.videoplayer.thumbImageView.setImageResource(R.drawable.pic_default);
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<10;i++){
            if(i!=9) {
                sb.append("用户" + i+"、");
            }else{
                sb.append("用户" + i);
            }
        }
        holder.tvAdmire.setMovementMethod(LinkMovementMethod.getInstance());
        String imgHtml = "<img src='"+R.drawable.disadmire+"' />";
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                int id = Integer.parseInt(source);
                Drawable d= mContext.getResources().getDrawable(id);
                d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
                return d;
            }
        };
        holder.tvAdmire.setText(addClickablePart(sb.toString()));
    }


    private SpannableStringBuilder addClickablePart(String str){
        //第一个赞图标
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.disadmire);
        drawable.setBounds(0,0,35,35);
        ImageSpan span = new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE);
        SpannableString spanStr = new SpannableString("p.");
        spanStr.setSpan(span,0,1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder ssb = new SpannableStringBuilder(spanStr);
        ssb.append(str);
        String[] likeUsers = str.split("、");
        if(likeUsers.length>0){
            //最后一个
            for(int i=0;i<likeUsers.length;i++){
                final String name = likeUsers[i];
                final int start = str.indexOf(name)+spanStr.length();
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(widget.getContext(), name, Toast.LENGTH_SHORT).show();
                        Log.e("TAG", name+"被点击了");
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(mContext.getResources().getColor(R.color.tv_blue2));
                        //ds.setColor(Color.BLUE);
                        //去掉下划线
                        ds.setUnderlineText(false);
                    }
                },start,start+name.length(),0);
            }
        }
        return ssb.append("等"+likeUsers.length+"个人赞了您.");
    }

}
