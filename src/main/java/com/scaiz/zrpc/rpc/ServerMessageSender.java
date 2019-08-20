package com.scaiz.zrpc.rpc;

import java.nio.channels.Channel;

public interface ServerMessageSender {
    void sendResponse(RpcMessage request, Channel channel, Object rsp);
}
