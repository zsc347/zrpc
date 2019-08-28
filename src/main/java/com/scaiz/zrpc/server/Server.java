package com.scaiz.zrpc.server;

import com.scaiz.zrpc.common.NamedThreadFactory;
import com.scaiz.zrpc.netty.RpcServer;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {

    private static final int MIN_SERVER_POOL_SIZE = 100;
    private static final int MAX_SERVER_POOL_SIZE = 500;
    private static final int MAX_TASK_QUEUE_SIZE = 20000;
    private static final int KEEP_ALIVE_TIME = 500;

    private static final ThreadPoolExecutor WORKING_THREADS =
            new ThreadPoolExecutor(MIN_SERVER_POOL_SIZE,
                    MAX_SERVER_POOL_SIZE,
                    KEEP_ALIVE_TIME,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(MAX_TASK_QUEUE_SIZE),
                    new NamedThreadFactory("ServerHandlerThread",
                            MAX_SERVER_POOL_SIZE),
                    new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer(WORKING_THREADS);
        rpcServer.setHost("127.0.0.1");
        rpcServer.setPort(8089);
        rpcServer.init();
    }
}
