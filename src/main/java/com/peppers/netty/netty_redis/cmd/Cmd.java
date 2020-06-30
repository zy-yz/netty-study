package com.peppers.netty.netty_redis.cmd;

public interface Cmd<PT> {
    /**
     * 构建RESP 协议
     * @return
     */
    PT build();
}