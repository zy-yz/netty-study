package com.peppers.netty.netty_redis.codecs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @ClassName ByteToByteBufEncoder
 * @Author peppers
 * @Date 2020/6/29
 * @Description
 **/
public class ByteToByteBufEncoder extends MessageToByteEncoder<byte[]> {

    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) throws Exception {
        if(msg == null){
            return;
        }
        out.writeBytes(msg);
    }
}