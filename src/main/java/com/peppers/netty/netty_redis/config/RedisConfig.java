package com.peppers.netty.netty_redis.config;

/**
 * @ClassName RedisConfig
 * @Author peppers
 * @Date 2020/6/29
 * @Description
 **/
public class RedisConfig {
    public static String host = "localhost";
    public static int port = 6379;
    /**
     * 每个client创建的连接数
     */
    public static int connectionCount = 10;

    /**
     * 超时时间 ms
     */
    public static int TIME_OUT_MS =  5000;
}
