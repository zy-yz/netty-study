package com.peppers.netty.nettydemo.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @ClassName EchoClient
 * @Author peppers
 * @Date 2020/6/10
 * @Description EchoClient负责与服务端的xxxx建立连接
 **/
public final class EchoClient {

    static final String HOST = System.getProperty("host","127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port","8040"));

    public static void main(String[] args) throws Exception{

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //第一步
            //首先创建一个Bootstrap实例，这个与ServerBootstrap相对应，表示这是一个客户端的启动类
            Bootstrap b = new Bootstrap();
            // 第二步:
            // 我们调用Bootstrap的group方法给Bootstrap实例设置了一个EventLoopGroup实例。
            // 前面提到，EventLoopGroup的作用是线程池。前面在创建ServerBootstrap时，
            // 设置了一个bossGroup，一个wrokerGroup，这样做主要是为将接受连接和处理连接请求任务划分开，
            // 以提升效率。对于客户端而言，则没有这种需求，只需要设置一个EventLoopGroup实例即可。
            b.group(group)
                    // 第三步:
                    // 通过channel方法指定了NioSocketChannel，这是netty在nio编程中用于表示客户端的对象实例。
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    // 第四步:
                    // 类似server端，在连接创建完成，初始化的时候，
                    // 我们也给SocketChannel添加了几个处理器类。
                    // 其中EchoClientHandler是我们自己编写的给服务端发送请求，并接受服务端响应的处理器类。
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ChannelPipeline p = ch.pipeline();
                            p.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                            p.addLast("decoder", new StringDecoder());
                            p.addLast("encoder", new StringEncoder());
                            p.addLast(new EchoClientHandler());
                        }
                    });
            //启动客户端
            ChannelFuture f = b.connect(HOST,PORT).sync();

            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        }finally {
            //退出，释放线程池资源
            group.shutdownGracefully();
        }
    }
}
