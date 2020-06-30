package com.peppers.netty.netty_redis.cmd.impl.setCmd;

import com.peppers.netty.netty_redis.cmd.AbstractCmd;

import java.util.ArrayList;

/**
 * @ClassName AbstractSetCmd
 * @Author peppers
 * @Date 2020/6/29
 * @Description set命令的抽象类
 **/
public abstract class AbstractSetCmd<T> extends AbstractCmd<T> {
    public AbstractSetCmd(T key,T value,T expireMode,T expireTime,T xmode){
        super();
        super.paramList = new ArrayList<>();
        paramList.add(key);
        paramList.add(value);

        //设置了过期时间
        if(expireMode != null){
            paramList.add(expireMode);
            paramList.add(expireTime);
        }

        //设置了XX或者NX
        if(xmode != null){
            paramList.add(xmode);
        }
    }
    @Override
    protected String getCmd() {
        return super.cmd = "set";
    }
}
