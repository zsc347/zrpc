package com.scaiz.zrpc.rpc;

import io.netty.channel.ChannelHandlerContext;

public interface ServerMessageListener {
    void onMessage(RpcMessage request, ChannelHandlerContext ctx, ServerMessageSender sender);
}
