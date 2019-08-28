package com.scaiz.zrpc.netty;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.NettyRuntime;

public class NettyServerConfig {

    private int port = 8089;

    private String bossThreadPrefix = "Server_Boss";
    private int bossThreadSize = NettyRuntime.availableProcessors();

    private String workerThreadPrefix = "Server_Worker";
    private int workerThreadSize = NettyRuntime.availableProcessors();

    private int soBackLogSize = 1024;
    private int serverSocketSendBufSize = 150 * 1024;
    private int serverSocketRecvBufSize = 150 * 1024;

    private int writeBufferHighWaterMark = 64 * 1024 * 1024;
    private int writeBufferLowWaterMark = 1024 * 1024;


    private int maxReadIdleSeconds = 60 * 60;


    private int serverShutdownWaitTime = 10; // s

    protected static int WORKER_THREAD_SIZE = NettyRuntime.availableProcessors();

    private boolean enableServerPooledByteBufferAllocator;


    public static final PooledByteBufAllocator DIRECT_BYTE_BUF_ALLOCATOR =
            new PooledByteBufAllocator(true,
                    WORKER_THREAD_SIZE,
                    WORKER_THREAD_SIZE,
                    2048 * 64,
                    10,
                    512,
                    256,
                    64,
                    true,
                    0);


    public int getBossThreadSize() {
        return bossThreadSize;
    }

    public void setBossThreadSize(int bossThreadSize) {
        this.bossThreadSize = bossThreadSize;
    }

    public int getWorkerThreadSize() {
        return workerThreadSize;
    }

    public void setWorkerThreadSize(int workerThreadSize) {
        this.workerThreadSize = workerThreadSize;
    }

    public String getWorkerThreadPrefix() {
        return workerThreadPrefix;
    }

    public String getBossThreadPrefix() {
        return bossThreadPrefix;
    }

    public void setBossThreadPrefix(String bossThreadPrefix) {
        this.bossThreadPrefix = bossThreadPrefix;
    }

    public void setWorkerThreadPrefix(String workerThreadPrefix) {
        this.workerThreadPrefix = workerThreadPrefix;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSoBackLogSize() {
        return soBackLogSize;
    }

    public void setSoBackLogSize(int soBackLogSize) {
        this.soBackLogSize = soBackLogSize;
    }

    public int getServerSocketSendBufSize() {
        return serverSocketSendBufSize;
    }

    public void setServerSocketSendBufSize(int serverSocketSendBufSize) {
        this.serverSocketSendBufSize = serverSocketSendBufSize;
    }

    public int getServerSocketRecvBufSize() {
        return serverSocketRecvBufSize;
    }

    public void setServerSocketRecvBufSize(int serverSocketRecvBufSize) {
        this.serverSocketRecvBufSize = serverSocketRecvBufSize;
    }

    public int getWriteBufferHighWaterMark() {
        return writeBufferHighWaterMark;
    }

    public void setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
        this.writeBufferHighWaterMark = writeBufferHighWaterMark;
    }

    public int getWriteBufferLowWaterMark() {
        return writeBufferLowWaterMark;
    }

    public void setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
        this.writeBufferLowWaterMark = writeBufferLowWaterMark;
    }

    public int getMaxReadIdleSeconds() {
        return maxReadIdleSeconds;
    }

    public void setMaxReadIdleSeconds(int maxReadIdleSeconds) {
        this.maxReadIdleSeconds = maxReadIdleSeconds;
    }

    public boolean isEnableServerPooledByteBufferAllocator() {
        return enableServerPooledByteBufferAllocator;
    }

    public void setEnableServerPooledByteBufferAllocator(boolean enableServerPooledByteBufferAllocator) {
        this.enableServerPooledByteBufferAllocator = enableServerPooledByteBufferAllocator;
    }

    public int getServerShutdownWaitTime() {
        return serverShutdownWaitTime;
    }
}
