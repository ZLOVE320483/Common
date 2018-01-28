package com.github.network.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by zlove on 2018/1/24.
 */

public class BaseApplication extends Application {

    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getAppContext() {
        return instance;
    }
}
