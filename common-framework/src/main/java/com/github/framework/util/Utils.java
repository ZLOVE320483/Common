package com.github.framework.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Created by zlove on 2018/1/16.
 */

public class Utils {

    public static int calculateMemoryCacheSize(Context context) {
        ActivityManager am = getService(context, Context.ACTIVITY_SERVICE);
        boolean largeHeap = (context.getApplicationInfo().flags & ApplicationInfo.FLAG_LARGE_HEAP) != 0;
        int memoryClass = largeHeap ? am.getLargeMemoryClass() : am.getMemoryClass();
        // Target ~10% of the available heap.
        return (int) (1024L * 1024L * memoryClass / 10);
    }

    static <T> T getService(Context context, String service) {
        return (T) context.getSystemService(service);
    }

}
