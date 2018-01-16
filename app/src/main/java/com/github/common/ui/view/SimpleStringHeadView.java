package com.github.common.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.github.common.R;

/**
 * Created by zlove on 2018/1/14.
 */

public class SimpleStringHeadView extends LinearLayout {

    public SimpleStringHeadView(Context context) {
        this(context, null);
    }

    public SimpleStringHeadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleStringHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.head_simple_string, this);
    }
}
