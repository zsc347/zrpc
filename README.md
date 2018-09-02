[![Build Status](https://travis-ci.org/zsc347/zrpc.svg?branch=develop)](https://travis-ci.org/zsc347/zrpc)

zrpc是一个轻量级的异步rpc框架。


本项目有以下特点：

1. 基于Java的异步接口`CompletableFuture`, 通过异步处理及并发执行的方式
提供流畅高效的异步处理。 

2. 以事件总线的方式实现服务提供和服务处理的解耦。

2. 使用`bson`作为传输默认传输格式。传输格式可配置。

3. 基于`Netty`提供网络通信，支持以`socket`, `http2`, `websocket`等方式提供服务。

4. 自带配置中心及负载均衡，无需依赖`zookeeper`等第三方服务。

