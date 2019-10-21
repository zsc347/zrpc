package com.scaiz.zrpc.proxy;

import com.scaiz.zrpc.Handler;
import com.scaiz.zrpc.registry.Dispatcher;
import com.scaiz.zrpc.rpc.RpcClientInternal;

import java.util.concurrent.TimeoutException;

public class TestRpcClientInternal implements RpcClientInternal {

    private Dispatcher dispatcher;

    public TestRpcClientInternal(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public Object sendMsgWithResponse(Object msg, long timeout) throws TimeoutException {
        if (msg instanceof RpcCallMessage) {
            RpcCallMessage callMessage = (RpcCallMessage) msg;
            Handler handler = dispatcher.dispatch(callMessage.getClazz(), callMessage.getMethod());
            return handler.execute(callMessage.getArgs().toArray());
        }
        throw new IllegalArgumentException("Invalid  msg type " + msg.getClass());
    }
}
