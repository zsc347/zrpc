package com.scaiz.zrpc.protocol;

import com.scaiz.zrpc.RpcMessage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MessageFuture {
    private RpcMessage requestMessage;
    private long timeout;
    private long start = System.currentTimeMillis();
    private transient CompletableFuture<Object> origin =
            new CompletableFuture<>();

    public void setResult(Object obj) {
        origin.complete(obj);
    }

    public RpcMessage getRequest() {
        return requestMessage;
    }

    public Object get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        Object result = null;
        try {
            result = origin.get(timeout, unit);
        } catch (TimeoutException e) {
            throw new TimeoutException("cost " + (System.currentTimeMillis() - start) + "ms");
        }

        if (result instanceof RuntimeException) {
            throw (RuntimeException) result;
        } else if (result instanceof Throwable) {
            throw new RuntimeException((Throwable) result);
        }
        return result;
    }

    public boolean isTimeout() {
        return System.currentTimeMillis() - start > timeout;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
