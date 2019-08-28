package com.scaiz.zrpc.common;

import io.netty.util.NettyRuntime;

public enum WorkThreadMode {

    Auto(NettyRuntime.availableProcessors() * 2 + 1),
    Pin(NettyRuntime.availableProcessors()),
    BusyPin(NettyRuntime.availableProcessors() + 1),
    Default(NettyRuntime.availableProcessors() * 2);

    WorkThreadMode(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }
}
