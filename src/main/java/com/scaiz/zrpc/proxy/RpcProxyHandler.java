package com.scaiz.zrpc.proxy;

import com.scaiz.zrpc.rpc.RpcClientInternal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RpcProxyHandler implements InvocationHandler {

    public static final int DEFAULT_RPC_METHOD_CALL_TIMEOUT = 10 * 60 * 1000; // second

    private RpcClientInternal rpcClient;

    public RpcProxyHandler(RpcClientInternal rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcCallMessage callMessage = ProxyFactory.requestToMessage(method, args);
        return rpcClient.sendMsgWithResponse(callMessage, DEFAULT_RPC_METHOD_CALL_TIMEOUT);
    }
}
