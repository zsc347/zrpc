package com.scaiz.zrpc.rpc;


import io.netty.channel.Channel;

public interface ServerMessageSender {
    void sendResponse(RpcMessage request, Channel channel, Object rsp);
}
