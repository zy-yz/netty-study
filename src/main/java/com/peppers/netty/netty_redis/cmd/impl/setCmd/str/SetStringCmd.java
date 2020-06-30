package com.peppers.netty.netty_redis.cmd.impl.setCmd.str;

import com.peppers.netty.netty_redis.cmd.CmdResp;
import com.peppers.netty.netty_redis.cmd.impl.setCmd.AbstractSetCmd;
import com.peppers.netty.netty_redis.enums.ExpireMode;
import com.peppers.netty.netty_redis.enums.Xmode;
import com.peppers.netty.netty_redis.utils.CmdBuildUtils;
import com.peppers.netty.netty_redis.utils.SymbolUtils;

/**
 * @ClassName SetStringCmd
 * @Author peppers
 * @Date 2020/6/29
 * @Description
 **/
public class SetStringCmd extends AbstractSetCmd<String> implements CmdResp<String,Boolean> {
    /**
     * 没有过期时间
     * @param key
     * @param value
     */
    public SetStringCmd(String key, String value){
        this(key, value, null, 0, null);
    }

    /**
     *
     * @param key
     * @param value
     * @param expireMode
     * @param expireTime
     */
    public SetStringCmd(String key, String value, ExpireMode expireMode, long expireTime){
        this(key, value, expireMode, expireTime, null);

    }
    public SetStringCmd(String key, String value, Xmode xmode){
        this(key, value, null, 0, xmode);
    }
    public SetStringCmd(String key, String value, ExpireMode expireMode, long expireTime, Xmode xmode){
        super( key,
                value ,
                expireMode == null ? null : expireMode.getType(),
                String.valueOf(expireTime),
                xmode == null ? null : xmode.getType() );
    }


    /**
     * 构建请求参数RESP
     * @return
     */
    @Override
    public String build() {
        return CmdBuildUtils.buildString(getCmd(), paramList);
    }

    /**
     * 解析redis返回的RESP
     * @param resp
     * @return
     */
    @Override
    public Boolean parseResp(String resp) {
        char ch = resp.charAt(0);
        // 一般返回 +OK 就是成功
        if(ch == SymbolUtils.OK_PLUS.charAt(0)){
            return true;
        }
        // 其他的都是失败
        return false;
    }
}
