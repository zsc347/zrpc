package com.scaiz.zrpc.netty;

import com.scaiz.zrpc.protocol.MsgType;
import com.scaiz.zrpc.rpc.ClientMessageListener;
import com.scaiz.zrpc.rpc.ClientMessageSender;
import com.scaiz.zrpc.rpc.RpcMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

public abstract class AbstractRpcRemoteClient extends AbstractRpcRemote implements ClientMessageSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRpcRemoteClient.class);
    private final RpcClientBootStrap clientBootStrap;
    private ClientMessageListener clientMessageListener;
    private final NettyClientConfig nettyClientConfig;
    private Channel serverChannel;

    public AbstractRpcRemoteClient(NettyClientConfig nettyClientConfig,
                                   ThreadPoolExecutor messageExecutor) {
        super(messageExecutor);
        this.nettyClientConfig = nettyClientConfig;
        this.clientBootStrap = new RpcClientBootStrap(nettyClientConfig, this);
    }


    @Override
    public void init() {
        clientBootStrap.start();
        this.serverChannel = clientBootStrap
                .getNewChannel(new InetSocketAddress("127.0.0.1", 8080));
        super.init();
    }

    @Override
    public void destroy() {
        clientBootStrap.shutdown();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof RpcMessage)) {
            return;
        }
        RpcMessage rpcMessage = (RpcMessage) msg;
        if (MsgType.PONG == rpcMessage.getMsgType()) {
            LOGGER.debug("Received PONG from {}", ctx.channel().remoteAddress());
        }
        super.channelRead(ctx, msg);
    }


    @Override
    public void dispatch(RpcMessage request, ChannelHandlerContext ctx) {
        if (clientMessageListener != null) {
            String remoteAddress = NetUtil.toStringAddress((InetSocketAddress)
                    ctx.channel().remoteAddress());
            clientMessageListener.onMessage(request, remoteAddress, this);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (messageExecutor.isShutdown()) {
            return;
        }
        LOGGER.info("Channel inactive: {}", ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (IdleState.READER_IDLE == idleStateEvent.state()) {
                LOGGER.info("Channel {} read idle", ctx.channel());
            }

            if (IdleState.WRITER_IDLE == idleStateEvent.state()) {
                LOGGER.info("Channel {} write idle", ctx.channel());
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        LOGGER.info("Exception caught {}", ctx.channel());
        super.exceptionCaught(ctx, cause);
    }


    @Override
    public Object sendMsgWithResponse(Object msg, long timeout)
            throws TimeoutException {
        return super.sendAsyncRequestWithResponse(null,
                serverChannel,
                msg,
                timeout);
    }

    @Override
    public Object sendMsgWithResponse(Object msg) throws TimeoutException {
        return sendMsgWithResponse(msg,
                nettyClientConfig.getRpcRequestTimeout());
    }


    @Override
    public void destroyChannel(Channel channel) {

    }

    public ClientMessageListener getClientMessageListener() {
        return clientMessageListener;
    }

    public void setClientMessageListener(ClientMessageListener clientMessageListener) {
        this.clientMessageListener = clientMessageListener;
    }
}
