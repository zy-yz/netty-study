package com.peppers.netty.nettydemo.echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @ClassName EchoClientHandler
 * @Author peppers
 * @Date 2020/6/10
 * @Description
 **/
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    private final String firstMessage;

    public EchoClientHandler() {
        firstMessage = "hello";
    }
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道注册成功...");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(firstMessage);
        System.out.println("通道被激活...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("收到服务器端消息： " + msg);
        ctx.write(msg + "\n");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
        System.out.println("通道读取完成....");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
