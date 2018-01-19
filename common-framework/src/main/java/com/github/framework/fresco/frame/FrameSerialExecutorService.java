package com.github.framework.fresco.frame;

import com.facebook.common.executors.ConstrainedExecutorService;

import java.util.concurrent.Executor;

/**
 * Created by zlove on 2018/1/17.
 */

public class FrameSerialExecutorService extends ConstrainedExecutorService {

    public FrameSerialExecutorService(Executor executor, int fixSize) {
        super("FixSizeSerialExecutor", 1, executor, new FrameBlockingQueue<Runnable>(fixSize));
    }

    @Override
    public synchronized void execute(Runnable runnable) {
        super.execute(runnable);
    }
}
