package com.github.framework.fresco.frame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Pair;

import com.facebook.imagepipeline.animated.base.AnimatedImage;
import com.facebook.imagepipeline.animated.base.AnimatedImageFrame;
import com.facebook.imagepipeline.image.CloseableAnimatedImage;
import com.github.framework.analysis.CrashlyticsWrapper;
import com.github.framework.fresco.BitmapCacheManager;
import com.github.framework.fresco.TmpBitmapCache;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by zlove on 2018/1/17.
 */

public class FirstFrameProcessor {

    private final TmpBitmapCache mTmpBitmapCache = new TmpBitmapCache();
    private FrameSerialExecutorService mExecutorService;
    private static final FirstFrameProcessor sInstance = new FirstFrameProcessor();

    public static FirstFrameProcessor inst() {
        return sInstance;
    }

    private void ensureExecutor() {
        if (mExecutorService == null) {
            mExecutorService = new FrameSerialExecutorService(Executors.newSingleThreadExecutor(), 10);
        }
    }

    public void renderFirstFrame(final String key, final CloseableAnimatedImage imageInfo) {
        ensureExecutor();
        if (key == null || imageInfo == null || imageInfo.getImageResult() == null) return;
        final AnimatedImage image = imageInfo.getImageResult().getImage();
        if (image == null) return;
        final AnimatedImageFrame frame = image.getFrame(0);
        if (frame == null) return;
        final Task<Object> feature = Task.call(
                new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        return doRender(key, frame, imageInfo.getWidth(), image.getHeight());
                    }
                }, mExecutorService);
        feature.continueWith(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
                onFutureFinished(task.getResult());
                return null;
            }
        });
    }

    private Pair<String, Bitmap> doRender(String key, AnimatedImageFrame frame, int width, int height) {
        if (frame == null || width == 0 || height == 0) return null;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        if (bitmap == null) return null;
        Bitmap tmpBitmap = mTmpBitmapCache.getCachedBitmap(width, height);
        tmpBitmap.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(bitmap);
        try {
            frame.renderFrame(width, height, tmpBitmap);
        } catch (Exception e) {
            CrashlyticsWrapper.logException(e);
            return null;
        }
        canvas.drawBitmap(tmpBitmap, 0, 0, null);
        return new Pair<>(key, bitmap);
    }

    private void onFutureFinished(Object object) {
        Pair<String, Bitmap> pair = (Pair<String, Bitmap>) object;
        if (pair != null && pair.second != null) {
            BitmapCacheManager.get().addCache(pair.first, new BitmapDrawable(pair.second));
        }
    }

}
