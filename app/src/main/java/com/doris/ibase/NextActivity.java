package com.doris.ibase;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.doris.ibase.activities.IBaseAppCompatActivity;
import com.doris.ibase.adapter.IBaseRecyclerAdapter;
import com.doris.ibase.adapter.IBaseViewHolder;
import com.doris.ibase.utils.IToastUtils;

/**
 * @author Doris
 * @date 2018/9/22
 */
public class NextActivity extends IBaseAppCompatActivity implements
        IBaseRecyclerAdapter.OnLoadMoreListener {

    private RecyclerView mRecyclerView;
    private int mPageIndex = 1;
    private PoetryAdapter mAdapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_next;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PoetryAdapter(this);
        addHeader();
        addFooter();
        mAdapter.setOnItemClickListener(new IBaseRecyclerAdapter.OnItemClickListener<Poetry>() {
            @Override
            public void onItemClick(IBaseViewHolder<Poetry> holder, Poetry resultBean) {
                IToastUtils.showToastCenter(NextActivity.this,
                        resultBean.getTitle());
            }
        });
        mAdapter.setOnItemLongClickListener(new IBaseRecyclerAdapter.OnItemLongClickListener<Poetry>() {
            @Override
            public void onItemLongClick(IBaseViewHolder<Poetry> holder, Poetry resultBean) {
                IToastUtils.showToastCenter(NextActivity.this,
                        resultBean.getAuthors());
            }
        });
        mAdapter.needLoadMore(false, mRecyclerView, this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startLoadMore();
    }

    private void addHeader() {
        mAdapter.addHeader(new IBaseRecyclerAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return LayoutInflater.from(NextActivity.this)
                        .inflate(R.layout.item_header1, parent, false);
            }
        });
        mAdapter.addHeader(new IBaseRecyclerAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return LayoutInflater.from(NextActivity.this)
                        .inflate(R.layout.item_header2, parent, false);
            }
        });
    }

    private void addFooter() {
        final View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.addFooter(new IBaseRecyclerAdapter.ItemView() {
                    @Override
                    public View onCreateView(ViewGroup parent) {
                        View footerII = LayoutInflater.from(NextActivity.this)
                                .inflate(R.layout.item_footer2, parent, false);
                        ImageView closeII = footerII.findViewById(R.id.close);
                        closeII.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mAdapter.removeFooter(mAdapter.getFooterCount() - 1);
                            }
                        });
                        return footerII;
                    }
                });
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        };
        mAdapter.addFooter(new IBaseRecyclerAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View footerI = LayoutInflater.from(NextActivity.this)
                        .inflate(R.layout.item_footer1, parent, false);
                ImageView closeI = footerI.findViewById(R.id.close);
                closeI.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAdapter.removeFooter(mAdapter.getFooter(0));
                    }
                });
                footerI.setOnClickListener(clickListener);
                return footerI;
            }
        });
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPageIndex == 1){
                    mAdapter.setDataList(Poetry.getPoetry());
                    mAdapter.canLoadMore();
                } else if (mPageIndex == 3) {
                    mAdapter.loadMoreError();
                } else if (mPageIndex > 3) {
                    mAdapter.stopLoadMore();
                } else {
                    mAdapter.add(Poetry.getPoetry1());
                    mAdapter.canLoadMore();
                }
                mPageIndex ++;
            }
        },5000);
    }

}
