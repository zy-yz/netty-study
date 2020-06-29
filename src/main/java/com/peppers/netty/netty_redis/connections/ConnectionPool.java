package com.peppers.netty.netty_redis.connections;

import com.peppers.netty.netty_redis.codecs.ByteBufToByteDecoder;
import com.peppers.netty.netty_redis.codecs.ByteToByteBufEncoder;
import com.peppers.netty.netty_redis.codecs.RedisRespHandler;
import com.peppers.netty.netty_redis.config.RedisConfig;
import com.peppers.netty.netty_redis.enums.ClientType;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.SocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * @ClassName ConnectionPool
 * @Author peppers
 * @Date 2020/6/29
 * @Description
 **/
public class ConnectionPool<T> {

    private BlockingQueue<RedisConnection<T>> connections;

    public ConnectionPool(ClientType clientType){
        this.connections = new LinkedBlockingQueue<>(RedisConfig.connectionCount);
        init(clientType);
    }

    private void init(final ClientType clientType){
        /**
         *  BootStrap启动器, 用于启动客户端
         *
         * */
        Bootstrap bootstrap = new Bootstrap();
        /**
         * EventLoopGroup是eventLoop的分组，可以获取到一个或者多个eventLoop对象
         * eventLoop负责处理注册到Channel的IO操作
         * */
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);

        try {
            int count = RedisConfig.connectionCount;
            int tryCount = RedisConfig.connectionCount * 2;
            while (connections.size() < count && tryCount-->0){
                //公平的同步队列，传到RedisRespHandler中用于异步获取返回的数据
                final SynchronousQueue<T> synchronousQueue = new SynchronousQueue<>(true);
                /***
                 *handler连接通道处理器，用来处理各种事件，比如连接，数据接收，异常，数据转换等
                 */
                bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                    /**
                     * ChannelPipeline为ChannelHandler的链，提供了一个容器并定义了传播入站的出站事件流API。
                     * */
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        if(clientType.equals(ClientType.STRING)){
                            //String类型的kv
                            ch.pipeline()
                                    .addLast("stringEncoder",new StringEncoder())
                                    .addLast("stringDecoder",new StringDecoder())
                                    .addLast("redisRespHandler",new RedisRespHandler<>(synchronousQueue));
                        }else if(clientType.equals(ClientType.BINARY)){
                            // byte[] 类型的kv,发送时先用byteToByteBufEncoder转成ByteBuf，发送到Redis
                            //返回数据的处理链 byteBufToByteDecoder -> RedisR二水平Handler
                            ch.pipeline()
                                    .addLast("byteBufToByteDecoder", new ByteBufToByteDecoder())
                                    .addLast("redisRespHandler", new RedisRespHandler<>(synchronousQueue));
                            ch.pipeline().addLast("byteToByteBufEncoder",new ByteToByteBufEncoder());
                        }else {
                            throw new IllegalArgumentException();
                        }
                    }
                });
                SocketAddress remoteAddress;
                ChannelFuture channelFuture = bootstrap.connect(RedisConfig.host,RedisConfig.port).sync();
                /**
                 *channelFuture异步非阻塞，监听自动触发返回结果
                 * */
                Channel channel = channelFuture.channel();
                if (channel.isActive()){
                    String name = "connect-" + connections.size();
                    this.connections.add(new RedisConnection<>(name,(NioSocketChannel)channel,synchronousQueue));

                }
            }
            if (connections.size() != count){
                throw  new IllegalStateException("");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public RedisConnection borrowConnection(){
        try {
            RedisConnection connection = connections.take();
            System.out.println("borrowConnection :"+ connection.getName());
            return connection;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void returnConnection(RedisConnection channel){
        //清除
        channel.cleanUp();
        boolean flag = connections.offer(channel);
        if (!flag){
            flag = connections.offer(channel);
        }
        if(!flag){
            channel.disconnect();
            channel.close();
        }
    }
    public boolean checkChannel(RedisConnection channel){
        return channel != null && channel.isActive();
    }
}
