package com.scaiz.zrpc;

import java.nio.channels.Channel;

public interface ServerMessageSender {
    void sendResponse(RpcMessage request, Channel channel, Object rsp);
}
