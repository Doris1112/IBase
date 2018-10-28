package com.doris.ibase.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Doris on 2018/10/28.
 * @param <Data>
 */
public abstract class IBaseViewHolder<Data> extends RecyclerView.ViewHolder {

    public IBaseViewHolder(View itemView) {
        super(itemView);
    }

    public IBaseViewHolder(ViewGroup parent, @LayoutRes int res) {
        super(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));
    }

    public void onBind(Data data, int position) {

    }

    protected <T extends View> T $(@IdRes int id) {
        return itemView.findViewById(id);
    }

    protected Context getContext() {
        return itemView.getContext();
    }

}