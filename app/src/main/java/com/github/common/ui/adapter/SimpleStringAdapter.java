package com.github.common.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.github.common.ui.holder.SimpleStringItemViewHolder;
import com.github.common.ui.view.SimpleStringItemView;
import com.github.lib.adapter.BaseAdapter;
import com.github.lib.utils.UIUtils;

/**
 * Created by zlove on 2018/1/14.
 */

public class SimpleStringAdapter extends BaseAdapter<String> {

    public static final int TYPE_NORMAL = 1;

    @Override
    public void onBindBasicViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            if (holder instanceof SimpleStringItemViewHolder) {
                ((SimpleStringItemViewHolder) holder).bind(mItems.get(position));
            }
        } else if (getItemViewType(position) == TYPE_FOOTER) {
            if (holder instanceof LoadMoreViewHolder) {
                ((LoadMoreViewHolder) holder).bind();
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return onCreateFooterViewHolder(parent);
        } else if (viewType == TYPE_NORMAL) {
            SimpleStringItemView simpleStringItemView = new SimpleStringItemView(parent.getContext());
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) UIUtils.dip2Px(parent.getContext(), 84));
            int margin = (int) UIUtils.dip2Px(parent.getContext(), 6);
            lp.setMargins(margin, 0, margin, 0);
            simpleStringItemView.setLayoutParams(lp);
            return new SimpleStringItemViewHolder(simpleStringItemView);
        }
        return null;
    }

    @Override
    public int getBasicItemViewType(int position) {
        if (position == getItemCount()) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }
}
