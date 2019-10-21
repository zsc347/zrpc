package com.scaiz.zrpc.proxy;

import java.util.List;

public class RpcCallMessage {
    private String clazz;
    private String method;
    private List<Object> args;

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getClazz() {
        return this.clazz;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setArgs(List<Object> args) {
        this.args = args;
    }

    public List<Object> getArgs() {
        return this.args;
    }
}
