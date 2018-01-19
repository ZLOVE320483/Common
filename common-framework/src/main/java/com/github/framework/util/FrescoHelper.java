package com.github.framework.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ImageDecodeOptionsBuilder;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.github.framework.ui.RemoteImageView;

/**
 * Created by zlove on 2018/1/17.
 */

public class FrescoHelper {

    public static void bindImage(RemoteImageView draweeView, String url) {
        bindImage(draweeView, url, -1, -1);
    }

    public static void bindImage(RemoteImageView draweeView, String url, int resizeWidth, int resizeHeight) {
        if (draweeView == null || TextUtils.isEmpty(url)) return;
        ResizeOptions resizeOptions = null;
        if (resizeWidth > 0 && resizeHeight > 0) {
            resizeOptions = new ResizeOptions(resizeWidth, resizeHeight);
        }
        Uri uri = Uri.parse(url);
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri);
        if (null != resizeOptions) {
            builder.setResizeOptions(resizeOptions);
        }
        ImageRequest imageRequest = builder.build();
        DraweeController controller = Fresco.newDraweeControllerBuilder().setOldController(draweeView.getController())
                .setImageRequest(imageRequest)
                .build();
        draweeView.setController(controller);
    }

    public static ImageRequest createImageRequest(String imgUrl, ResizeOptions options, Postprocessor postprocessor) {
        if (TextUtils.isEmpty(imgUrl)) {
            return null;
        }
        ImageRequest imageRequest;
        ImageDecodeOptionsBuilder b = new ImageDecodeOptionsBuilder();
        b.setBitmapConfig(Bitmap.Config.RGB_565);
        b.setDecodeAllFrames(false);
        ImageDecodeOptions imageDecodeOptions = new ImageDecodeOptions(b);
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imgUrl))
                .setImageDecodeOptions(imageDecodeOptions);
        if (postprocessor != null) {
            builder.setPostprocessor(postprocessor);
            if (null != options) {
                builder.setResizeOptions(options);
            }
        }
        imageRequest = builder.build();
        return imageRequest;
    }

    public static ControllerListener<ImageInfo> createMonitorListener(final ControllerListener<ImageInfo> controllerListener, final Uri uri, final Context context, final String imgUrl) {

        return new ControllerListener<ImageInfo>() {
            @Override
            public void onSubmit(String id, Object callerContext) {
                if (controllerListener != null) {
                    controllerListener.onSubmit(id, callerContext);
                }
            }

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                if (controllerListener != null) {
                    controllerListener.onFinalImageSet(id, imageInfo, animatable);
                }
                Worker.postWorker(new Runnable() {
                    @Override
                    public void run() {
                        // TODO 上报错误
                    }
                });
            }

            @Override
            public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                if (controllerListener != null) {
                    controllerListener.onIntermediateImageSet(id, imageInfo);
                }
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                if (controllerListener != null) {
                    controllerListener.onIntermediateImageFailed(id, throwable);
                }
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                if (controllerListener != null) {
                    controllerListener.onFailure(id, throwable);
                }
            }

            @Override
            public void onRelease(String id) {
                if (controllerListener != null) {
                    controllerListener.onRelease(id);
                }
            }
        };
    }
}
