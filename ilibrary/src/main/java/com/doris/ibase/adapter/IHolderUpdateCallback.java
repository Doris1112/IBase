package com.doris.ibase.adapter;

/**
 * @author Doris
 * @date 2018/10/31
 */
public interface IHolderUpdateCallback<Data> {

    void update(Data data, IBaseViewHolder<Data> holder);
}
