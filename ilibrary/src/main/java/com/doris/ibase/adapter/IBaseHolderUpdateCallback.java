package com.doris.ibase.adapter;

/**
 * Created by Doris on 2018/10/31.
 */
public interface IBaseHolderUpdateCallback<Data> {

    void update(Data data, IBaseViewHolder<Data> holder);
}
