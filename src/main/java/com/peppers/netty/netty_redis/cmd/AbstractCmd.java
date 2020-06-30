package com.peppers.netty.netty_redis.cmd;

import java.util.List;

/**
 * @ClassName AbstractCmd
 * @Author peppers
 * @Date 2020/6/29
 * @Description
 **/
public abstract class AbstractCmd<T> implements Cmd<T> {
    /**
     * 具体是什么命令, 例如get set等待
     */
    protected String cmd;
    /**
     * 这个命令后面的参数
     */
    protected List<T> paramList;
    /**
     *  redis命令
     * @return
     */
    protected abstract String getCmd();

}
