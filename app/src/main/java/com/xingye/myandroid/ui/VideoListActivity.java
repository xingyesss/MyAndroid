package com.xingye.myandroid.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.Toast;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.xingye.myandroid.Contants;
import com.xingye.myandroid.MyApplication;
import com.xingye.myandroid.R;
import com.xingye.myandroid.adapter.VideoListAdapter;
import com.xingye.myandroid.bean.Video;
import com.xingye.myandroid.db.DbManager;
import com.xingye.myandroid.utils.DensityUtil;
import com.xingye.myandroid.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCUtils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by sunxiquan on 2017/5/19.
 */

public class VideoListActivity extends AppCompatActivity {
    private LuRecyclerView lRecyclerView;
    private LuRecyclerViewAdapter lRecyclerViewAdapter;
    private boolean isRefresh = false;
    private VideoListAdapter adapter;
    private VideoListActivity context;
    private String[] paths = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    public int firstVisible = 0,visibleCount = 0,totalCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        lRecyclerView = (LuRecyclerView) findViewById(R.id.lRecylerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        context = this;
        init();
    }

    private void init() {
        adapter = new VideoListAdapter(context);
        lRecyclerViewAdapter = new LuRecyclerViewAdapter(adapter);
        lRecyclerView.setAdapter(lRecyclerViewAdapter);
        lRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        lRecyclerView.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(context, 10)));
        /*lRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        lRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

        lRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                requestData();
                //insertData();
            }
        });
        lRecyclerView.refresh();*/
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
        android.R.color.holo_red_light,android.R.color.holo_orange_light,android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                requestData();
            }
        });

        lRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                isRefresh = true;
                loadingData();
            }
        });

        refreshUI();
        lRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch(newState){
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        autoPlayVideo(recyclerView);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LuRecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if(layoutManager instanceof LinearLayoutManager){
                    LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                    firstVisible = manager.findFirstVisibleItemPosition();
                    visibleCount = manager.findLastVisibleItemPosition()-firstVisible;
                    Log.d("TAG", "firstVisible:"+firstVisible+"===="+visibleCount);
                }
            }
        });



    }

    private void loadingData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(1500);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lRecyclerView.refreshComplete(0);
                        lRecyclerView.setNoMore(true);
                    }
                });
            }
        }.start();
    }


    private void autoPlayVideo(RecyclerView view) {
        for(int i=0;i<visibleCount;i++){
            if(view!=null && view.getChildAt(i)!=null && view.getChildAt(i).findViewById(R.id.videoplayer)!=null){
                JCVideoPlayerStandard videoPlayerStandard = (JCVideoPlayerStandard) view.getChildAt(i).findViewById(R.id.videoplayer);
                Rect rect = new Rect();
                videoPlayerStandard.getLocalVisibleRect(rect);
                int videoHeight = videoPlayerStandard.getHeight();
                if(rect.top == 0 && rect.bottom == videoHeight){
                    if(videoPlayerStandard.currentState == JCVideoPlayer.CURRENT_STATE_NORMAL
                            || videoPlayerStandard.currentState == JCVideoPlayer.CURRENT_STATE_ERROR){
                        if(JCUtils.isWifiConnected(this)) {
                            videoPlayerStandard.startButton.performClick();
                            MyApplication.mApp.videoPlaying = videoPlayerStandard;
                        }
                    }
                    return;
                }
            }

        }
        JCVideoPlayer.releaseAllVideos();
        MyApplication.mApp.videoPlaying = null;
    }

    private void refreshUI() {
        isRefresh = true;
        swipeRefreshLayout.setRefreshing(true);
        requestData();
    }

    private void insertData() {
        List<Video> list = new ArrayList<>();
        for(int i=0;i< Contants.videoPath.length;i++){
            Video video = new Video();
            video.setUserName("小明");
            video.setContent(Contants.contents[i]);
            video.setVideoPath(Contants.videoPath[i]);
            list.add(video);
        }
        DbManager.getInstance(context).insertVideoList(list);
        Toast.makeText(context, "插入成功!", Toast.LENGTH_SHORT).show();
    }

    private void requestData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(1500);
                final List<Video> list = DbManager.getInstance(context).queryVideoList();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isRefresh && list!=null && list.size()>0) {
                            adapter.clear();
                            adapter.addAll(list);
                            lRecyclerView.refreshComplete(list.size());
                            swipeRefreshLayout.setRefreshing(false);
                            //swipeRefreshLayout.setEnabled(false);
                            isRefresh = false;
                        }
                    }
                });

            }
        }.start();

    }
}
