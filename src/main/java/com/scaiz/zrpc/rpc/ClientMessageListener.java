package com.scaiz.zrpc.rpc;

public interface ClientMessageListener {

    void onMessage(RpcMessage request, String serverAddress,
                   ClientMessageSender sender);
}
