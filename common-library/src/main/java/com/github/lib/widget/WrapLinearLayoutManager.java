package com.github.lib.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.github.framework.analysis.CrashlyticsWrapper;

/**
 * Created by zlove on 2018/1/15.
 */

public class WrapLinearLayoutManager extends LinearLayoutManager {

    RecyclerView.Adapter mAdapter;

    public WrapLinearLayoutManager(Context context) {
        super(context);
    }

    public WrapLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public WrapLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onAdapterChanged(RecyclerView.Adapter oldAdapter, RecyclerView.Adapter newAdapter) {
        super.onAdapterChanged(oldAdapter, newAdapter);
        this.mAdapter = newAdapter;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            if (mAdapter != null) {
                CrashlyticsWrapper.catchException(mAdapter.getClass().getName(), wrapIndexOutOfBoundsException(e));
            }
        } catch (IllegalArgumentException e) {
            if (mAdapter != null) {
                CrashlyticsWrapper.catchException(mAdapter.getClass().getName(), wrapIllegalArgumentException(e));
            }
        }
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            return super.scrollVerticallyBy(dy, recycler, state);
        } catch (IndexOutOfBoundsException e) {
            if (mAdapter != null) {
                CrashlyticsWrapper.catchException(mAdapter.getClass().getName(), wrapIndexOutOfBoundsException(e));
            }
        }
        return 0;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            return super.scrollHorizontallyBy(dx, recycler, state);
        } catch (IndexOutOfBoundsException e) {
            if (mAdapter != null) {
                CrashlyticsWrapper.catchException(mAdapter.getClass().getName(), wrapIndexOutOfBoundsException(e));
            }
        }
        return 0;
    }

    IndexOutOfBoundsException wrapIndexOutOfBoundsException(IndexOutOfBoundsException e) {
        IndexOutOfBoundsException oob = e;
        if (mAdapter != null) {
            oob = new IndexOutOfBoundsException(mAdapter.getClass().getName());
            oob.initCause(e);
        }
        return oob;
    }

    IllegalArgumentException wrapIllegalArgumentException(IllegalArgumentException e) {
        IllegalArgumentException iae = e;
        if (mAdapter != null) {
            iae = new IllegalArgumentException(mAdapter.getClass().getName(), e);
        }
        return iae;
    }
}
