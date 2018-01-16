package com.github.lib.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lib.R;

/**
 * Created by zlove on 2018/1/10.
 */

public class LoadLayout extends LinearLayout {

    private ImageView mLoadingView;
    private TextView mLoadingTextView;
    private Animation mLoadingAnim;

    public LoadLayout(Context context) {
        this(context, null);
    }

    public LoadLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLoadingView = (ImageView) findViewById(R.id.loading_progress);
        mLoadingTextView = (TextView) findViewById(R.id.loading_text);
        mLoadingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotate);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            mLoadingView.startAnimation(mLoadingAnim);
        } else {
            mLoadingView.clearAnimation();
        }
    }

    public void setLoadingViewSize(int size) {
        LinearLayout.LayoutParams params = new LayoutParams(size, size);
        params.gravity = Gravity.CENTER_VERTICAL;
        mLoadingView.setLayoutParams(params);
    }

    public void showLoadingText() {
        mLoadingTextView.setVisibility(View.VISIBLE);
    }

    public void setLoadingText(int resId) {
        mLoadingTextView.setText(resId);
    }

    public void setLoadingText(String text) {
        mLoadingTextView.setText(text);
    }
}
