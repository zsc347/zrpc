package com.scaiz.zrpc.protocol;

public interface Codec {

    <T> byte[] encode(T t);


    <T> T decode(byte[] bytes);
}
