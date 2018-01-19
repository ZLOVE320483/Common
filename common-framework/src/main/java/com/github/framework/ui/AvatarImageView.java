package com.github.framework.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.github.framework.R;

/**
 * Created by zlove on 2018/1/18.
 */

public class AvatarImageView extends CircleImageView {

    public AvatarImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public AvatarImageView(Context context) {
        super(context);
    }

    public AvatarImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AvatarImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AvatarImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init() {
        super.init();
        getHierarchy().setPlaceholderImage(R.drawable.ic_img_signin_defaultavatar, ScalingUtils.ScaleType.CENTER_CROP);
    }
}
