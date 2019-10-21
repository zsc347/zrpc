package com.scaiz.zrpc.dispatcher;


import com.scaiz.zrpc.Handler;
import com.scaiz.zrpc.common.TestUtil;
import com.scaiz.zrpc.registry.Dispatcher;
import com.scaiz.zrpc.registry.ReflectUtil;
import com.scaiz.zrpc.service.HelloService;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;

public class DispatcherTest {

    private static Dispatcher dispatcher;

    @BeforeClass
    public static void setup() {
        dispatcher = TestUtil.buildDefaultDispatcher();
    }

    @Test
    public void testDispatch() throws Exception {
        Method method = HelloService.class.getMethod("hello", String.class);
        Handler handler = dispatcher.dispatch(HelloService.class.getName(), ReflectUtil.getMethodSignature(method));
        Object result = handler.execute("hahaha");
        System.out.println(result);
    }
}
