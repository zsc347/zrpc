package com.scaiz.zrpc.protocol;

public class CodecFactory {

    private static final Codec codec = new DefaultCodec();

    public static Codec getCodec() {
        return codec;
    }
}
