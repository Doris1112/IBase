package com.doris.ibase.adapter.more;

import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;

import com.doris.ibase.adapter.IBaseRecyclerAdapter;
import com.doris.ibase.adapter.IBaseViewHolder;

/**
 * Created by Doris on 2018/10/30.
 */
public abstract class IBaseLoadMoreHolder {

    int mState = STATE_LOAD_MORE_EMPTY;
    public static final int STATE_LOAD_MORE_EMPTY = 0;
    public static final int STATE_LOAD_MORE_UP = 1;
    public static final int STATE_LOAD_MORE = 2;
    public static final int STATE_LOAD_MORE_ERROR = 3;
    public static final int STATE_LOAD_MORE_NO = 4;

    private IBaseRecyclerAdapter.OnLoadMoreListener mLoadMoreListener;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            notifyLoadMoreChanged();
        }
    };

    public abstract IBaseViewHolder<Integer> getLoadMoreHolder(ViewGroup parent);

    /**
     * 更新加载状态
     *
     * @param state
     */
    public void changeMoreState(int state) {
        if (mState != state){
            mState = state;
            mHandler.sendEmptyMessageDelayed(mState, 100);
        }
    }

    /**
     * 获取加载更多状态
     * @return
     */
    public int getLoadMoreState(){
        return mState;
    }

    public abstract void notifyLoadMoreChanged();

    /**
     * 设置加载更多事件
     *
     * @param listener 加载更多事件
     */
    public void setOnLoadMoreListener(IBaseRecyclerAdapter.OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    public void onLoadMore() {
        if (mLoadMoreListener != null) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoadMoreListener.onLoadMore();
                }
            }, 200);
        }
    }

}
