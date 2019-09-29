package com.doris.ibase.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doris.ibase.adapter.more.IBaseLoadMoreHolder;
import com.doris.ibase.adapter.more.IDefaultLoadMoreHolder;
import com.doris.ibase.ilibrary.R;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Doris on 2018/10/28.
 */
public abstract class IBaseRecyclerAdapter<Data> extends RecyclerView.Adapter<IBaseViewHolder>
        implements View.OnClickListener, View.OnLongClickListener,
        IBaseHolderUpdateCallback<Data> {

    private LinkedList<Data> mDataList;
    private LinkedList<ItemView> mHeaderList = new LinkedList<>();
    private LinkedList<ItemView> mFooterList = new LinkedList<>();
    private final Object mLock = new Object();
    private boolean mNotifyOnChange = true;

    private OnItemClickListener<Data> mItemClickListener;
    private OnItemLongClickListener<Data> mItemLongClickListener;

    private Context mContext;

    private boolean mNeedLoadMore = false, mLoadMoreIsLast = true;
    private IBaseLoadMoreHolder mLoadMoreHolder;

    public static abstract class ItemView {
        protected abstract View onCreateView(ViewGroup parent);

        public void onBindView(View view) {
        }
    }

    public IBaseRecyclerAdapter(Context context) {
        this.mContext = context;
        mDataList = new LinkedList<>();
    }

    @Override
    public final int getItemCount() {
        int count = getHeaderCount() + getCount() + getFooterCount();
        if (mNeedLoadMore) {
            return count + 1;
        }
        return count;
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
            if (mNeedLoadMore && !mLoadMoreIsLast) {
                index -= 1;
            }
            if (index >= 0 && getFooterCount() > index) {
                return mFooterList.get(index).hashCode();
            }
        }
        // 加载更多
        if (mNeedLoadMore) {
            if (mLoadMoreIsLast) {
                if (position == getItemCount() - 1) {
                    return mLoadMoreHolder.hashCode();
                }
            } else if (position == getItemCount() - getFooterCount() - 1) {
                return mLoadMoreHolder.hashCode();
            }
        }
        // 数据内容
        return getContentItemViewType(position - getHeaderCount());
    }

    public abstract int getContentItemViewType(int position);

    @NonNull
    @Override
    public final IBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        // 头部或底部
        View view = createHeaderOrFooterViewByType(parent, viewType);
        if (view != null) {
            return new HeaderOrFooterViewHolder(view);
        }
        // 加载更多
        if (mNeedLoadMore) {
            if (viewType == mLoadMoreHolder.hashCode()) {
                return mLoadMoreHolder.getLoadMoreHolder(parent);
            }
        }
        // 数据内容
        return getViewHolder(parent, viewType);
    }

    private View createHeaderOrFooterViewByType(ViewGroup parent, int viewType) {
        for (ItemView headerView : mHeaderList) {
            if (headerView.hashCode() == viewType) {
                return getStateView(headerView.onCreateView(parent));
            }
        }
        for (ItemView footerView : mFooterList) {
            if (footerView.hashCode() == viewType) {
                return getStateView(footerView.onCreateView(parent));
            }
        }
        return null;
    }

    private View getStateView(View view) {
        StaggeredGridLayoutManager.LayoutParams layoutParams;
        if (view.getLayoutParams() != null) {
            layoutParams = new StaggeredGridLayoutManager.LayoutParams(view.getLayoutParams());
        } else {
            layoutParams = new StaggeredGridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        layoutParams.setFullSpan(true);
        view.setLayoutParams(layoutParams);
        return view;
    }

    private IBaseViewHolder<Data> getViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        IBaseViewHolder<Data> holder = createContentViewHolder(root);
        root.setTag(R.id.tag_recycler_holder, holder);
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);
        holder.setHolderUpdateCallback(this);
        return holder;
    }

    public abstract IBaseViewHolder<Data> createContentViewHolder(View root);

    @Override
    public final void onBindViewHolder(@NonNull IBaseViewHolder holder, int position) {
        holder.itemView.setId(position);
        // 头部
        if (getHeaderCount() > 0 && position < getHeaderCount()) {
            mHeaderList.get(position).onBindView(holder.itemView);
            return;
        }
        // 底部
        int index = position - getHeaderCount() - getCount();
        if (index >= 0 && getFooterCount() > index) {
            mFooterList.get(index).onBindView(holder.itemView);
            return;
        }
        //
        if (mNeedLoadMore) {
            if (position == getItemCount() - 1) {
                return;
            }
        }
        // 数据内容
        index = position - getHeaderCount();
        holder.bind(getItem(index), index);
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void update(Data data, IBaseViewHolder<Data> holder) {
        // 得到当前ViewHolder的坐标
        int pos = holder.getAdapterPosition() - getHeaderCount();
        if (pos >= 0 && getCount() > pos) {
            // 进行数据的移除与更新
            mDataList.remove(pos);
            mDataList.add(pos, data);
            // 通知这个坐标下的数据有更新
            notifyItemChanged(holder.getAdapterPosition());
        }
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
            int position = getHeaderCount() + getCount() + getFooterCount() - 1;
            if (mNeedLoadMore && !mLoadMoreIsLast) {
                position = getHeaderCount() + getCount() + getFooterCount() + 1;
            }
            notifyItemInserted(position);
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
        if (index >= 0 && getFooterCount() > index) {
            return mFooterList.get(index);
        }
        return null;
    }

    /**
     * 获取头部集合大小
     *
     * @return
     */
    public int getHeaderCount() {
        return mHeaderList.size();
    }

    /**
     * 获取底部集合大小
     *
     * @return
     */
    public int getFooterCount() {
        return mFooterList.size();
    }

    /**
     * 获取头部集合
     *
     * @return
     */
    public LinkedList<ItemView> getHeaders() {
        return mHeaderList;
    }

    /**
     * 获取底部集合
     *
     * @return
     */
    public LinkedList<ItemView> getFooters() {
        return mFooterList;
    }

    /**
     * 移除指定头部数据
     *
     * @param view
     */
    public void removeHeader(ItemView view) {
        removeHeader(mHeaderList.indexOf(view));
    }

    /**
     * 移除指定底部数据
     *
     * @param view
     */
    public void removeFooter(ItemView view) {
        removeFooter(mFooterList.indexOf(view));
    }

    /**
     * 根据下标移除头部数据
     *
     * @param index
     */
    public void removeHeader(int index) {
        if (index >= 0 && getHeaderCount() > index) {
            mHeaderList.remove(index);
            notifyItemRemoved(index);
        }
    }

    /**
     * 根据下标移除底部数据
     *
     * @param index
     */
    public void removeFooter(int index) {
        if (index >= 0 && getFooterCount() > index) {
            mFooterList.remove(index);
            int position = getHeaderCount() + getCount() + index;
            if (mNeedLoadMore && !mLoadMoreIsLast) {
                position += 1;
            }
            notifyItemRemoved(position);
        }
    }

    /**
     * 获取数据集合
     *
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
     *
     * @param index
     * @return
     */
    public Data getItem(int index) {
        if (index >= 0 && getCount() > index) {
            return mDataList.get(index);
        }
        return null;
    }

    /**
     * 添加一条数据
     *
     * @param data
     */
    public void addFirst(Data data) {
        if (data != null) {
            synchronized (mLock) {
                mDataList.addFirst(data);
            }
        }
        if (mNotifyOnChange) {
            notifyItemInserted(getHeaderCount());
        }
        if (mNeedLoadMore) {
            mLoadMoreHolder.changeMoreState(IBaseLoadMoreHolder.STATE_LOAD_MORE_UP);
        }
    }

    /**
     * 添加一条数据
     *
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
        if (mNeedLoadMore) {
            mLoadMoreHolder.changeMoreState(IBaseLoadMoreHolder.STATE_LOAD_MORE_UP);
        }
    }

    /**
     * 添加多条数据
     *
     * @param dataList
     */
    public void add(Collection<? extends Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            synchronized (mLock) {
                mDataList.addAll(dataList);
            }
            if (mNotifyOnChange) {
                notifyItemRangeInserted(
                        getHeaderCount() + getCount() - dataList.size() + 1,
                        dataList.size());
            }
            if (mNeedLoadMore) {
                mLoadMoreHolder.changeMoreState(IBaseLoadMoreHolder.STATE_LOAD_MORE_UP);
            }
        }

    }

    /**
     * 移除一条数据
     *
     * @param data
     */
    public void remove(Data data) {
        if (data != null) {
            int position = mDataList.indexOf(data);
            remove(position);
        }
    }

    /**
     * 根据下标移除一条数据
     *
     * @param position
     */
    public void remove(int position) {
        if (position >= 0 && getCount() > position) {
            synchronized (mLock) {
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
     * 是否需要加载更多
     */
    public void needLoadMore(RecyclerView recyclerView, OnLoadMoreListener listener) {
        needLoadMore(true, recyclerView, listener);
    }

    /**
     * 是否需要加载更多
     */
    public void needLoadMore(boolean loadMoreIsLast, RecyclerView recyclerView, OnLoadMoreListener listener) {
        needLoadMore(loadMoreIsLast, recyclerView, new IDefaultLoadMoreHolder(), listener);
    }

    /**
     * 是否需要加载更多
     */
    public void needLoadMore(boolean loadMoreIsLast, RecyclerView recyclerView,
                             IBaseLoadMoreHolder loadMoreHolder, OnLoadMoreListener listener) {
        mLoadMoreIsLast = loadMoreIsLast;
        mNeedLoadMore = true;
        mLoadMoreHolder = loadMoreHolder;
        mLoadMoreHolder.setOnLoadMoreListener(listener);
        initLoadMore(recyclerView);
    }

    private void initLoadMore(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return;
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 当前并不处于滑动状态
                    int lastVisiblePosition;
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof GridLayoutManager) {
                        lastVisiblePosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        int into[] = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                        ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                        lastVisiblePosition = findMax(into);
                    } else {
                        lastVisiblePosition = ((LinearLayoutManager) layoutManager)
                                .findLastCompletelyVisibleItemPosition();
                    }
                    if (getCount() == 0) {
                        mLoadMoreHolder.changeMoreState(IBaseLoadMoreHolder.STATE_LOAD_MORE_EMPTY);
                        return;
                    }
                    int lastItemPosition;
                    if (mLoadMoreIsLast) {
                        lastItemPosition = layoutManager.getItemCount() - 1;
                    } else {
                        lastItemPosition = layoutManager.getItemCount() - getFooterCount() - 1;
                    }
                    if (layoutManager.getChildCount() > 0
                            && lastVisiblePosition >= lastItemPosition
                            && layoutManager.getItemCount() > layoutManager.getChildCount()) {
                        if (mLoadMoreHolder.getLoadMoreState()
                                != IBaseLoadMoreHolder.STATE_LOAD_MORE_NO) {
                            mLoadMoreHolder.changeMoreState(IBaseLoadMoreHolder.STATE_LOAD_MORE);
                            mLoadMoreHolder.onLoadMore();
                        }
                    }
                }
            }
        });
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 停止加载更多（没有更多了）
     */
    public void loadMoreStop() {
        if (mNeedLoadMore) {
            mLoadMoreHolder.changeMoreState(IBaseLoadMoreHolder.STATE_LOAD_MORE_NO);
        }
    }

    /**
     * 加载失败
     */
    public void loadMoreError() {
        if (mNeedLoadMore) {
            mLoadMoreHolder.changeMoreState(IBaseLoadMoreHolder.STATE_LOAD_MORE_ERROR);
        }
    }

    /**
     * 设置是否需要刷新
     *
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

    @Override
    public void onClick(View v) {
        IBaseViewHolder<Data> viewHolder = (IBaseViewHolder<Data>) v.getTag(R.id.tag_recycler_holder);
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(viewHolder, viewHolder.getData());
        }
    }

    public void setOnItemClickListener(OnItemClickListener<Data> listener) {
        this.mItemClickListener = listener;
    }

    public interface OnItemClickListener<Data> {
        void onItemClick(IBaseViewHolder<Data> holder, Data data);
    }

    @Override
    public boolean onLongClick(View v) {
        IBaseViewHolder<Data> viewHolder = (IBaseViewHolder<Data>) v.getTag(R.id.tag_recycler_holder);
        if (mItemLongClickListener != null) {
            mItemLongClickListener.onItemLongClick(viewHolder, viewHolder.getData());
            return true;
        }
        return false;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<Data> listener) {
        this.mItemLongClickListener = listener;
    }

    public interface OnItemLongClickListener<Data> {
        void onItemLongClick(IBaseViewHolder<Data> holder, Data data);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public GridSpanSizeLookup obtainGridSpanSizeLookUp(int maxCount) {
        return new GridSpanSizeLookup(maxCount);
    }

    private class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

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
