package com.peppers.netty.netty_redis.clients;

import com.peppers.netty.netty_redis.enums.ExpireMode;
import com.peppers.netty.netty_redis.enums.Xmode;

public interface RedisClient<T> {
    
    /**
     * @Description      set 命令，没有过期时间   
     * @param [key, v]
     * @return boolean
     */
    boolean set(T key,T v);
    
    
    /**
     * @Description        SETNX 命令
     * @param [key, v]
     * @return boolean
     */
    boolean setNx(T key,T v);
    
    /**
     * @Description        带有过期时间的set命令
     * @param [key, v, seconds]
     * @return boolean
     */
    boolean setWithExpireTime(T key,T v,long seconds);

    /**
     * @Description        
     * @param [key, v, expireMode, expireTime, x]
     * @return boolean
     */
    boolean set(T key, T v, ExpireMode expireMode, long expireTime, Xmode x);

    /**
     * get命令
     * @param key
     * @return
     */
    T get(T key);
}
