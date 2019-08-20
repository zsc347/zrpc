package com.scaiz.zrpc.netty;

import com.scaiz.zrpc.rpc.Disposable;
import com.scaiz.zrpc.rpc.RpcMessage;
import com.scaiz.zrpc.protocol.MessageFuture;
import com.scaiz.zrpc.protocol.MsgType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractRpcRemote extends ChannelDuplexHandler implements Disposable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRpcRemote.class);
    private AtomicLong idGenerator = new AtomicLong(0);

    protected final ConcurrentHashMap<Long, MessageFuture> futures =
            new ConcurrentHashMap<>();

    protected ChannelHandler[] channelHandlers;

    protected final ThreadPoolExecutor messageExecutor;

    protected final ConcurrentHashMap<String, BlockingQueue<RpcMessage>> basketMap =
            new ConcurrentHashMap<>();


    public AbstractRpcRemote(ThreadPoolExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }


    public void init() {
    }


    private long nextId() {
        return idGenerator.getAndIncrement();
    }

    protected Object sendAsyncRequestWithResponse(String address, Channel channel,
                                                  Object msg, long timeout)
            throws TimeoutException {
        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout must > 0");
        }
        return sendAsyncRequest(address, channel, msg, timeout);
    }


    private Object sendAsyncRequest(String address, Channel channel,
                                    Object msg, long timeout)
            throws TimeoutException {
        if (channel == null) {
            LOGGER.warn("channel null, send nothing");
            return null;
        }

        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setId(nextId());
        rpcMessage.setMsgType(MsgType.REQUEST);
        rpcMessage.setBody(msg);

        MessageFuture messageFuture = new MessageFuture();
        messageFuture.setRequestMessage(rpcMessage);
        messageFuture.setTimeout(timeout);
        futures.put(rpcMessage.getId(), messageFuture);

        if (address != null) {
            BlockingQueue<RpcMessage> basket = basketMap.get(address);
            if (basket == null) {
                basketMap.putIfAbsent(address, new LinkedBlockingQueue<>());
                basket = basketMap.get(address);
            }
            basket.offer(rpcMessage);
        } else {
            ChannelFuture future = channel.writeAndFlush(rpcMessage);
            future.addListener((ChannelFutureListener) f -> {
                if (!f.isSuccess()) {
                    MessageFuture msgFuture = futures.remove(rpcMessage.getId());
                    if (msgFuture != null) {
                        msgFuture.setResult(future.cause());
                    }
                    destroyChannel(future.channel());
                }
            });
        }

        if (timeout > 0) {
            try {
                return messageFuture.get(timeout, TimeUnit.MICROSECONDS);
            } catch (Exception e) {
                LOGGER.error("wait response error ", e);
                if (e instanceof TimeoutException) {
                    throw (TimeoutException) e;
                } else {
                    throw new RuntimeException(e);
                }
            }
        } else {
            return null;
        }
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
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
            } else {
                LOGGER.debug("response {}", rpcMessage);
                MessageFuture messageFuture = futures.remove(rpcMessage.getId());
                if (messageFuture != null) {
                    messageFuture.setResult(rpcMessage.getBody());
                } else {
                    this.messageExecutor.execute(() -> dispatch(rpcMessage, ctx));
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

    @Override
    public void destroy() {
        messageExecutor.shutdown();
    }
}
