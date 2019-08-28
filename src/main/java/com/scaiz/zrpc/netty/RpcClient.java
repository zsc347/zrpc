package com.scaiz.zrpc.netty;

import com.scaiz.zrpc.common.NamedThreadFactory;
import com.scaiz.zrpc.common.WorkThreadMode;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class RpcClient extends AbstractRpcRemoteClient {

    private static final int DEFAULT_CLIENT_WORKER_THREADS = WorkThreadMode.Auto.getValue();

    private static final long KEEP_ALIVE_TIME = Integer.MAX_VALUE;

    private static final int MAX_QUEUE_SIZE = 2000;

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public RpcClient(NettyClientConfig nettyClientConfig, ThreadPoolExecutor messageExecutor) {
        super(nettyClientConfig, messageExecutor);
    }

    public static RpcClient getInstance() {
        NettyClientConfig nettyClientConfig = new NettyClientConfig();
        ThreadPoolExecutor messageExecutor = new ThreadPoolExecutor(
                DEFAULT_CLIENT_WORKER_THREADS,
                DEFAULT_CLIENT_WORKER_THREADS,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(MAX_QUEUE_SIZE),
                new NamedThreadFactory(
                        "ClientWorkerThread",
                        DEFAULT_CLIENT_WORKER_THREADS
                ), new ThreadPoolExecutor.CallerRunsPolicy());
        return new RpcClient(nettyClientConfig, messageExecutor);
    }

    @Override
    public void init() {
        if (initialized.compareAndSet(false, true)) {
            super.init();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        initialized.getAndSet(false);
    }
}
