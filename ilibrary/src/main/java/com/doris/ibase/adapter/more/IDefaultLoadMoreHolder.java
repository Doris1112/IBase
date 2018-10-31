package com.doris.ibase.adapter.more;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.doris.ibase.adapter.IBaseViewHolder;
import com.doris.ibase.ilibrary.R;

/**
 * Created by Doris on 2018/10/30.
 */
public class IDefaultLoadMoreHolder extends IBaseLoadMoreHolder {

    private LoadMoreHolder mHolder;

    @Override
    public IBaseViewHolder getLoadMoreHolder(ViewGroup parent) {
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

    private class LoadMoreHolder extends IBaseViewHolder {

        private ProgressBar progressBar;
        private TextView textView;

        LoadMoreHolder(View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.i_progress_load_more);
            textView = itemView.findViewById(R.id.i_tv_load_more);

            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }

        @Override
        public void onBind(Object o, int position) {
            updateUi();
        }

        void updateUi(){
            textView.setVisibility(View.VISIBLE);
            switch (mState) {
                case STATE_LOAD_MORE_EMPTY:
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    break;
                case STATE_LOAD_MORE_UP:
                    progressBar.setVisibility(View.GONE);
                    textView.setText("上拉加载更多");
                    break;
                case STATE_LOAD_MORE:
                    progressBar.setVisibility(View.VISIBLE);
                    textView.setText("正在加载...");
                    break;
                case STATE_LOAD_MORE_NO:
                    progressBar.setVisibility(View.GONE);
                    textView.setText("没有更多了！");
                    break;
                case STATE_LOAD_MORE_ERROR:
                    progressBar.setVisibility(View.GONE);
                    textView.setText("加载失败了！");
                    break;
                default:
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
