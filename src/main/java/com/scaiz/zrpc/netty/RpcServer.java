package com.scaiz.zrpc.netty;

import com.scaiz.zrpc.rpc.RpcMessage;
import com.scaiz.zrpc.rpc.ServerMessageListener;
import com.scaiz.zrpc.rpc.ServerMessageSender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;


@Sharable
public class RpcServer extends AbstractRpcRemoteServer implements ServerMessageSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    private ServerMessageListener serverMessageListener;


    public RpcServer(ThreadPoolExecutor messageExecutor) {
        this(new NettyServerConfig(), messageExecutor);
    }

    public RpcServer(NettyServerConfig config,
                     ThreadPoolExecutor messageExecutor,
                     ChannelHandler... handlers) {
        super(config, messageExecutor, handlers);
    }

    @Override
    public void init() {
        super.init();
        setChannelHandlers(this);
        serverMessageListener = new DefaultServerMessageListenerImpl();
        super.start();
    }

    @Override
    public void destroy() {
        super.destroy();
        super.shutdown();
        LOGGER.info("Destroyed rpc server");
    }


    private void closeChannelHandlerContext(ChannelHandlerContext ctx) {
        ctx.disconnect();
        ctx.close();
    }


    @Override
    public void dispatch(RpcMessage request, ChannelHandlerContext ctx) {
        serverMessageListener.onMessage(request, ctx, this);
    }


    @Override
    public void sendResponse(RpcMessage request, Channel channel, Object msg) {
        if (channel != null) {
            super.sendResponse(request, channel, msg);
        } else {
            throw new RuntimeException("null client channel.");
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (messageExecutor.isShutdown()) {
            return;
        }
        super.channelInactive(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        LOGGER.info("channel {} caught exception ", ctx.channel(), cause);
        super.exceptionCaught(ctx, cause);
    }
}
