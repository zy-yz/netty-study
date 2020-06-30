package com.peppers.netty.netty_redis.cmd.impl.getCmd.str;

import com.peppers.netty.netty_redis.cmd.CmdResp;
import com.peppers.netty.netty_redis.cmd.impl.getCmd.AbstractGetCmd;
import com.peppers.netty.netty_redis.utils.CmdBuildUtils;
import com.peppers.netty.netty_redis.utils.SymbolUtils;

/**
 * @ClassName GetStringCmd
 * @Author peppers
 * @Date 2020/6/29
 * @Description
 **/
public class GetStringCmd extends AbstractGetCmd<String> implements CmdResp<String, String> {
    public GetStringCmd(String key) {
        super(key);
    }

    @Override
    public String build() {
        return CmdBuildUtils.buildString(getCmd(), paramList);
    }

    @Override
    public String parseResp(String resp) {
        if(resp == null){
            return null;
        }
        // 跳过第一位符号位
        int index = 1;
        int len = 0;
        char ch = resp.charAt(index);
        while(ch != SymbolUtils.CRLF.charAt(0)) {
            len = (len*10)+(ch - '0');
            ch = resp.charAt(++index);
        }
        if(len <= 0){
            return null;
        }
        index+=2;
        return resp.substring(index, index + len);
    }
}
