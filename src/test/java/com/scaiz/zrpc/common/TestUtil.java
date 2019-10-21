package com.scaiz.zrpc.common;

import com.scaiz.zrpc.registry.Dispatcher;
import com.scaiz.zrpc.registry.Registry;
import com.scaiz.zrpc.service.HelloService;
import com.scaiz.zrpc.service.impl.HelloServiceImpl;

public class TestUtil {

    public static Dispatcher buildDefaultDispatcher() {
        HelloService helloService = new HelloServiceImpl();
        Registry registry = new Registry();
        registry.register(HelloService.class, helloService);
        return new Dispatcher(registry);
    }

}
