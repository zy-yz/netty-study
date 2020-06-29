package com.peppers.netty.netty_redis.codecs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @ClassName ByteBufToByteDecoder
 * @Author peppers
 * @Date 2020/6/29
 * @Description
 **/
public class ByteBufToByteDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        byte[] bytes = new byte[in.writerIndex()];
        in.readBytes(bytes);
        out.add(bytes);
    }
}
