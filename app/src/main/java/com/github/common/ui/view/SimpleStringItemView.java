package com.github.common.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.common.R;

/**
 * Created by zlove on 2018/1/14.
 */

public class SimpleStringItemView extends LinearLayout {

    private TextView tvName;

    public SimpleStringItemView(Context context) {
        this(context, null);
    }

    public SimpleStringItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleStringItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_simple_string, this);
        setOrientation(VERTICAL);
        tvName = (TextView) findViewById(R.id.name);
    }

    public void bindData(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        tvName.setText(content);
    }
}
