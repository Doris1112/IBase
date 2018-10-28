package com.doris.ibase.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Doris on 2018/10/28.
 */
public abstract class IBaseRecyclerAdapter<Data> extends RecyclerView.Adapter<IBaseViewHolder> {

    private LinkedList<Data> mDataList;
    private LinkedList<ItemView> mHeaderList = new LinkedList<>();
    private LinkedList<ItemView> mFooterList = new LinkedList<>();
    private final Object mLock = new Object();
    private boolean mNotifyOnChange = true;

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public static abstract class ItemView {
        protected abstract View onCreateView(ViewGroup parent);

        public void onBindView(View headerView) {

        }
    }

    public IBaseRecyclerAdapter() {
        mDataList = new LinkedList<>();
    }

    @Override
    public final int getItemCount() {
        return getHeaderCount() + getCount() + getFooterCount();
    }

    @Override
    public final int getItemViewType(int position) {
        // 头部
        if (getHeaderCount() > 0) {
            if (position < getHeaderCount()) {
                return mHeaderList.get(position).hashCode();
            }
        }
        // 底部
        if (getFooterCount() > 0) {
            int index = position - getHeaderCount() - getCount();
            if (index >= 0) {
                return mFooterList.get(index).hashCode();
            }
        }
        return position - getHeaderCount();
    }

