package com.github.common;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.common.ui.adapter.DividerItemDecoration;
import com.github.common.ui.adapter.SimpleStringAdapter;
import com.github.common.utils.HandlerUtils;
import com.github.lib.adapter.LoadMoreRecyclerViewAdapter;
import com.github.lib.utils.UIUtils;
import com.github.lib.widget.LoadingStatusView;
import com.github.lib.widget.WrapLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoadMoreRecyclerViewAdapter.ILoadMore {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private LoadingStatusView statusView;

    private SimpleStringAdapter adapter;
    private DividerItemDecoration mDividerItemDecoration;

    private List<String> mData = new ArrayList<>();
    private List<String> mMoreData = new ArrayList<>();

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.list_view);
        statusView = (LoadingStatusView) findViewById(R.id.status_view);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HandlerUtils.getInstance().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        adapter = new SimpleStringAdapter();
        adapter.setLoadMoreListener(this);
        adapter.setShowFooter(true);

        LinearLayoutManager layoutManager = new WrapLinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mDividerItemDecoration = new DividerItemDecoration(getResources().getColor(R.color.colorAccent)
                , (int) UIUtils.dip2Px(this, 0.5f), LinearLayoutManager.VERTICAL
                , UIUtils.dip2Px(this, 20), UIUtils.dip2Px(this, 20));
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setAdapter(adapter);

        statusView.setBuilder(new LoadingStatusView.Builder(this)
                .setEmptyText(R.string.load_status_empty)
                .setLoadingText(R.string.load_status_loading)
                .setErrorText(R.string.load_status_error, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO
                    }
                })
        );
        statusView.showLoading();
        initData();
    }

    private void initData() {
        refreshLayout.setRefreshing(true);
        HandlerUtils.getInstance().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    String content = "content---" + i;
                    mData.add(content);
                }
                statusView.setVisibility(View.INVISIBLE);
                refreshLayout.setRefreshing(false);
                adapter.setData(mData);
            }
        }, 2000);
    }


    @Override
    public void loadMore() {
        mMoreData.clear();
        adapter.showLoadMoreLoading();
        if (count < 3) {
            HandlerUtils.getInstance().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.resetLoadMoreState();
                    for (int i = 0; i < 10; i++) {
                        String content = "content---" + i;
                        mMoreData.add(content);
                    }
                    adapter.addData(mMoreData);
                }
            }, 1000);
            count++;
        } else {
            adapter.showLoadMoreEmpty();
        }
    }

}
