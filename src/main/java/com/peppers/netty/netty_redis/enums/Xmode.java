package com.peppers.netty.netty_redis.enums;

/**
 * @ClassName Xmode
 * @Author peppers
 * @Date 2020/6/29
 * @Description
 **/
public enum Xmode {
    /**
     * key存在的时候才创建
     */
    XX("XX"),
    /**
     * key不存在的时候才创建
     */
    NX("EX");
    private String type;
    Xmode(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
