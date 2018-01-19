package com.github.framework.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;

/**
 * Created by zlove on 2018/1/17.
 */

public class CircleImageView extends AnimatedImageView {

    public CircleImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init() {
        super.init();
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setRoundAsCircle(true);
        getHierarchy().setRoundingParams(roundingParams);
        getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
    }
}
