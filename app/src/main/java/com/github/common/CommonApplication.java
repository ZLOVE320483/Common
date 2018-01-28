package com.github.common;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.framework.util.AppProvider;
import com.github.network.app.BaseApplication;

/**
 * Created by zlove on 2018/1/17.
 */

public class CommonApplication extends BaseApplication {

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
