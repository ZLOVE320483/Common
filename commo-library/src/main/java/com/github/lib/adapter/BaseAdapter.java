package com.github.lib.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.lib.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlove on 2018/1/12.
 */

public abstract class BaseAdapter<T> extends LoadMoreRecyclerViewAdapter {

    protected List<T> mItems;
    protected int mPreviousCount = 0;

    static final String TAG = BaseAdapter.class.getSimpleName();

    public BaseAdapter() {
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mPreviousCount = getItemCount();
                Log.d(TAG, "onChanged");
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                mPreviousCount = getItemCount();
                Log.d(TAG, "onItemRangeChanged() positionStart:" + positionStart + " itemCount:" + itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mPreviousCount = getItemCount();
                Log.d(TAG, "onItemRangeInserted() positionStart:" + positionStart + " itemCount:" + itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                mPreviousCount = getItemCount();
                Log.d(TAG, "onItemRangeRemoved() positionStart:" + positionStart + " itemCount:" + itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                mPreviousCount = getItemCount();
                Log.d(TAG, "onItemRangeMoved() fromPosition:" + fromPosition + " toPosition:" + toPosition + " itemCount:" + itemCount);
            }
        });
    }

    public void setData(List<T> list) {
        mItems = list;
        notifyDataSetChanged();
    }

    @Override
    protected void onShowFooterChanged(boolean newShowFooter) {
        if (newShowFooter) {
            notifyItemInserted(getItemCount());
        } else {
            notifyItemRemoved(getItemCount() - 1);
        }
        mPreviousCount = getItemCount();
    }

    public List<T> getData() {
        return mItems;
    }

    public void clearData() {
        if (mItems != null) {
            mItems.clear();
            notifyDataSetChanged();
        }
    }

    public void setDataAfterLoadMore(List<T> list) {
        mItems = list;
        // fix: IndexOutOfBoundsException during RecyclerView.onLayoutChildren().
        // If item count changes: 8 -> 13, we should notifyItemRangeInserted(7, 5), not (8, 6).
        if (isShowFooter()) {
            notifyItemRangeInserted(mPreviousCount - 1, getItemCount() - mPreviousCount);

            // fix: notify footer changed.
            // This is wired. RecyclerView does not call onBindViewHolder() on footer
            // when notifyItemRangeInserted() animation is not completed.
            notifyItemChanged(getItemCount() - 1);
        } else {
            // If no footer is shown
            notifyItemRangeInserted(mPreviousCount, getItemCount() - mPreviousCount);
        }
    }

    public void setDataAfterLoadLatest(List<T> list) {
        mItems = list;
        notifyItemRangeInserted(0, getItemCount() - mPreviousCount);
    }

    public void insertData(T item, int position) {
        if (mItems == null) {
            mItems = new ArrayList<>();  // ArrayList has better performance with less overhead in most cases.
        }
        mItems.add(position, item);
        notifyItemInserted(position);
    }

    public void addData(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        if (mItems == null) {
            mItems = new ArrayList<>();  // ArrayList has better performance with less overhead in most cases.
        }
        mItems.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getBasicItemCount() {
        return mItems == null ? 0 : mItems.size();
    }
}
