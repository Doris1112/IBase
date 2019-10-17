package com.doris.ibase.adapter.more;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doris.ibase.adapter.IBaseViewHolder;
import com.doris.ibase.ilibrary.R;
import com.doris.ibase.widget.ILoadingView;

/**
 * @author Doris
 * @date 2018/10/30
 */
public class IDefaultLoadMoreHolder extends IBaseLoadMoreHolder {

    private LoadMoreHolder mHolder;

    @Override
    public IBaseViewHolder<Integer> getLoadMoreHolder(ViewGroup parent) {
        if (mHolder == null){
            mHolder = new LoadMoreHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.i_default_load_more, parent, false));
        }
        return mHolder;
    }

    @Override
    public void notifyLoadMoreChanged() {
        if (mHolder != null){
            mHolder.updateUi();
        }
    }

    private class LoadMoreHolder extends IBaseViewHolder<Integer>  {

        private ILoadingView loadingView;
        private TextView textView;

        LoadMoreHolder(View itemView) {
            super(itemView);

            loadingView = itemView.findViewById(R.id.i_loading_load_more);
            textView = itemView.findViewById(R.id.i_tv_load_more);

            loadingView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }

        @Override
        public void onBind(Integer integer, int position) {
            super.onBind(integer, position);
            updateUi();
        }

        void updateUi(){
            textView.setVisibility(View.VISIBLE);
            switch (mState) {
                case STATE_LOAD_MORE_UP:
                    loadingView.setVisibility(View.GONE);
                    textView.setText("上拉加载更多");
                    break;
                case STATE_LOAD_MORE:
                    loadingView.setVisibility(View.VISIBLE);
                    textView.setText("正在加载...");
                    break;
                case STATE_LOAD_MORE_NO:
                    loadingView.setVisibility(View.GONE);
                    textView.setText("没有更多了");
                    break;
                case STATE_LOAD_MORE_ERROR:
                    loadingView.setVisibility(View.GONE);
                    textView.setText("加载失败了");
                    break;
                default:
                    loadingView.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
