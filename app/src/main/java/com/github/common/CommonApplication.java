package com.github.common;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.framework.util.AppProvider;

/**
 * Created by zlove on 2018/1/17.
 */

public class CommonApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppProvider.initApp(this);
        initFresco();
    }

    private void initFresco() {
        if (Fresco.hasBeenInitialized()) {
            return;
        }
        Fresco.initialize(this);
    }
}
