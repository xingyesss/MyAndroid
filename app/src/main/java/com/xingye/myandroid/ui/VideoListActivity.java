package com.xingye.myandroid.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.xingye.myandroid.Contants;
import com.xingye.myandroid.R;
import com.xingye.myandroid.adapter.VideoListAdapter;
import com.xingye.myandroid.bean.Video;
import com.xingye.myandroid.db.DbManager;
import com.xingye.myandroid.utils.DensityUtil;
import com.xingye.myandroid.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunxiquan on 2017/5/19.
 */

public class VideoListActivity extends AppCompatActivity {
    private LRecyclerView lRecyclerView;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private boolean isRefresh = false;
    private VideoListAdapter adapter;
    private VideoListActivity context;
    private String[] paths = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        lRecyclerView = (LRecyclerView) findViewById(R.id.lRecylerView);
        context = this;
        init();
    }

    private void init() {
        adapter = new VideoListAdapter(context);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerView.setAdapter(lRecyclerViewAdapter);
        lRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        lRecyclerView.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(context, 10)));
        lRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        lRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

        lRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                requestData();
                //insertData();
            }
        });
        lRecyclerView.refresh();
        lRecyclerView.setLoadMoreEnabled(false);


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
                final List<Video> list = DbManager.getInstance(context).queryVideoList();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isRefresh && list!=null && list.size()>0) {
                            adapter.addAll(list);
                            lRecyclerView.refreshComplete(list.size());
                            isRefresh = false;
                        }
                    }
                });

            }
        }.start();

    }
}
