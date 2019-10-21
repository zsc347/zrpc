package com.scaiz.zrpc.proxy;

import com.scaiz.zrpc.netty.RpcClient;
import com.scaiz.zrpc.registry.ReflectUtil;
import com.scaiz.zrpc.rpc.RpcClientInternal;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class ProxyFactory {

    @SuppressWarnings("unchecked")
    public static  <T> T createProxy(Class<T> clazz, RpcClientInternal rpcClient) {
        return (T) Proxy.newProxyInstance(ProxyFactory.class.getClassLoader(), new Class[]{clazz},
                new RpcProxyHandler(rpcClient));
    }

    static RpcCallMessage requestToMessage(Method method, Object[] args) {
        RpcCallMessage callMessage = new RpcCallMessage();
        callMessage.setClazz(method.getDeclaringClass().getName());
        callMessage.setMethod(ReflectUtil.getMethodSignature(method));
        callMessage.setArgs(Arrays.asList(args));
        return callMessage;
    }
}
