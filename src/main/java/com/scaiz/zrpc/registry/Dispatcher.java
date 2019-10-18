package com.scaiz.zrpc.registry;

import com.scaiz.zrpc.Handler;

public class Dispatcher {
    private Registry registry;

    public Dispatcher(Registry registry) {
        this.registry = registry;
    }

    public Handler dispatch(String clazz, String methodSignature) {
        return registry.getHandler(clazz, methodSignature);
    }
}
