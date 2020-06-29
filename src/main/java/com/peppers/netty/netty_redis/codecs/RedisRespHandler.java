package com.peppers.netty.netty_redis.codecs;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.SynchronousQueue;

/**
 * @ClassName RedisRespHandler
 * @Author peppers
 * @Date 2020/6/29
 * @Description
 **/
public class RedisRespHandler<T> extends ChannelInboundHandlerAdapter {
    private SynchronousQueue<T> synchronousQueue;

    public RedisRespHandler(SynchronousQueue<T> synchronousQueue){
        this.synchronousQueue = synchronousQueue;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        super.channelRead(ctx,msg);
        synchronousQueue.put((T)msg);
    }
}
