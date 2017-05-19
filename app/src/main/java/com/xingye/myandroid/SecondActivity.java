package com.xingye.myandroid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xingye.myandroid.adapter.ChapterAdapter;
import com.xingye.myandroid.widget.CustomView;
import com.xingye.myandroid.widget.FlipperLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sunxiquan on 2017/5/15.
 */

public class SecondActivity extends AppCompatActivity {

    private ListView lv;
    private ChapterAdapter adapter;
    //private ProgressBar bar;
    private LinearLayout llLoading;
    private Pattern p = null;
    private List<String> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        lv = (ListView) findViewById(R.id.lv);
        //bar = (ProgressBar) findViewById(R.id.bar);
        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        list = new ArrayList<>();

        adapter = new ChapterAdapter(this);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(SecondActivity.this,BookActivity.class));
            }
        });
        String regex = ".第.{1,7}章.{0,}";
        p = Pattern.compile(regex);

        new ChapterHandler().start();

    }

    private class ChapterHandler extends Thread {
        @Override
        public void run() {
            super.run();
            File file = new File(Environment.getExternalStorageDirectory()+"/tencent/QQfile_recv/太古神王.txt");
            if(!file.exists()){
                Toast.makeText(SecondActivity.this, "指定文件不存在!", Toast.LENGTH_SHORT).show();
                if(llLoading.getVisibility() == View.VISIBLE){
                    llLoading.setVisibility(View.INVISIBLE);
                }
                return;
            }
            FileInputStream fis = null;

            try {
                list.clear();
                fis = new FileInputStream(file);
                BufferedReader dr = new BufferedReader(new InputStreamReader(fis,"GBK"));
                String line = null;
                long offset = 0;
                while((line = dr.readLine())!=null){
                    Matcher m = p.matcher(line);
                    if(m.find()){
                        Log.d(offset+"",m.group());
                        list.add(m.group());
                    }
                    offset = offset+line.length()+2;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llLoading.setVisibility(View.INVISIBLE);
                        adapter.addAll(list);
                    }
                });
                dr.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
