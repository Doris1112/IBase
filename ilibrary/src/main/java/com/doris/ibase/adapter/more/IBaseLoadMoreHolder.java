package com.doris.ibase.adapter.more;

import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;

import com.doris.ibase.adapter.IBaseRecyclerAdapter;
import com.doris.ibase.adapter.IBaseViewHolder;

import java.lang.ref.WeakReference;

/**
 * @author Doris
 * @date 2018/10/30
 */
public abstract class IBaseLoadMoreHolder {

    protected int mState = -1;
    public static final int STATE_LOAD_MORE_EMPTY = 0;
    public static final int STATE_LOAD_MORE_UP = 1;
    public static final int STATE_LOAD_MORE = 2;
    public static final int STATE_LOAD_MORE_ERROR = 3;
    public static final int STATE_LOAD_MORE_NO = 4;
    public static final int STATE_LOAD_MORE_CURRENT_REFRESH = 5;

    private IBaseRecyclerAdapter.OnLoadMoreListener mLoadMoreListener;
    private NotifyLoadMoreChangedHandler mHandler = new NotifyLoadMoreChangedHandler(this);

    public abstract IBaseViewHolder<Integer> getLoadMoreHolder(ViewGroup parent);

    /**
     * 更新加载状态
     */
    public void changeMoreState(int state) {
        if (mState != state) {
            mState = state;
            mHandler.sendEmptyMessage(mState);
        }
    }

    /**
     * 获取加载更多状态
     */
    public int getLoadMoreState() {
        return mState;
    }

    /**
     * 当前状态是否能加载更多
     */
    public boolean canLoadMore(){
        return mState != STATE_LOAD_MORE && mState != STATE_LOAD_MORE_NO &&
                mState != STATE_LOAD_MORE_CURRENT_REFRESH;
    }

    public abstract void notifyLoadMoreChanged();

    /**
     * 设置加载更多事件
     */
    public void setOnLoadMoreListener(IBaseRecyclerAdapter.OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    public void onLoadMore() {
        if (mLoadMoreListener != null) {
            mLoadMoreListener.onLoadMore();
        }
    }

    private static class NotifyLoadMoreChangedHandler extends Handler {

        private final WeakReference<IBaseLoadMoreHolder> mTarget;

        NotifyLoadMoreChangedHandler(IBaseLoadMoreHolder target) {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mTarget.get().notifyLoadMoreChanged();
        }
    }
}
