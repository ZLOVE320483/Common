package com.github.common.ui.adapter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zlove on 2018/1/15.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private int mColor;
    private int mDividerSize;
    private int mOrientation;
    int mPos = -1;
    //需要特殊处理的分割线的宽度
    int mWidth;

    private float mStartMargin;
    private float mEndMargin;


    public DividerItemDecoration(int color, int dividerSize, int orientation) {
        this(color, dividerSize, orientation, 0, 0);
    }

    public DividerItemDecoration(int color, int dividerSize, int orientation, float startMargin, float endMargin) {
        mColor = color;
        mDividerSize = dividerSize;
        mOrientation = orientation;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);
        mStartMargin = startMargin;
        mEndMargin = endMargin;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVerticalDivider(c, parent);
        } else {
            drawHorizontalDivider(c, parent);
        }
    }

    private void drawHorizontalDivider(Canvas c, RecyclerView parent) {
        int top = 0;
        int bottom = parent.getMeasuredHeight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int pos = parent.getChildAdapterPosition(child);
            if (pos > 0 && pos < childCount - 1 && pos != (mPos + 1)) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                int right = child.getLeft() + layoutParams.leftMargin;
                int left = right - mDividerSize;
                c.drawRect(left, top, left, top + mStartMargin, mPaint);
                c.drawRect(right, bottom - mEndMargin, right, bottom, mPaint);
            }
        }
    }

    private void drawVerticalDivider(Canvas c, RecyclerView parent) {
        final int left = 0;
        final int right = parent.getMeasuredWidth();
        int childCount = parent.getChildCount();
        int count = parent.getAdapter().getItemCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int pos = parent.getChildAdapterPosition(child);
            if (pos > 0 && pos < count - 1 && pos != (mPos + 1)) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int bottom = child.getTop() + layoutParams.topMargin;
                final int top = bottom - mDividerSize;
                c.drawRect(left, top, left + mStartMargin, bottom, mPaint);
                c.drawRect(right - mEndMargin, top, right, bottom, mPaint);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, (pos == mPos ? mWidth : mDividerSize));
        } else {
            outRect.set(0, 0, (pos == mPos ? mWidth : mDividerSize), 0);
        }
    }

    //设置position位置的分割线的宽度
    public void setDivider(int position, int width) {
        this.mPos = position;
        this.mWidth = width;
    }
}
