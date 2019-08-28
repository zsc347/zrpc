package com.scaiz.zrpc.netty;

public class NettyClientConfig {

    private String serverAddress = "127.0.0.1";
    private int serverPort = 8089;

    private int clientSelectorThreadSize = 10;
    private String clientSelectorThreadPrefix = "NettyClientSelector";

    private int connectTimeoutMillis = 10000;

    private int clientSocketSndBufSize = 150 * 1024;
    private int clientSocketRcvBufSize = 150 * 1024;

    private int maxReadIdleSeconds = 0;
    private int maxWriteIdleSeconds = 0;
    private int maxAllIdleSeconds = 0;

    private int rpcRequestTimeout = 30*1000;


    public NettyClientConfig() {
    }


    public int getClientSelectorThreadSize() {
        return clientSelectorThreadSize;
    }

    public void setClientSelectorThreadSize(int clientSelectorThreadSize) {
        this.clientSelectorThreadSize = clientSelectorThreadSize;
    }

    public String getClientSelectorThreadPrefix() {
        return clientSelectorThreadPrefix;
    }

    public void setClientSelectorThreadPrefix(String clientSelectorThreadPrefix) {
        this.clientSelectorThreadPrefix = clientSelectorThreadPrefix;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public int getClientSocketSndBufSize() {
        return clientSocketSndBufSize;
    }

    public void setClientSocketSndBufSize(int clientSocketSndBufSize) {
        this.clientSocketSndBufSize = clientSocketSndBufSize;
    }

    public int getClientSocketRcvBufSize() {
        return clientSocketRcvBufSize;
    }

    public void setClientSocketRcvBufSize(int clientSocketRcvBufSize) {
        this.clientSocketRcvBufSize = clientSocketRcvBufSize;
    }

    public int getMaxReadIdleSeconds() {
        return maxReadIdleSeconds;
    }

    public void setMaxReadIdleSeconds(int maxReadIdleSeconds) {
        this.maxReadIdleSeconds = maxReadIdleSeconds;
    }

    public int getMaxWriteIdleSeconds() {
        return maxWriteIdleSeconds;
    }

    public void setMaxWriteIdleSeconds(int maxWriteIdleSeconds) {
        this.maxWriteIdleSeconds = maxWriteIdleSeconds;
    }

    public int getMaxAllIdleSeconds() {
        return maxAllIdleSeconds;
    }

    public void setMaxAllIdleSeconds(int maxAllIdleSeconds) {
        this.maxAllIdleSeconds = maxAllIdleSeconds;
    }

    public int getRpcRequestTimeout() {
        return rpcRequestTimeout;
    }

    public void setRpcRequestTimeout(int rpcRequestTimeout) {
        this.rpcRequestTimeout = rpcRequestTimeout;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
}
