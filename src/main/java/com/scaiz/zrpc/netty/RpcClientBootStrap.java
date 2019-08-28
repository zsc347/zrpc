package com.scaiz.zrpc.netty;

import com.scaiz.zrpc.common.NamedThreadFactory;
import com.scaiz.zrpc.protocol.ProtocolV1Decoder;
import com.scaiz.zrpc.protocol.ProtocolV1Encoder;
import com.scaiz.zrpc.rpc.RemoteClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class RpcClientBootStrap implements RemoteClient {
    private final Logger LOGGER = LoggerFactory.getLogger(RpcClientBootStrap.class);

    private final NettyClientConfig clientConfig;
    private final EventLoopGroup eventLoopGroupWorker;
    private final ChannelHandler channelHandler;
    private final Bootstrap bootstrap = new Bootstrap();
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public RpcClientBootStrap(NettyClientConfig clientConfig,
                              ChannelHandler channelHandler) {
        this.clientConfig = clientConfig;
        this.eventLoopGroupWorker = new NioEventLoopGroup(
                clientConfig.getClientSelectorThreadSize(),
                new NamedThreadFactory(true,
                        clientConfig.getClientSelectorThreadPrefix(),
                        clientConfig.getClientSelectorThreadSize())
        );
        this.channelHandler = channelHandler;
    }

    @Override
    public void start() {
        this.bootstrap
                .group(this.eventLoopGroupWorker)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                        clientConfig.getConnectTimeoutMillis())
                .option(ChannelOption.SO_SNDBUF,
                        clientConfig.getClientSocketSndBufSize())
                .option(ChannelOption.SO_RCVBUF, clientConfig.getClientSocketRcvBufSize())
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelHandler idleHandler = new IdleStateHandler(clientConfig.getMaxReadIdleSeconds(),
                                clientConfig.getMaxWriteIdleSeconds(),
                                clientConfig.getMaxAllIdleSeconds());

                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(idleHandler)
                                .addLast(new ProtocolV1Decoder())
                                .addLast(new ProtocolV1Encoder());
                        if (RpcClientBootStrap.this.channelHandler != null) {
                            ch.pipeline().addLast(RpcClientBootStrap.this.channelHandler);
                        }
                    }
                });
        initialized.compareAndSet(false, true);
    }


    @Override
    public void shutdown() {
        this.eventLoopGroupWorker.shutdownGracefully();
    }


    public Channel getNewChannel(InetSocketAddress address) {
        ChannelFuture f = this.bootstrap.connect(address);
        try {
            f.await(this.clientConfig.getConnectTimeoutMillis(), TimeUnit.MILLISECONDS);
            if (f.isCancelled()) {
                LOGGER.error("Error occur when try to connect to server", f.cause());
                throw new RuntimeException("Connect cancelled", f.cause());
            } else if (!f.isSuccess()) {
                LOGGER.error("Error occur when try to connect to server", f.cause());
                throw new RuntimeException("Connect failed", f.cause());
            } else {
                return f.channel();
            }
        } catch (Exception e) {
            LOGGER.error("Error occur when try to connect to server", e);
            throw new RuntimeException("Cannot connect to rpc server", e);
        }
    }
}
