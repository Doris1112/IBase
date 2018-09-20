package com.doris.ibase.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Doris on 2018/3/1.
 */
public abstract class IBaseAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected final LinkedList<T> mDataList;

    public IBaseAdapter(Context context){
        mContext = context;
        mDataList = new LinkedList<>();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public T getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(T t){
        if (t != null){
            mDataList.add(t);
        }
    }

    public void addFirst(T t) {
        if (t != null){
            mDataList.addFirst(t);
        }
    }

    public void addAll(Collection<T> dataList){
        if(dataList != null && dataList.size() > 0){
            mDataList.addAll(dataList);
        }
    }

    public void set(int position, T t){
        if (mDataList.size() > position && t != null){
            mDataList.set(position, t);
        }
    }

    public void removeAtIndex(int position) {
        if (mDataList.size() > position){
            mDataList.remove(position);
        }
    }

    public void clear() {
        mDataList.clear();
    }
}
