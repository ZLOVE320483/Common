package com.github.framework.fresco;

import android.graphics.Bitmap;
import android.util.SparseArray;

import java.lang.ref.SoftReference;

/**
 * Created by zlove on 2018/1/17.
 */

public class TmpBitmapCache {

    private final SparseArray<SoftReference<Bitmap>> mTmpBitmaps = new SparseArray<>();

    public Bitmap getCachedBitmap(int width, int height) {
        int key = buildTempKey(width, height);
        SoftReference<Bitmap> reference = mTmpBitmaps.get(key);
        Bitmap bitmap = reference == null ? null : reference.get();
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(
                    width,
                    height,
                    Bitmap.Config.ARGB_8888);
            mTmpBitmaps.put(key, new SoftReference<>(bitmap));
        }
        return bitmap;
    }

    private int buildTempKey(int width, int height) {
        return (width << 10) + height;
    }
}
