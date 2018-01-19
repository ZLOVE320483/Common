package com.github.framework.util;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zlove on 2018/1/17.
 */

public class Worker {

    public static ExecutorService EXECUTOR;
    private static Handler handlerMain;

    public static void destroy() {
        EXECUTOR.shutdown();

        try {
            if (!EXECUTOR.awaitTermination(1, TimeUnit.SECONDS)) {
                EXECUTOR.shutdownNow();
            }
        } catch (Exception e) {
            EXECUTOR.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void postWorker(Runnable r) {
        checkInit();

        try {
            EXECUTOR.execute(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void postMain(Runnable r) {
        checkInit();

        if (handlerMain != null) {
            handlerMain.post(r);
        }
    }

    public static void postMain(Runnable r, int delay) {
        checkInit();

        handlerMain.postDelayed(r, delay);
    }

    private static void checkInit() {
        if (EXECUTOR == null || EXECUTOR.isTerminated()) {
            EXECUTOR = (ExecutorService) AsyncTask.THREAD_POOL_EXECUTOR;
            handlerMain = new Handler(Looper.getMainLooper());
        }
    }

    public static void cancelMain(Runnable r) {
        checkInit();
        handlerMain.removeCallbacks(r);
    }

}
