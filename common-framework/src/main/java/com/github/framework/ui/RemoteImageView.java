package com.github.framework.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by zlove on 2018/1/16.
 */

public class RemoteImageView extends SimpleDraweeView {

    public RemoteImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init();
    }

    public RemoteImageView(Context context) {
        super(context);
        init();
    }

    public RemoteImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RemoteImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RemoteImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    protected void init() {

    }
}
