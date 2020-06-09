package com.peppers.netty.NIO.buffers;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * @ClassName BufferAccess
 * @Author peppers
 * @Date 2020/6/9
 * @Description
 **/
public class BufferAccess {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        printBuffer(buffer);

        buffer.put((byte)'H').put((byte)'e').put((byte)'l').put((byte)'l').put((byte)'0');
        printBuffer(buffer);

        //取Buffer
        System.out.println("" + (char) buffer.get() + (char)buffer.get());
        printBuffer(buffer);

        buffer.mark();
        printBuffer(buffer);

        //读取两个元素后，恢复到之前mark的位置处
        System.out.println(""+(char)buffer.get()+(char)buffer.get());
        printBuffer(buffer);

        buffer.reset();
        printBuffer(buffer);

        //将 position 与 limit之间的数据复制到buffer的开始位置，复制后 position  = limit -position,limit = capacity
        //但如果position 与limit 之间没有数据的话发，就不会进行复制
        buffer.compact();
        printBuffer(buffer);

        buffer.clear();
        printBuffer(buffer);
    }

    private static void printBuffer(Buffer buffer) {
        System.out.println("[limit=" + buffer.limit()
                +", position = " + buffer.position()
                +", capacity = " + buffer.capacity()
                +", array = " + buffer.toString()+"]");
    }
}
