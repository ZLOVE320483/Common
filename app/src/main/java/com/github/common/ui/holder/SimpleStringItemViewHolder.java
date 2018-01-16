package com.github.common.ui.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.common.ui.view.SimpleStringItemView;

/**
 * Created by zlove on 2018/1/14.
 */

public class SimpleStringItemViewHolder extends RecyclerView.ViewHolder {

    private SimpleStringItemView simpleStringItemView;

    public SimpleStringItemViewHolder(View itemView) {
        super(itemView);
        simpleStringItemView = (SimpleStringItemView) itemView;
    }

    public void bind(String content) {
        simpleStringItemView.bindData(content);
    }
}
