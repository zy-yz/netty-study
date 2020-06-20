package com.peppers.netty.nettydemo.echo;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName EchoServerHandler
 * @Author peppers
 * @Date 2020/6/10
 * @Description
 **/
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    // 1.
    // EchoServerHandler继承了ChannelInboundHandlerAdapter，并覆盖了channelRead方法，
    // 当接受到客户端发送了请求之后，channelRead方法会被回调。
    // 参数ChannelHandlerContext包含了"当前发送请求的客户端"的一些上下文信息，msg表示客户端发送的请求信息。
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        System.out.println("客户端["+ctx.channel().remoteAddress()+"]->服务器端"+msg.toString());
        final ChannelFuture future = ctx.write(msg+"\n");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        System.out.println("服务器端读取完成...");
        ctx.flush();
        TimeUnit.MICROSECONDS.sleep(200);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
