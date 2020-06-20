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
    //设置Server监听的端口客户端请求
    private int port;

    public EchoServer(int port){
        this.port = port;
    }

    public void run() throws Exception{
        //第一步，首先创建两个EventLoopGroup实例，bossGroup和workerGroup，(可以理解为两个线程池)
        //配置服务端的NIO线程组
        //主线程组，用于接收客户端的连接，但是不做任何具体业务处理
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //工作组线程组，boss线程会将任务丢给他，让手下线程组做任务
        EventLoopGroup  workerGroup = new NioEventLoopGroup();

        try {
            //第二步，创建一个ServerBootstrap实例，这是一个服务端启动类
            //需要设置一些参数，包括第一步创建的bossGroup和workerGroup
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    //第三步
                    //通过channel方法指定NioServerSocketChannel，这是netty中表示服务端的类
                    //用于接收客户端请求，对应java.nio中的ServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    //第四步
                    //通过childHandler方法，设置一个匿名内部类ChannelInitializer实例，也可以不用匿名内部类而是重写一个类
                    //用于初始化客户端连接SocketChannel实例
                    //在接收到客户端连接之后，netty会回调ChannelInitializier的initChannel方法需要对接受到的这个客户端连接进行一些初始化工作，
                    //主要是告诉netty之后如何处理和相应这个客户端的请求
                    // 在这里，主要是添加了3个ChannelHandler实例：LineBasedFrameDecoder、StringDecoder、TimeServerHandler。
                    // 其中LineBasedFrameDecoder、StringDecoder是netty本身提供的，用于解决TCP粘包、解包的工具类。

                    //LineBasedFrameDecoder在解析客户端请求时，遇到字符”\n”或”\r\n”时则认为是一个完整的请求报文，
                    // 然后将这个请求报文的二进制字节流交给StringDecoder处理。

                    //StringDecoder将二进制字节流转换成一个字符串，交给TimeServerHandler来进行处理。

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
            //第五步，
            //在所有的配置都设置好之后，我们调用ServerBootstrap的bind(part)方法
            //.sync()必须要写，这样才会一直等待，不会退出来
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
