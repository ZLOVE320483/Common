package com.github.common.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by zlove on 2018/1/14.
 */

public class HandlerUtils {

    private static HandlerUtils instance;
    private Handler mHandler;

    private HandlerUtils() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static HandlerUtils getInstance() {
        if (instance == null) {
            synchronized (HandlerUtils.class) {
                if (instance == null) {
                    instance = new HandlerUtils();
                }
            }
        }
        return instance;
    }

    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    public void postDelayed(Runnable runnable, long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
    }
}
