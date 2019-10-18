package com.scaiz.zrpc.service.impl;

import com.scaiz.zrpc.service.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String message) {
        return "hello," + message;
    }
}
