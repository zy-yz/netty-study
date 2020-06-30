package com.peppers.netty.netty_redis.cmd.impl.getCmd;

import com.peppers.netty.netty_redis.cmd.AbstractCmd;

import java.util.ArrayList;

/**
 * @ClassName AbstractGetCmd
 * @Author peppers
 * @Date 2020/6/29
 * @Description
 **/
public abstract class AbstractGetCmd<T> extends AbstractCmd<T> {

    public AbstractGetCmd(T  key){
        super();
        super.paramList = new ArrayList<>();
        paramList.add(key);
    }

    @Override
    protected String getCmd() {
        return super.cmd = "get";
    }
}

