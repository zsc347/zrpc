package com.scaiz.zrpc.rpc;

import java.util.concurrent.TimeoutException;

public interface RpcClientInternal {

     Object sendMsgWithResponse(Object msg, long timeout) throws TimeoutException;
}
