package com.scaiz.zrpc.protocol;

import com.scaiz.zrpc.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolV1Encoder extends MessageToByteEncoder<RpcMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolV1Encoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage msg, ByteBuf out)
            throws Exception {
        try {
            int fullLength = 0;
            int writeCurrentWriteIndex = out.writerIndex();
            out.writeInt(0);
            fullLength += 4;
            out.writeLong(msg.getId());
            fullLength += 8;
            out.writeByte(msg.getMsgType());
            fullLength += 1;

            if (msg.getBody() != null) {
                byte[] body = CodecFactory.getCodec().encode(msg.getBody());
                fullLength += body.length;
            }

            out.writerIndex(writeCurrentWriteIndex);
            out.writeInt(fullLength);
            out.writerIndex(writeCurrentWriteIndex + fullLength);
        } catch (Exception e) {
            LOGGER.error("Encode request error", e);
            throw e;
        }
    }
}
