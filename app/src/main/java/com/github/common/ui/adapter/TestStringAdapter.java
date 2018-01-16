package com.github.common.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.github.common.ui.holder.SimpleStringItemViewHolder;
import com.github.common.ui.view.SimpleStringItemView;

import java.util.List;

/**
 * Created by zlove on 2018/1/16.
 */

public class TestStringAdapter extends RecyclerView.Adapter<SimpleStringItemViewHolder> {

    private List<String> mData;

    public TestStringAdapter(List<String> mData) {
        this.mData = mData;
    }

    @Override
    public SimpleStringItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SimpleStringItemView itemView = new SimpleStringItemView(parent.getContext());
        return new SimpleStringItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SimpleStringItemViewHolder holder, int position) {
        if (holder instanceof SimpleStringItemViewHolder) {
            holder.bind(mData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<String> data) {
        this.mData = data;
        notifyDataSetChanged();
    }
}
