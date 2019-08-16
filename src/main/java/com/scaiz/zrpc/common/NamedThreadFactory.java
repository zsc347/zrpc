package com.scaiz.zrpc.common;

import io.netty.util.concurrent.FastThreadLocalThread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {

    private final boolean makeDaemon;
    private final String prefix;
    private final int totalSize;
    private final AtomicInteger counter = new AtomicInteger(0);


    public NamedThreadFactory(boolean makeDaemon, String prefix, int totalSize) {
        this.makeDaemon = makeDaemon;
        this.prefix = prefix;
        this.totalSize = totalSize;
    }

    @Override
    public Thread newThread(Runnable r) {
        String name = prefix + "_" + counter.incrementAndGet();
        if (totalSize > 0) {
            name += "_" + totalSize;
        }
        Thread thread = new FastThreadLocalThread(r, name);
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        thread.setDaemon(makeDaemon);
        return thread;
    }
}
