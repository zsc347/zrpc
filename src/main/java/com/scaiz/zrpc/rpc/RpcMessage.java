package com.scaiz.zrpc.rpc;

import com.scaiz.zrpc.protocol.MsgType;

public class RpcMessage {
    private long id;
    private byte msgType;
    private Object body;


    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "[" + id + "]-[" + MsgType.of(this.msgType) + "]-[" + this.body + "]";
    }
}
