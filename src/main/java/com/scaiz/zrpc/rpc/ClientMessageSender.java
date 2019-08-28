package com.scaiz.zrpc.rpc;

import java.util.concurrent.TimeoutException;

public interface ClientMessageSender {

    Object sendMsgWithResponse(Object msg, long timeout)
            throws TimeoutException;

    Object sendMsgWithResponse(Object msg) throws TimeoutException;
}
