package com.scaiz.zrpc.netty;

import com.scaiz.zrpc.rpc.RpcMessage;
import com.scaiz.zrpc.rpc.ServerMessageListener;
import com.scaiz.zrpc.rpc.ServerMessageSender;
import io.netty.channel.ChannelHandlerContext;

public class DefaultServerMessageListenerImpl implements ServerMessageListener {

    @Override
    public void onMessage(RpcMessage request, ChannelHandlerContext ctx,
                          ServerMessageSender sender) {
        Object message = request.getBody();
        sender.sendResponse(request, ctx.channel(), "Response " + message.toString());
    }
}
