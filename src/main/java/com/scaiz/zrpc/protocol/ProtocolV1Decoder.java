package com.scaiz.zrpc.protocol;

import com.scaiz.zrpc.rpc.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolV1Decoder extends LengthFieldBasedFrameDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolV1Decoder.class);

    public ProtocolV1Decoder() {
        this(ProtocolConstants.MAX_FRAME_LENGTH);
    }

    private ProtocolV1Decoder(int maxFrameLength) {
        super(maxFrameLength, 0, 4, -4, 0);
    }


    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;
            try {
                return decodeFrame(frame);
            } catch (Exception e) {
                LOGGER.error("Decode error!", e);
            } finally {
                frame.release();
            }
        }
        return decoded;
    }

    public Object decodeFrame(ByteBuf frame) {
        RpcMessage rpcMessage = new RpcMessage();
        int fullLength = frame.readInt();

        long msgId = frame.readLong();
        rpcMessage.setId(msgId);
        byte msgType = frame.readByte();
        rpcMessage.setMsgType(msgType);
        int bodyLength = fullLength - 13;

        Object body = null;
        if (bodyLength > 0) {
            byte[] bs = new byte[bodyLength];
            frame.readBytes(bs);
            body = CodecFactory.getCodec().decode(bs);
        }
        rpcMessage.setBody(body);
        return rpcMessage;
    }

}
