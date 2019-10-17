package com.doris.ibase;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.doris.ibase.activities.IBaseAppCompatActivity;
import com.doris.ibase.adapter.IBaseRecyclerAdapter;
import com.doris.ibase.adapter.IBaseViewHolder;
import com.doris.ibase.utils.IToastUtils;

/**
 * Created by Doris on 2018/9/22.
 */
public class NextActivity extends IBaseAppCompatActivity implements View.OnClickListener,
        IBaseRecyclerAdapter.OnLoadMoreListener {

    private static final String TAG = NextActivity.class.getSimpleName();

    private SwipeRefreshLayout mRefreshLayout;
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
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh: ");
                mPageIndex = 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.clear();
                        mAdapter.add(Poetry.getPoetry());
                        mRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });

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
    public void onClick(View v) {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onLoadMore() {
        Log.d(TAG, "onLoadMore: ");
        mPageIndex ++;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPageIndex == 4) {
                    mAdapter.loadMoreError();
                } else if (mPageIndex > 4) {
                    mAdapter.loadMoreStop();
                } else {
                    mAdapter.add(Poetry.getPoetry());
                }
            }
        },5000);
    }

}
