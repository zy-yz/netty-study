package com.peppers.netty.nettydemo.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @ClassName EchoServer
 * @Author peppers
 * @Date 2020/6/10
 * @Description
 **/
public class EchoServer {
    private int port;

    public EchoServer(int port){
        this.port = port;
    }

    public void run() throws Exception{
        //配置服务端的NIO线程组
        //主线程组，用于接收客户端的连接，但是不做任何具体业务处理
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //工作组线程组，老板线程会将任务丢给他，让手下线程组做任务
        EventLoopGroup  workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                            ch.pipeline().addLast("decoder",new StringDecoder());
                            ch.pipeline().addLast("encoder",new StringEncoder());
                            ch.pipeline().addLast(new EchoServerHandler());

                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);

                    //绑定端口，开始接受进来的连接
            ChannelFuture f = b.bind(port).sync();

            System.out.println("服务器启动完成，监听端口:" +port);

            //等待服务器监听端口关闭
            f.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }else {
            port = 8040;
        }
        new EchoServer(port).run();
    }
}
