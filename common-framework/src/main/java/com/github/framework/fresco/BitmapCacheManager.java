package com.github.framework.fresco;

import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.LruCache;

import com.github.framework.util.AppProvider;
import com.github.framework.util.Utils;

/**
 * Created by zlove on 2018/1/16.
 */

public class BitmapCacheManager {

    private LruCache<String, BitmapDrawable> gifBitmapCache;
    private int mTotalSize;

    private BitmapCacheManager() {
        mTotalSize = Utils.calculateMemoryCacheSize(AppProvider.getApp());
        gifBitmapCache = new LruCache<String, BitmapDrawable>(mTotalSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                if (value.getBitmap() == null) {
                    return 0;
                }
                return value.getBitmap().getRowBytes() * value.getBitmap().getHeight();
            }
        };
    }

    public void addCache(String url, BitmapDrawable bitmap) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        gifBitmapCache.put(url, bitmap);
    }

    public BitmapDrawable getCache(String url) {
        return gifBitmapCache.get(url);
    }

    public static BitmapCacheManager get() {
        return BitmapCacheManager.SingleInstanceHolder.instance;
    }

    private static class SingleInstanceHolder {

        private SingleInstanceHolder() {
        }

        private static BitmapCacheManager instance = new BitmapCacheManager();
    }
}
