package com.scaiz.zrpc.protocol;


import java.nio.charset.StandardCharsets;

public class DefaultCodec implements Codec {

    @Override
    public <T> byte[] encode(T t) {
        return t.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T decode(byte[] bytes) {
        return (T) new String(bytes, StandardCharsets.UTF_8);
    }
}
