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

    public IBaseViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(Data data, int position) {
        mData = data;
        onBind(data, position);
    }

    public void onBind(Data data, int position) {

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
}