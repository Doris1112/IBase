package com.doris.ibase.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author Doris
 * @date 2018/10/28
 */
public abstract class IBaseViewHolder<Data> extends RecyclerView.ViewHolder {

    private Data mData;
    private IHolderUpdateCallback<Data> mCallback;

    public IBaseViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * 用于绑定数据的触发
     */
    public void bind(Data data, int position) {
        mData = data;
        onBind(data, position);
    }

    /**
     * 当触发绑定数据的时候的回调
     */
    public void onBind(Data data, int position) {

    }

    /**
     * Holder自己对自己对应的Data进行更新操作
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

    public IBaseViewHolder<Data> setHolderUpdateCallback(IHolderUpdateCallback<Data> callback){
        mCallback = callback;
        return this;
    }
}