package com.github.framework.util;

import android.app.Application;

/**
 * Created by zlove on 2018/1/16.
 */

public class AppProvider {
    private static Application app;

    public static void initApp(Application application) {
        app = application;
    }

    public static Application getApp() {
        return app;
    }
}
