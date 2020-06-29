package com.peppers.netty.netty_redis.utils;

/**
 * @author peppers
 */
public class EncodeUtils {
    public static byte[] getBytes(Object object){
        if(object == null){
            return null;
        }
        return String.valueOf(object).getBytes();
    }
}
