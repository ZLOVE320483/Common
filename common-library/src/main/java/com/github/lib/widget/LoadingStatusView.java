package com.github.lib.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.lib.R;
import com.github.lib.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlove on 2018/1/10.
 */

public class LoadingStatusView extends FrameLayout {

    private final String TAG = LoadingStatusView.class.getSimpleName();
    List<View> mStatusViews = new ArrayList<>(3);

    public final static int STATUS_NONE = -1;
    public final static int STATUS_LOADING = 0;
    public final static int STATUS_EMPTY = 1;
    public final static int STATUS_ERROR = 2;

    private int mStatus = STATUS_NONE;


    public LoadingStatusView(@NonNull Context context) {
        this(context, null);
    }

    public LoadingStatusView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingStatusView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBuilder(Builder builder) {
        if (builder == null) {
            builder = Builder.createDefaultBuilder(getContext());
        }
        mStatusViews.clear();
        mStatusViews.add(builder.mLoadingView);
        mStatusViews.add(builder.mEmptyView);
        mStatusViews.add(builder.mErrorView);
        removeAllViews();
        for (int i = 0; i < mStatusViews.size(); i++) {
            View view = mStatusViews.get(i);
            if (view == null) {
                continue;
            }
            view.setVisibility(INVISIBLE);
            addView(view);
        }
    }

    public Builder newBuilder() {
        Builder builder = new Builder(getContext());
        builder.mLoadingView = mStatusViews.get(0);
        builder.mEmptyView = mStatusViews.get(1);
        builder.mErrorView = mStatusViews.get(2);
        return builder;
    }

    public void reset() {
        if (mStatus == STATUS_NONE) {
            return;
        }
        mStatusViews.get(mStatus).setVisibility(View.INVISIBLE);
        mStatus = STATUS_NONE;
    }

    public boolean isReset() {
        return mStatus == STATUS_NONE;
    }

    public void showLoading() {
        setStatus(STATUS_LOADING);
    }

    public void showEmpty() {
        setStatus(STATUS_EMPTY);
    }

    public void showError() {
        setStatus(STATUS_ERROR);
    }

    public void setStatus(int status) {
        if (mStatus == status) {
            return;
        }
        if (mStatus >= 0) {
            mStatusViews.get(mStatus).setVisibility(View.INVISIBLE);
        }
        if (status >= 0) {
            View view = mStatusViews.get(status);
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }
        mStatus = status;
    }

    public static class Builder {
        Context mContext;
        View mLoadingView;
        View mEmptyView;
        View mErrorView;
        int mTextColor;

        public Builder setTextColor(int color) {
            mTextColor = color;
            return this;
        }

        public Builder(Context context) {
            if (null == context) {
                throw new IllegalArgumentException("LoadingStatusView.Builder:Context can not be null");
            }
            mContext = context;
        }

        public Builder setLoadingView(View loadingView) {
            mLoadingView = loadingView;
            mLoadingView.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));
            return this;
        }

        public Builder setLoadingText(@StringRes int resId) {
            if (mLoadingView instanceof LoadLayout) {
                LoadLayout loadLayout = (LoadLayout) mLoadingView;
                loadLayout.setLoadingText(resId);
            } else {
                setLoadingText(resId, -1);
            }
            return this;
        }

        public Builder setLoadingText(@StringRes int resId, @ColorInt int color) {
            TextView textView = createDefaultView(resId);
            if (color != -1) {
                textView.setTextColor(color);
            }
            return setLoadingView(textView);
        }

        public Builder setUseProgressBar(int size, boolean isLoadMore) {
            LoadLayout view = (LoadLayout) LayoutInflater.from(mContext).inflate(R.layout.loading_layout, null);
            TextView loading_text = (TextView) view.findViewById(R.id.loading_text);
            if (mTextColor != 0) {
                loading_text.setTextColor(mTextColor);
            }
            if (size >= 0) {
                view.setLoadingViewSize(size);
            }
            if (isLoadMore) {
                loading_text.setTextSize(13);
            } else {
                loading_text.setTextSize(15);
            }
            return setLoadingView(view);
        }

        public Builder setEmptyView(View emptyView) {
            mEmptyView = emptyView;
            mEmptyView.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));
            return this;
        }

        public Builder setEmptyText(@StringRes int resId) {
            if (mEmptyView != null && mEmptyView instanceof TextView) {
                final TextView temp = (TextView) mEmptyView;
                temp.setText(resId);
                return this;
            }
            TextView textView = createDefaultEmptyView();
            textView.setText(resId);
            return setEmptyView(textView);
        }

        public Builder setEmptyText(int resId, int color) {
            TextView textView = createDefaultEmptyView();
            textView.setText(resId);
            textView.setTextColor(color);
            return setEmptyView(textView);
        }

        public Builder setErrorView(View errorView) {
            mErrorView = errorView;
            return this;
        }

        public Builder setErrorText(int resId, OnClickListener retryListener) {
            View view = createDefaultView(resId);
            view.setOnClickListener(retryListener);
            return setErrorView(view);
        }

        private TextView createDefaultEmptyView() {
            TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.view_default_empty_list, null);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setPadding(0, (int) UIUtils.dip2Px(mContext, 100), 0, 0);
            if (mTextColor != 0)
                textView.setTextColor(mTextColor);
            return textView;
        }

        private TextView createDefaultView(@StringRes int resId) {
            TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.load_status_item_view, null);
            textView.setText(resId);
            if (mTextColor != 0)
                textView.setTextColor(mTextColor);
            return textView;
        }

//        private CircularProgressView createProgressView() {
//            return (CircularProgressView) LayoutInflater.from(mActivity)
//                    .inflate(R.layout.load_status_item_progressbar, null);
//        }

        /**
         * create default Builder instance.
         *
         * @return the Builder instance
         */
        public static Builder createDefaultBuilder(Context context) {
            return new Builder(context).setEmptyText(R.string.load_status_empty)
                    .setUseProgressBar((int) UIUtils.dip2Px(context, 15), true)
                    .setLoadingText(R.string.load_status_loading)
                    .setErrorText(R.string.load_status_error, null);
        }
    }
}
