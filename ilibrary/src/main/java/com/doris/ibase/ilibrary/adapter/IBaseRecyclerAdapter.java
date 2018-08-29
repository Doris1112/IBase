package com.doris.ibase.ilibrary.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doris.ibase.ilibrary.R;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by Doris on 2018/3/4.
 */
public abstract class IBaseRecyclerAdapter<Data>
        extends RecyclerView.Adapter<IBaseRecyclerAdapter.ViewHolder<Data>>
        implements View.OnClickListener, View.OnLongClickListener, IBaseAdapterCallback<Data> {

    private final LinkedList<Data> mDataList;
    private AdapterListener<Data> mListener;

    public IBaseRecyclerAdapter() {
        this(null);
    }

    public IBaseRecyclerAdapter(AdapterListener<Data> listener) {
        this(new LinkedList<Data>(), listener);
    }

    public IBaseRecyclerAdapter(LinkedList<Data> dataList, AdapterListener<Data> listener) {
        this.mDataList = dataList;
        this.mListener = listener;
    }

    /**
     * 复写默认的布局类型返回
     *
     * @param position
     * @return 其实返回的都是XML文件的ID
     */
    @Override
    public int getItemViewType(int position) {
        try{
            return getItemViewType(position, mDataList.get(position));
        } catch (Exception e){
            // 以防下标越界，程序异常
            return getItemViewType(position, null);
        }
    }

    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);

    /**
     * @param parent
     * @param viewType 定为xml布局id
     * @return
     */
    @Override
    public ViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
        // 得到LayoutInflater用于把XML初始化未View
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // 把XML ID为viewType的文件初始化为一个root view
        View root = inflater.inflate(viewType, null, false);
        // 通过子类必须实现的方法，得到一个ViewHolder
        ViewHolder<Data> holder = onCreateViewHolder(root, viewType);
        // 设置View的tag为ViewHolder，进行双向绑定
        root.setTag(R.id.tag_recycler_holder, holder);
        // 设置事件点击
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);
        // 绑定callback
        holder.callback = this;
        return holder;
    }

    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    @Override
    public void onBindViewHolder(ViewHolder<Data> holder, int position) {
        try {
            // 得到需要绑定的数据
            Data data = mDataList.get(position);
            // 触发holder的绑定方法
            holder.bind(data);
        } catch (Exception e) {
            // 以防下标越界，程序异常
            holder.bind(null);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 添加一条数据并通知添加
     *
     * @param data
     */
    public void add(Data data) {
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 添加一堆数据并通知这段集合更新
     *
     * @param dataList
     */
    public void add(Data... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }

    /**
     * 添加一堆数据并通知这段集合更新
     *
     * @param dataList
     */
    public void add(Collection<Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPos = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }

    /**
     * 添加一堆数据并通知这段集合更新
     *
     * @param data
     */
    public void addFirst(Data data) {
        mDataList.addFirst(data);
    }

    /**
     * 替换为一个新的集合，其中包括了清空
     *
     * @param dataList
     */
    public void replace(Collection<Data> dataList) {
        mDataList.clear();
        if (dataList != null && dataList.size() > 0) {
            mDataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    /**
     * 替换指定位置数据
     * @param position
     * @param data
     */
    public void replace(int position, Data data){
        mDataList.set(position, data);
        notifyItemChanged(position);
    }

    /**
     * 根据下标移除数据
     * @param position
     */
    public void removeAtIndex(int position){
        mDataList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void update(Data data, ViewHolder<Data> holder) {
        // 得到当前ViewHolder的坐标
        int pos = holder.getAdapterPosition();
        if (pos >= 0) {
            // 进行数据的移除与更新
            mDataList.remove(pos);
            mDataList.add(pos, data);
            // 通知这个坐标下的数据有更新
            notifyItemChanged(pos);
        }
    }

    @Override
    public void onClick(View v) {
        ViewHolder<Data> viewHolder = (ViewHolder<Data>) v.getTag(R.id.tag_recycler_holder);
        if (mListener != null) {
            mListener.onItemClick(viewHolder, viewHolder.mData);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        ViewHolder<Data> viewHolder = (ViewHolder<Data>) v.getTag(R.id.tag_recycler_holder);
        if (mListener != null) {
            mListener.onItemLongClick(viewHolder, viewHolder.mData);
            return true;
        }
        return false;
    }

    /**
     * 设置适配器监听
     *
     * @param listener
     */
    public void setListener(AdapterListener<Data> listener) {
        mListener = listener;
    }

    /**
     * 自定义监听器
     *
     * @param <Data>
     */
    public interface AdapterListener<Data> {
        // 当Cell点击时触发
        void onItemClick(IBaseRecyclerAdapter.ViewHolder<Data> holder, Data data);
        // 当Cell长按时触发
        void onItemLongClick(IBaseRecyclerAdapter.ViewHolder<Data> holder, Data data);
    }

    /**
     * 自定义ViewHolder
     *
     * @param <Data>
     */
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {

        private IBaseAdapterCallback<Data> callback;
        private Data mData;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         *
         * @param data
         */
        void bind(Data data) {
            mData = data;
            onBind(data);
        }

        /**
         * 当触发绑定数据的时候的回调，必须复写
         *
         * @param data
         */
        protected abstract void onBind(Data data);

        /**
         * Holder自己对自己对应的Data进行更新操作
         *
         * @param data
         */
        public void updateData(Data data) {
            if (callback != null) {
                callback.update(data, this);
            }
        }
    }
}
