package com.scaiz.zrpc.eventbus;


import com.scaiz.zrpc.Handler;
import com.scaiz.zrpc.registry.Dispatcher;
import com.scaiz.zrpc.registry.ReflectUtil;
import com.scaiz.zrpc.registry.Registry;
import com.scaiz.zrpc.service.HelloService;
import com.scaiz.zrpc.service.impl.HelloServiceImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;

public class DispatcherTest {

    private static Dispatcher dispatcher;

    @BeforeClass
    public static void setup() {
        HelloService helloService = new HelloServiceImpl();
        Registry registry = new Registry();
        registry.register(HelloService.class, helloService);
        dispatcher = new Dispatcher(registry);
    }

    @Test
    public void testDispatch() throws Exception {
        Method method = HelloService.class.getMethod("hello", String.class);
        Handler handler = dispatcher.dispatch(HelloService.class.getName(), ReflectUtil.getMethodSignature(method));
        Object result = handler.execute("hahaha");
        System.out.println(result);
    }
}
