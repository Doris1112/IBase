package com.doris.ibase.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Doris on 2018/10/28.
 *
 * @param <Data>
 */
public abstract class IBaseViewHolder<Data> extends RecyclerView.ViewHolder {

    private Data mData;
    private IBaseHolderUpdateCallback mCallback;

    public IBaseViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * 用于绑定数据的触发
     *
     * @param data
     */
    public void bind(Data data, int position) {
        mData = data;
        onBind(data, position);
    }

    /**
     * 当触发绑定数据的时候的回调
     *
     * @param data
     */
    public void onBind(Data data, int position) {

    }

    /**
     * Holder自己对自己对应的Data进行更新操作
     *
     * @param data
     */
    public void updateData(Data data) {
        if (mCallback != null) {
            mCallback.update(data, this);
        }
    }

    protected <T extends View> T $(@IdRes int id) {
        return itemView.findViewById(id);
    }

    protected Context getContext() {
        return itemView.getContext();
    }

    public Data getData() {
        return mData;
    }

    public IBaseViewHolder<Data> setHolderUpdateCallback(IBaseHolderUpdateCallback callback){
        mCallback = callback;
        return this;
    }
}