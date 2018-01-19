package com.github.framework.ui;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScaleTypeDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.CloseableAnimatedImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.github.framework.fresco.BitmapCacheManager;
import com.github.framework.fresco.frame.FirstFrameProcessor;
import com.github.framework.ui.listener.AnimationListener;
import com.github.framework.util.FrescoHelper;

import java.lang.ref.WeakReference;

/**
 * Created by zlove on 2018/1/16.
 */

public class AnimatedImageView extends RemoteImageView {

    private WeakReference<AnimationListener> animationListenerWeakReference;
    private OnImageLoadFinishListener loadFinishListener;

    public AnimatedImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init();
    }

    public AnimatedImageView(Context context) {
        super(context);
        init();
    }

    public AnimatedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimatedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AnimatedImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private String imgUrl;

    public void setAnimationListener(AnimationListener animationListener) {
        this.animationListenerWeakReference = new WeakReference<>(animationListener);
    }

    public void bindImage(String imgUrl) {
        if (TextUtils.isEmpty(imgUrl)) {
            return;
        }
        this.imgUrl = imgUrl;

        getHierarchy().setBackgroundImage(null);
        BitmapDrawable firstPreviewDrawable = BitmapCacheManager.get().getCache(imgUrl);
        if (firstPreviewDrawable != null) {
            getHierarchy().setBackgroundImage(new ScaleTypeDrawable(firstPreviewDrawable, ScalingUtils.ScaleType.CENTER_CROP));
        }
        ImageRequest imageRequest = FrescoHelper.createImageRequest(imgUrl, null, null);
        if (imageRequest == null) {
            return;
        }
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder().setOldController(getController())
                .setImageRequest(imageRequest);
        Context context = null;
        if (getContext() != null) {
            context = getContext().getApplicationContext();
        }
        builder.setControllerListener(FrescoHelper.createMonitorListener(mControllerListener, imageRequest.getSourceUri(), context, imgUrl));
        DraweeController controller = builder.build();
        setController(controller);
    }

    private boolean mAttached;
    private boolean mAnimatedReady;
    private boolean mUserVisibleHint;

    public String getUrl() {
        return imgUrl;
    }

    protected ControllerListener<ImageInfo> mControllerListener = new BaseControllerListener<ImageInfo>() {

        @Override
        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
            if (loadFinishListener != null) {
                loadFinishListener.onLoadFinish();
            }

            String key = getUrl();
            if (!TextUtils.isEmpty(key) && BitmapCacheManager.get().getCache(key) == null && imageInfo instanceof CloseableAnimatedImage) {
                FirstFrameProcessor.inst().renderFirstFrame(key, (CloseableAnimatedImage) imageInfo);
            }

            if (animatable != null) {
                mAnimatedReady = true;
                if (mAttached) {
                    tryStartAnimation();
                }
            } else {
                mAnimatedReady = false;
            }
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            super.onFailure(id, throwable);
            mAnimatedReady = false;
        }

        @Override
        public void onRelease(String id) {
            super.onRelease(id);
            mAnimatedReady = false;
        }

        @Override
        public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            super.onIntermediateImageSet(id, imageInfo);
            mAnimatedReady = false;
        }
    };

    public void tryStartAnimation() {
        if (getController() == null) return;
        if (mAttached && mAnimatedReady && mUserVisibleHint) {
            Animatable animation = getController().getAnimatable();
            if (animation != null && !animation.isRunning()) {
                animation.start();
                //TODO
                if (animationListenerWeakReference != null && animationListenerWeakReference.get() != null) {
                    animationListenerWeakReference.get().started();
                }
            }
        }
    }

    public void setAttached(boolean attached) {
        this.mAttached = attached;
    }

    public void setUserVisibleHint(boolean userVisibleHint) {
        this.mUserVisibleHint = userVisibleHint;
    }

    public void tryStopAnimation() {
        if (getController() == null) return;
        Animatable animation = getController().getAnimatable();
        if (animation != null && animation.isRunning()) {
            animation.stop();
            //TODO
            if (animationListenerWeakReference != null && animationListenerWeakReference.get() != null) {
                animationListenerWeakReference.get().stopped();
            }
        }
    }

    public interface OnImageLoadFinishListener {
        void onLoadFinish();
    }

    public void setImageLoadFinishListener(OnImageLoadFinishListener listener) {
        loadFinishListener = listener;
    }
}
