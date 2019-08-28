package com.scaiz.zrpc.protocol;

public class MsgType {
    public static final byte REQUEST = 0;
    public static final byte RESPONSE = 1;
    public static final byte PING = 2;
    public static final byte PONG = 3;


    public static String of(byte b) {
        switch (b) {
            case REQUEST:
                return "REQUEST";
            case RESPONSE:
                return "RESPONSE";
            case PING:
                return "PING";
            case PONG:
                return "PONG";
            default:
                return "UNKNOWN";
        }
    }
}
