package com.scaiz.zrpc;

import io.netty.channel.ChannelHandlerContext;

public interface ServerMessageListener {
    void onMessage(RpcMessage request, ChannelHandlerContext ctx, ServerMessageSender sender);
}
