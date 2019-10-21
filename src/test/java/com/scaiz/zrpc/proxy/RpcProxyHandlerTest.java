package com.scaiz.zrpc.proxy;

import com.scaiz.zrpc.common.TestUtil;
import com.scaiz.zrpc.registry.Dispatcher;
import com.scaiz.zrpc.service.HelloService;
import org.junit.Test;

public class RpcProxyHandlerTest {

    @Test
    public void testProxy() {
        Dispatcher dispatcher = TestUtil.buildDefaultDispatcher();
        HelloService helloService = ProxyFactory.createProxy(HelloService.class, new TestRpcClientInternal(dispatcher));
        String result = helloService.hello("hahahaha");
        System.out.println(result);
    }
}
