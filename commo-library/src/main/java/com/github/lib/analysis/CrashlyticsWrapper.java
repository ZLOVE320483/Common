package com.github.lib.analysis;

import com.github.lib.BuildConfig;

/**
 * Created by zlove on 2018/1/15.
 */

public class CrashlyticsWrapper {

    private static final String TAG = CrashlyticsWrapper.class.getSimpleName();

    public static volatile boolean isInit = false;

    public static void catchException(Exception e) {
        catchException("", e);
    }

    public static void catchException(String message, Exception e) {
        if (BuildConfig.DEBUG) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException(message, e);
            }
        } else {
            if (isInit) {
                // TODO 上报Crash日志
            }
        }
    }
}
