package com.peppers.netty.netty_redis.cmd;

public interface CmdResp<PARAM,RETURN> {
    /**
     * 解析redis的resp
     * @param resp
     * @return
     */
    RETURN parseResp(PARAM resp);
}
