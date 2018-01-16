package com.github.lib.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by zlove on 2018/1/10.
 */

public abstract class RecyclerViewWithFooterAdapter extends RecyclerView.Adapter {

    protected static final int TYPE_FOOTER = Integer.MIN_VALUE;
    protected boolean mShowFooter = true;

    public void setShowFooter(boolean showFooter) {
        if (showFooter != mShowFooter) {
            mShowFooter = showFooter;
            onShowFooterChanged(showFooter);
        }
    }

    // mShowFooter may affect the result of getItemCount(). This change is not exposed
    // to subclasses that take care of previous adapter item count.
    //
    // see RecyclerView: IndexOutOfBoundsException: Inconsistency detected. Invalid view holder.
    protected void onShowFooterChanged(boolean newShowFooter) {

    }

    public boolean isShowFooter() {
        return mShowFooter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (TYPE_FOOTER == viewType) {
            return onCreateFooterViewHolder(parent);
        }
        return onCreateBasicViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            onBindFooterViewHolder(holder);
        } else {
            onBindBasicViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mShowFooter ? getBasicItemCount() + 1 : getBasicItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mShowFooter && position == getBasicItemCount()) {
            return TYPE_FOOTER;
        }
        return getBasicItemViewType(position);
    }

    public abstract void onBindBasicViewHolder(RecyclerView.ViewHolder holder, int position);

    public abstract void onBindFooterViewHolder(RecyclerView.ViewHolder holder);

    public abstract RecyclerView.ViewHolder onCreateBasicViewHolder(ViewGroup parent, int viewType);

    public abstract RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent);

    public abstract int getBasicItemCount();

    /**
     * Subclass should return type bigger than 1000.
     *
     * @param position
     * @return
     */
    public int getBasicItemViewType(int position) {
        return 0;
    }
}
