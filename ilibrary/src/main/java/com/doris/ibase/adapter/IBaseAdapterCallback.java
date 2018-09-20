package com.doris.ibase.adapter;

/**
 * Created by Doris on 2018/3/4.
 */
public interface IBaseAdapterCallback<Data> {

    void update(Data data, IBaseRecyclerAdapter.ViewHolder<Data> holder);
}
