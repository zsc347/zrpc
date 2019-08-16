package com.scaiz.zrpc.netty;

import com.scaiz.zrpc.RemotingService;
import com.scaiz.zrpc.common.NamedThreadFactory;
import com.scaiz.zrpc.protocol.ProtocolV1Decoder;
import com.scaiz.zrpc.protocol.ProtocolV1Encoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractRpcRemoteServer extends AbstractRpcRemote implements RemotingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRpcRemoteServer.class);
    private String host;
    private int port;

    private final ServerBootstrap serverBootstrap;
    private final EventLoopGroup eventLoopGroupWorker;
    private final EventLoopGroup eventLoopGroupBoss;
    private final NettyServerConfig nettyServerConfig;

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public void setPort(int port) {
        if (port <= 0) {
            throw new IllegalArgumentException("Invalid listen port " + port);
        }
        this.port = port;
    }


    public int getPort() {
        return this.port;
    }

    public void setHost(String host) {
        if (!NetUtil.isValidIp(host)) {
            throw new IllegalArgumentException("Invalid host" + host);
        }
        this.host = host;
    }

    public String getHost() {
        return this.host;
    }


    public AbstractRpcRemoteServer(NettyServerConfig config,
                                   ThreadPoolExecutor messageExecutor,
                                   ChannelHandler... handlers) {
        super(messageExecutor);
        this.nettyServerConfig = config;
        this.serverBootstrap = new ServerBootstrap();
        this.eventLoopGroupBoss = new EpollEventLoopGroup(
                nettyServerConfig.getBossThreadSize(),
                new NamedThreadFactory(true, config.getBossThreadPrefix(),
                        config.getBossThreadSize()));
        this.eventLoopGroupWorker = new EpollEventLoopGroup(
                nettyServerConfig.getWorkerThreadSize(),
                new NamedThreadFactory(true, config.getWorkerThreadPrefix(),
                        config.getWorkerThreadSize()));
        if (handlers != null) {
            setChannelHandlers(handlers);
        }
        setPort(config.getPort());
    }


    @Override
    public void start() {
        this.serverBootstrap.group(this.eventLoopGroupBoss, this.eventLoopGroupWorker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, nettyServerConfig.getSoBackLogSize())
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_SNDBUF,
                        nettyServerConfig.getServerSocketSendBufSize())
                .childOption(ChannelOption.SO_RCVBUF,
                        nettyServerConfig.getServerSocketRecvBufSize())
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,
                        new WriteBufferWaterMark(
                                nettyServerConfig.getWriteBufferHighWaterMark(),
                                nettyServerConfig.getWriteBufferLowWaterMark()))
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new IdleStateHandler(nettyServerConfig.getMaxReadIdleSeconds(), 0, 0))
                                .addLast(new ProtocolV1Decoder())
                                .addLast(new ProtocolV1Encoder());
                        if (channelHandlers != null) {
                            addChannelPipelineLast(ch, channelHandlers);
                        }
                    }
                });

        if (nettyServerConfig.isEnableServerPooledByteBufferAllocator()) {
            this.serverBootstrap.childOption(ChannelOption.ALLOCATOR,
                    NettyServerConfig.)
        }
    }

}