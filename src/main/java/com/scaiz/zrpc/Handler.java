package com.scaiz.zrpc;

import java.lang.reflect.Method;

public class Handler {
    private Object bean;
    private Method method;

    public Handler(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    public Object execute(Object... args) {
        try {
            return method.invoke(bean, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