    @NonNull
    @Override
    public IBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        // 头部或底部
        View view = createHeaderOrFooterViewByType(parent, viewType);
        if (view != null) {
            return new HeaderOrFooterViewHolder(view);
        }
        // 数据内容
        final IBaseViewHolder viewHolder = createViewHolder(parent, viewType,
                mDataList.get(viewType));
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(viewType, v);
                }
            });
        }

        if (mItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mItemLongClickListener.onItemClick(viewType, v);
                }
            });
        }
        return viewHolder;
    }

    private View createHeaderOrFooterViewByType(ViewGroup parent, int viewType) {
        for (ItemView headerView : mHeaderList) {
            if (headerView.hashCode() == viewType) {
                View view = headerView.onCreateView(parent);
                StaggeredGridLayoutManager.LayoutParams layoutParams;
                if (view.getLayoutParams() != null)
                    layoutParams = new StaggeredGridLayoutManager.LayoutParams(view.getLayoutParams());
                else
                    layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setFullSpan(true);
                view.setLayoutParams(layoutParams);
                return view;
            }
        }
        for (ItemView footerView : mFooterList) {
            if (footerView.hashCode() == viewType) {
                View view = footerView.onCreateView(parent);
                StaggeredGridLayoutManager.LayoutParams layoutParams;
                if (view.getLayoutParams() != null)
                    layoutParams = new StaggeredGridLayoutManager.LayoutParams(view.getLayoutParams());
                else
                    layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setFullSpan(true);
                view.setLayoutParams(layoutParams);
                return view;
            }
        }
        return null;
    }

    public abstract IBaseViewHolder<Data> createViewHolder(ViewGroup parent, int viewType, Data data);

    @Override
    public void onBindViewHolder(@NonNull IBaseViewHolder holder, int position) {
        holder.itemView.setId(position);
        // 头部
        if (getHeaderCount() > 0 && position < getHeaderCount()) {
            mHeaderList.get(position).onBindView(holder.itemView);
            return;
        }
        // 底部
        int index = position - getHeaderCount() - getCount();
        if (getFooterCount() > 0 && index >= 0) {
            mFooterList.get(index).onBindView(holder.itemView);
            return;
        }
        index = position - getHeaderCount();
        holder.onBind(getItem(index), index);
    }

    /**
     * 添加头部
     *
     * @param view
     */
    public void addHeader(ItemView view) {
        if (view != null) {
            mHeaderList.add(view);
            notifyItemInserted(getHeaderCount() - 1);
        }
    }

    /**
     * 添加底部
     *
     * @param view
     */
    public void addFooter(ItemView view) {
        if (view != null) {
            mFooterList.add(view);
            notifyItemInserted(getHeaderCount() + getCount() + getFooterCount() - 1);
        }
    }

    /**
     * 清空所有头部
     */
    public void removeAllHeader() {
        int count = getHeaderCount();
        if (count > 0) {
            mHeaderList.clear();
            notifyItemRangeRemoved(0, count);
        }
    }

    /**
     * 清空所有底部
     */
    public void removeAllFooter() {
        int count = getFooterCount();
        if (count > 0) {
            mFooterList.clear();
            notifyItemRangeRemoved(getHeaderCount() + getCount(), count);
        }
    }

    /**
     * 根据下标获取头部
     *
     * @param index
     * @return
     */
    public ItemView getHeader(int index) {
        if (index >= 0 && getHeaderCount() > index) {
            return mHeaderList.get(index);
        }
        return null;
    }

    /**
     * 根据下标获取底部
     *
     * @param index
     * @return
     */
    public ItemView getFooter(int index) {
        if (index >= 0 && getFooterCount() > index){
            return mFooterList.get(index);
        }
        return null;
    }

    /**
     * 获取头部集合大小
     * @return
     */
    public int getHeaderCount() {
        return mHeaderList.size();
    }

    /**
     * 获取底部集合大小
     * @return
     */
    public int getFooterCount() {
        return mFooterList.size();
    }

    /**
     * 获取头部集合
     * @return
     */
    public LinkedList<ItemView> getHeaders(){
        return mHeaderList;
    }

    /**
     * 获取底部集合
     * @return
     */
    public LinkedList<ItemView> getFooters(){
        return mFooterList;
    }

    /**
     * 移除指定头部数据
     * @param view
     */
    public void removeHeader(ItemView view) {
        removeHeader(mHeaderList.indexOf(view));
    }

    /**
     * 移除指定底部数据
     * @param view
     */
    public void removeFooter(ItemView view) {
        removeFooter(mFooterList.indexOf(view));
    }

    /**
     * 根据下标移除头部数据
     * @param index
     */
    public void removeHeader(int index){
        if (index >= 0 && getHeaderCount() > index){
            mHeaderList.remove(index);
            notifyItemRemoved(index);
        }
    }

    /**
     * 根据下标移除底部数据
     * @param index
     */
    public void removeFooter(int index){
        if (index >= 0 && getFooterCount() > index){
            mFooterList.remove(index);
            notifyItemRemoved(getHeaderCount() + getCount() + index);
        }
    }

    /**
     * 获取数据集合
     * @return
     */
    public LinkedList<Data> getDataList() {
        return mDataList;
    }

    /**
     * 获取数据大小
     *
     * @return
     */
    public int getCount() {
        return mDataList.size();
    }

    /**
     * 根据下标获取数据
     * @param index
     * @return
     */
    public Data getItem(int index) {
        if (index >=0 && getCount() > index){
            return mDataList.get(index);
        }
        return null;
    }

    /**
     * 添加一条数据
     * @param data
     */
    public void add(Data data) {
        if (data != null) {
            synchronized (mLock) {
                mDataList.add(data);
            }
        }
        if (mNotifyOnChange) {
            notifyItemInserted(getHeaderCount() + getCount() + 1);
        }
    }

    /**
     * 添加多条数据
     * @param dataList
     */
    public void add(Collection<? extends Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            synchronized (mLock) {
                mDataList.addAll(dataList);
            }
            if (mNotifyOnChange){
                notifyItemRangeInserted(
                        getHeaderCount() + getCount() - dataList.size() + 1,
                        dataList.size());
            }
        }

    }

    /**
     * 移除一条数据
     * @param data
     */
    public void remove(Data data) {
        if (data != null){
            int position = mDataList.indexOf(data);
            remove(position);
        }
    }

    /**
     * 根据下标移除一条数据
     * @param position
     */
    public void remove(int position) {
        if (position >= 0 && getCount() > position){
            synchronized (mLock){
                mDataList.remove(position);
            }
            if (mNotifyOnChange) {
                notifyItemRemoved(getHeaderCount() + position);
            }
        }
    }

    /**
     * 清空数据
     */
    public void clear() {
        int count = getCount();
        synchronized (mLock) {
            mDataList.clear();
        }
        if (mNotifyOnChange) {
            notifyItemRangeRemoved(getHeaderCount(), count);
        }
    }

    /**
     *  设置是否需要刷新
     * @param notifyOnChange
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    private class HeaderOrFooterViewHolder extends IBaseViewHolder {

        HeaderOrFooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    public interface OnItemLongClickListener {
        boolean onItemClick(int position, View v);
    }

    public GridSpanSizeLookup obtainGridSpanSizeLookUp(int maxCount) {
        return new GridSpanSizeLookup(maxCount);
    }

    public class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        private int mMaxCount;

        public GridSpanSizeLookup(int maxCount) {
            this.mMaxCount = maxCount;
        }

        @Override
        public int getSpanSize(int position) {
            if (getHeaderCount() > 0) {
                if (position < getHeaderCount()) return mMaxCount;
            }
            if (getFooterCount() > 0) {
                int i = position - getHeaderCount() - getCount();
                if (i >= 0) {
                    return mMaxCount;
                }
            }
            return 1;
        }
    }

}
