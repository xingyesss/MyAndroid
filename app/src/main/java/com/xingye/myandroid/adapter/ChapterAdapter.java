package com.xingye.myandroid.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xingye.myandroid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunxiquan on 2017/5/15.
 */

public class ChapterAdapter extends BaseAdapter {
    private List<String> mList;
    private Activity context;
    private ViewHolder holder;

    public ChapterAdapter(Activity context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chapter,parent,false);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView;
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(mList.get(position));
        return convertView;
    }

    public void addAll(List<String> list){
        if(list!=null){
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    private class ViewHolder{
        public TextView tv;
    }
}
