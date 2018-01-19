package com.github.framework.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by zlove on 2018/1/17.
 */

public class BorderLayout extends LinearLayout {

    private boolean mSelected = false;
    protected float mBorderWidth = 3;
    protected float mBorderCornerRadius = 6;

    @ColorInt
    protected int mSelectedColor = Color.YELLOW;
    protected int mUnSelectedColor = Color.WHITE;

    private Paint mBorderPaint;

    public BorderLayout(Context context) {
        this(context, null);
    }

    public BorderLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        if (mBorderPaint == null) {
            mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBorderPaint.setColor(mUnSelectedColor);
        }
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }

    public void setStateColor(int selectedColor, int unSelectedColor) {
        mSelectedColor = selectedColor;
        mUnSelectedColor = unSelectedColor;
        mBorderPaint.setColor(mUnSelectedColor);
    }

    private void drawBorder(Canvas canvas) {
        Rect rect = new Rect();
        getDrawingRect(rect);
        RectF rectF = new RectF();
        rectF.left = rect.left + mBorderWidth;
        rectF.right = rect.right - mBorderWidth;
        rectF.top = rect.top + mBorderWidth;
        rectF.bottom = rect.bottom - mBorderWidth;
        canvas.drawRoundRect(rectF, mBorderCornerRadius, mBorderCornerRadius, mBorderPaint);
    }

    public void setSelected(boolean selected) {
        if (mSelected == selected) {
            return;
        }
        mSelected = selected;
        if (mSelected) {
            mBorderPaint.setColor(mSelectedColor);
        } else {
            mBorderPaint.setColor(mUnSelectedColor);
        }
        postInvalidate();
    }

    public boolean isSelected() {
        return mSelected;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBorder(canvas);
    }
}
