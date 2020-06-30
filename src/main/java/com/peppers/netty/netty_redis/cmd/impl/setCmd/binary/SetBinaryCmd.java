package com.peppers.netty.netty_redis.cmd.impl.setCmd.binary;

import com.peppers.netty.netty_redis.cmd.CmdResp;
import com.peppers.netty.netty_redis.cmd.impl.setCmd.AbstractSetCmd;
import com.peppers.netty.netty_redis.enums.ExpireMode;
import com.peppers.netty.netty_redis.enums.Xmode;
import com.peppers.netty.netty_redis.utils.CmdBuildUtils;
import com.peppers.netty.netty_redis.utils.EncodeUtils;
import com.peppers.netty.netty_redis.utils.SymbolUtils;



/**
 * @ClassName SetBinaryCmd
 * @Author peppers
 * @Date 2020/6/29
 * @Description
 **/
public class SetBinaryCmd extends AbstractSetCmd<byte []>  implements CmdResp<byte [], Boolean> {
    /**
     * 没有过期时间
     * @param key
     * @param value
     */
    public SetBinaryCmd(byte [] key, byte []  value){
        this(key, value, null, 0, null);
    }
    public SetBinaryCmd(byte []  key, byte []  value, ExpireMode expireMode, long expireTime){
        this(key, value, expireMode, expireTime, null);

    }
    public SetBinaryCmd(byte []  key, byte []  value, Xmode xmode){
        this(key, value, null, 0, xmode);
    }
    public SetBinaryCmd(byte []  key, byte []  value, ExpireMode expireMode, long expireTime, Xmode xmode){
        super(  key,
                value,
                EncodeUtils.getBytes(expireMode),
                String.valueOf(expireTime).getBytes(),
                EncodeUtils.getBytes(xmode));
    }

    @Override
    public byte[] build() {
        return CmdBuildUtils.buildBinary(getCmd(), paramList);
    }

    @Override
    public Boolean parseResp(byte[] resp){
        if(resp == null){
            return false;
        }
        if(resp[0] == SymbolUtils.OK_PLUS_BYTE[0]){
            return true;
        }
        System.err.println("resp = [" + new String(resp) + "]");
        return false;
    }
}
