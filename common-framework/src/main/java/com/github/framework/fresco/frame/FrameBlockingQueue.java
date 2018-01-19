package com.github.framework.fresco.frame;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by zlove on 2018/1/17.
 */

public class FrameBlockingQueue<E> extends LinkedBlockingDeque<E> {

    private int mFixedSize;

    public FrameBlockingQueue(int fixedSize) {
        mFixedSize = fixedSize;
    }

    @Override
    public boolean offer(E e) {
        synchronized (this) {
            if (size() == mFixedSize) {
                removeLast();
            }
        }
        return super.offerFirst(e);
    }
}
