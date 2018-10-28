package com.doris.ibase;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doris.ibase.activities.IBaseAppCompatActivity;
import com.doris.ibase.adapter.IBaseRecyclerAdapter;
import com.doris.ibase.adapter.IBaseViewHolder;
import com.doris.ibase.utils.IToastUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Doris on 2018/9/22.
 */
public class NextActivity extends IBaseAppCompatActivity implements View.OnClickListener {

    private static final String TAG = NextActivity.class.getSimpleName();

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private PoetryService poetryService;
    private int page = 1, count = 20;
    private boolean isRefresh = true;
    private PoetryAdapter mAdapter;

    private TextView Song, Tang, LB, DF, WW, MJ, LYX, LY, LH, WB, HZZ, JD, des;
    private String type = "宋";

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_next;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.removeAllHeader();
                mAdapter.removeAllFooter();
                isRefresh = true;
                page = 1;
                if (type.equals("宋")) {
                    poetryService.getSonyPoetry(page, count).enqueue(callback);
                } else if (type.equals("唐")) {
                    poetryService.getTangPoetry(page, count).enqueue(callback);
                } else {
                    refreshLayout.setRefreshing(false);
                }
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PoetryAdapter(this);
        addHeader();
        addFooter();
        mAdapter.setOnItemClickListener(new IBaseRecyclerAdapter.OnItemClickListener<Poetry.ResultBean>() {
            @Override
            public void onItemClick(IBaseViewHolder<Poetry.ResultBean> holder, Poetry.ResultBean resultBean) {
                IToastUtils.showToastCenter(NextActivity.this,
                        resultBean.getTitle());
            }
        });
        mAdapter.setOnItemLongClickListener(new IBaseRecyclerAdapter.OnItemLongClickListener<Poetry.ResultBean>() {
            @Override
            public void onItemLongClick(IBaseViewHolder<Poetry.ResultBean> holder, Poetry.ResultBean resultBean) {
                IToastUtils.showToastCenter(NextActivity.this,
                        resultBean.getAuthors());
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void addHeader() {
        mAdapter.addHeader(new IBaseRecyclerAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View headerI = LayoutInflater.from(NextActivity.this)
                        .inflate(R.layout.item_header1, parent, false);
                Song = headerI.findViewById(R.id.Song);
                Song.setOnClickListener(NextActivity.this);
                Tang = headerI.findViewById(R.id.Tang);
                Tang.setOnClickListener(NextActivity.this);
                return headerI;
            }
        });
        mAdapter.addHeader(new IBaseRecyclerAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View headerII = LayoutInflater.from(NextActivity.this)
                        .inflate(R.layout.item_header2, parent, false);
                LB = headerII.findViewById(R.id.LB);
                LB.setOnClickListener(NextActivity.this);
                DF = headerII.findViewById(R.id.DF);
                DF.setOnClickListener(NextActivity.this);
                WW = headerII.findViewById(R.id.WW);
                WW.setOnClickListener(NextActivity.this);
                MJ = headerII.findViewById(R.id.MJ);
                MJ.setOnClickListener(NextActivity.this);
                LYX = headerII.findViewById(R.id.LYX);
                LYX.setOnClickListener(NextActivity.this);
                LY = headerII.findViewById(R.id.LY);
                LY.setOnClickListener(NextActivity.this);
                LH = headerII.findViewById(R.id.LH);
                LH.setOnClickListener(NextActivity.this);
                WB = headerII.findViewById(R.id.WB);
                WB.setOnClickListener(NextActivity.this);
                HZZ = headerII.findViewById(R.id.HZZ);
                HZZ.setOnClickListener(NextActivity.this);
                JD = headerII.findViewById(R.id.JD);
                JD.setOnClickListener(NextActivity.this);
                return headerII;
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
                recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
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
        isRefresh = true;
        page = 1;
        refreshLayout.setRefreshing(true);
        switch (v.getId()) {
            case R.id.Song:
                type = "宋";
                poetryService.getSonyPoetry(page, count).enqueue(callback);
                break;
            case R.id.Tang:
                type = "唐";
                poetryService.getTangPoetry(page, count).enqueue(callback);
                break;
            default:
                String author = ((TextView) v).getText().toString().split("-")[1];
                poetryService.searchAuthors(author).enqueue(AuthorCallback);
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PoetryService.BASE_URL)
                .build();
        poetryService = retrofit.create(PoetryService.class);
        isRefresh = true;
        page = 1;
        refreshLayout.setRefreshing(true);
        poetryService.getSonyPoetry(page, count).enqueue(callback);
    }

    private Callback<ResponseBody> callback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            try {
                String resultString = response.body().string();
                Log.d(TAG, "onResponse: " + resultString);
                Poetry poetry = Poetry.getPoetry(resultString);
                if (poetry != null && poetry.getCode() == 200) {
                    if (isRefresh) {
                        mAdapter.clear();
                    }
                    mAdapter.add(poetry.getResult());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            stopLoading();
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Log.d(TAG, "onFailure: ");
            stopLoading();
        }
    };

    private Callback<ResponseBody> AuthorCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            try {
                String resultString = response.body().string();
                final Author author = Author.getAuthor(resultString);
                if (author != null && author.getCode() == 200) {
                    if (author.getResult().size() > 0) {
                        if (mAdapter.getHeaderCount() == 2) {
                            mAdapter.addHeader(new IBaseRecyclerAdapter.ItemView() {
                                @Override
                                public View onCreateView(ViewGroup parent) {
                                    View headerIII = LayoutInflater.from(NextActivity.this)
                                            .inflate(R.layout.item_header3, parent, false);
                                    ImageView close = headerIII.findViewById(R.id.close);
                                    close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mAdapter.removeHeader(mAdapter.getHeader(2));
                                        }
                                    });
                                    des = headerIII.findViewById(R.id.des);
                                    des.setText(author.getResult().get(0).getDesc());
                                    return headerIII;
                                }
                            });
                        }
                        if (des != null) {
                            des.setText(author.getResult().get(0).getDesc());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            refreshLayout.setRefreshing(false);
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Log.d(TAG, "onFailure: ");
            refreshLayout.setRefreshing(false);
        }
    };

    private void stopLoading() {
        if (isRefresh) {
            refreshLayout.setRefreshing(false);
        }
    }

}
