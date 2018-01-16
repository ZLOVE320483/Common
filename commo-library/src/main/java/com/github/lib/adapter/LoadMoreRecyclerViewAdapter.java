package com.github.lib.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lib.R;
import com.github.lib.widget.LoadingStatusView;

/**
 * A RecyclerView Adapter that contains a load more footer.
 *
 * @author wuyang
 */
public abstract class LoadMoreRecyclerViewAdapter extends RecyclerViewWithFooterAdapter {

    static final String TAG = LoadMoreRecyclerViewAdapter.class.getSimpleName();

    private int mCurStatus = LoadingStatusView.STATUS_NONE;
    private int mTextColor;
    private int loadEmptyTextResId;
    private TextView textView;
    private long mLoadStartTime = -1L;
    private String mLabel;


    public interface ILoadMore {
        void loadMore();
    }

    private LoadMoreViewHolder mLoadMoreViewHolder;

    public void setLoadMoreListener(ILoadMore loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    private ILoadMore mLoadMoreListener;

    private GridLayoutManager.SpanSizeLookup spanSizeLookup;

    public void setSpanSizeLookup(GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        this.spanSizeLookup = spanSizeLookup;
    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder) {
        ((LoadMoreViewHolder) holder).bind();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {

                    if (getItemViewType(position) == TYPE_FOOTER) {
                        return gridLayoutManager.getSpanCount();
                    } else if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position);
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (mLoadStartTime != -1 && !TextUtils.isEmpty(mLabel)) {
            // TODO
            mLoadStartTime = -1;
        }
    }

    public void setLabel(String label) {
        this.mLabel = label;
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(getItemViewType(holder.getLayoutPosition()) == TYPE_FOOTER);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent) {
        final LoadingStatusView statusView = new LoadingStatusView(parent.getContext());
        int height = getLoadMoreHeight(parent);
        setLoadingPadding(statusView);
        int progressBarSize = parent.getResources().getDimensionPixelSize(R.dimen.default_list_loadmore_progressbar);
        statusView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_default_empty_list, null);
        if (mTextColor != 0) {
            textView.setTextColor(mTextColor);
        }
        if (loadEmptyTextResId != 0) {
            textView.setText(loadEmptyTextResId);
        }
        textView.setGravity(Gravity.CENTER);
        LoadingStatusView.Builder builder = new LoadingStatusView.Builder(parent.getContext())
                .setTextColor(mTextColor)
                .setUseProgressBar(progressBarSize, true)
                .setErrorText(R.string.load_status_click_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mLoadMoreListener != null) {
                            mLoadMoreListener.loadMore();
                        }
                    }
                })
                .setEmptyView(textView);
        statusView.setBuilder(builder);
        mLoadMoreViewHolder = new LoadMoreViewHolder(statusView);
        return mLoadMoreViewHolder;
    }

    protected int getLoadMoreHeight(View view) {
        return view.getResources().getDimensionPixelSize(R.dimen.default_list_loadmore_height);
    }

    protected void setLoadingPadding(View view) {

    }

    @Override
    public int getItemCount() {
        if (getBasicItemCount() == 0) {
            return 0;
        }
        return super.getItemCount();
    }

    public void showLoadMoreLoading() {

        Log.d(TAG, "showLoadMoreLoading()");
        if (mLoadMoreViewHolder != null) {
            mLoadMoreViewHolder.showLoading();
        }
        mCurStatus = LoadingStatusView.STATUS_LOADING;
        if (mLoadStartTime == -1L) {
            mLoadStartTime = System.currentTimeMillis();
        }
    }

    public void showLoadMoreError() {
        if (mLoadMoreViewHolder != null) {
            mLoadMoreViewHolder.showError();
        }
        mCurStatus = LoadingStatusView.STATUS_ERROR;
    }


    public void showLoadMoreEmpty() {
        if (mLoadMoreViewHolder != null) {
            mLoadMoreViewHolder.showEmpty();
        }
        mCurStatus = LoadingStatusView.STATUS_EMPTY;
    }

    public void resetLoadMoreState() {
        Log.d(TAG, "resetLoadMoreState()");
        if (mLoadMoreViewHolder != null) {
            mLoadMoreViewHolder.reset();
        }
        mCurStatus = LoadingStatusView.STATUS_NONE;
        mLoadStartTime = -1L;
    }

    protected class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            Log.i("music_sxg", "LoadMoreViewHolder()");
        }

        public void bind() {
            Log.d(TAG, "bind() status:" + mCurStatus);
            LoadingStatusView statusView = ((LoadingStatusView) itemView);
            statusView.setStatus(mCurStatus);
            if (statusView.isReset()) {
                //处于普通状态, 尝试加载
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.loadMore();
                }
            }
        }

        private void showLoading() {
            LoadingStatusView statusView = ((LoadingStatusView) itemView);
            statusView.showLoading();
        }

        private void showError() {
            LoadingStatusView statusView = ((LoadingStatusView) itemView);
            statusView.showError();
        }

        private void showEmpty() {
            LoadingStatusView statusView = ((LoadingStatusView) itemView);
            statusView.showEmpty();
        }

        private void reset() {
            LoadingStatusView statusView = ((LoadingStatusView) itemView);
            statusView.reset();
        }
    }

    public void setLoaddingTextColor(int color) {
        mTextColor = color;
    }

    public void setLoadEmptyTextResId(int textResId) {
        if (textView != null)
            textView.setText(textResId);
        loadEmptyTextResId = textResId;
    }
}
