package com.scaiz.zrpc.netty;

import com.scaiz.zrpc.Disposable;
import com.scaiz.zrpc.RpcMessage;
import com.scaiz.zrpc.protocol.MessageFuture;
import com.scaiz.zrpc.protocol.MsgType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractRpcRemote extends ChannelDuplexHandler implements Disposable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRpcRemote.class);
    private AtomicLong idGenerator = new AtomicLong(0);

    protected final ConcurrentHashMap<Integer, MessageFuture> futures =
            new ConcurrentHashMap<>();

    protected ChannelHandler[] channelHandlers;

    protected final ThreadPoolExecutor messageExecutor;


    public AbstractRpcRemote(ThreadPoolExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }


    private long nextId() {
        return idGenerator.getAndIncrement();
    }

    protected void sendRequest(Channel channel, Object msg) {
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setId(idGenerator.getAndIncrement());
        rpcMessage.setMsgType(MsgType.REQUEST);
        rpcMessage.setBody(msg);
        try {
            channel.writeAndFlush(rpcMessage).await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected void sendResponse(RpcMessage request, Channel channel, Object msg) {
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setMsgType(MsgType.RESPONSE);
        rpcMessage.setId(request.getId());
        rpcMessage.setBody(msg);
        try {
            channel.writeAndFlush(rpcMessage).await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        try {
            destroyChannel(ctx.channel());
        } catch (Exception e) {
            LOGGER.error("close channel " + ctx.channel() + " fail.", e);
        }
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof RpcMessage) {
            RpcMessage rpcMessage = (RpcMessage) msg;
            if (rpcMessage.getMsgType() == MsgType.REQUEST) {
                try {
                    AbstractRpcRemote.this.messageExecutor.execute(() -> {
                        try {
                            dispatch(rpcMessage, ctx);
                        } catch (Throwable th) {
                            LOGGER.error("Network Error {}", th.getMessage(), th);
                        }
                    });
                } catch (RejectedExecutionException e) {
                    LOGGER.error("thread pool is full, current max pool size is "
                            + messageExecutor.getActiveCount());
                }
            }
        }
    }

    public abstract void dispatch(RpcMessage request, ChannelHandlerContext ctx);


    protected void addChannelPipelineLast(Channel channel, ChannelHandler... handlers) {
        if (channel != null && handlers != null) {
            channel.pipeline().addLast(handlers);
        }
    }


    protected void setChannelHandlers(ChannelHandler... handlers) {
        this.channelHandlers = handlers;
    }

    public abstract void destroyChannel(Channel channel);

    public abstract void destroy();
}
