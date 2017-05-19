package com.xingye.myandroid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ListBaseAdapter<T extends Entity> extends RecyclerView.Adapter {
    protected Context mContext;

    public ListBaseAdapter(Context mContext) {
        this.mContext = mContext;
    }

    protected ArrayList<T> mDataList = new ArrayList<>();

    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 获取当前集合
     * @return
     */
    public List<T> getDataList() {
        return mDataList;
    }

    /**
     * 设置数据集合
     * @param list
     */
    public void setDataList(Collection<T> list) {
        this.mDataList.clear();
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加集合
     * @param list
     */
    public void addAll(Collection<T> list) {

        int lastIndex = this.mDataList.size();
        if (this.mDataList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
        Log.i("TAG", "addAll: "+list.size());
    }

    public void remove(int position) {
        if(this.mDataList.size() > 0) {
            mDataList.remove(position);
            notifyItemRemoved(position);
        }

    }

    /**
     * 清除数据
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }


}
