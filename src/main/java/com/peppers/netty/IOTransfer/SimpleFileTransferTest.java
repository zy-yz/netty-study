package com.peppers.netty.IOTransfer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @ClassName SimpleFileTransferTest
 * @Author peppers
 * @Date 2020/6/9
 * @Description
 **/
public class SimpleFileTransferTest {

    private long transferFile(File source, File des) throws IOException{
        long startTime = System.currentTimeMillis();

        if(!des.exists()){
            des.createNewFile();
        }
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(source));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(des));
        //将数据源读到的内容写入目的地--使用数组
        byte[] bytes = new byte[1024*1024];
        int len;
        while ((len = bis.read(bytes)) != -1){
            bos.write(bytes,0,len);
        }
        long  endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private long transferFileWithNIO(File source,File des) throws IOException{
        long startTime = System.currentTimeMillis();

        if(!des.exists()){
            des.createNewFile();
        }

        RandomAccessFile read = new RandomAccessFile(source, "rw");
        RandomAccessFile write = new RandomAccessFile(des, "rw");

        FileChannel readChannel = read.getChannel();
        FileChannel writeChannel = write.getChannel();

        //创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024);

        while (readChannel.read(byteBuffer) > 0){
            byteBuffer.flip();
            writeChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        writeChannel.close();
        readChannel.close();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    public static void main(String[] args) throws IOException {
        SimpleFileTransferTest simpleFileTransferTest = new SimpleFileTransferTest();

        File source = new File("/Users/zhaoyu/Downloads/视屏/突然好想你.mp4");
        File des = new File("/Users/zhaoyu/Downloads/视屏/pr/爱乐之城开头.mp4");
        File nio  = new File("/Users/zhaoyu/Downloads/视屏/pr/爱乐之城开头.mp4");

        long time = simpleFileTransferTest.transferFile(source, des);
        System.out.println(time + "：普通字节流时间");

        long timeNio = simpleFileTransferTest.transferFileWithNIO(source, nio);
        System.out.println(timeNio + "：NIO时间");


        // 创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 看一下初始时4个核心变量的值
        System.out.println("初始时-->limit--->"+byteBuffer.limit());
        System.out.println("初始时-->position--->"+byteBuffer.position());
        System.out.println("初始时-->capacity--->"+byteBuffer.capacity());
        System.out.println("初始时-->mark--->" + byteBuffer.mark());

        System.out.println("--------------------------------------");

        // 添加一些数据到缓冲区中
        String s = "peppers";
        byteBuffer.put(s.getBytes());

        // 看一下初始时4个核心变量的值
        System.out.println("put完之后-->limit--->"+byteBuffer.limit());
        System.out.println("put完之后-->position--->"+byteBuffer.position());
        System.out.println("put完之后-->capacity--->"+byteBuffer.capacity());
        System.out.println("put完之后-->mark--->" + byteBuffer.mark());

        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(s.getBytes());
        System.out.println(new String(bytes, 0, bytes.length));
        System.out.println("--------------------------------------");

        // 看一下初始时4个核心变量的值
        System.out.println("get完之后-->limit--->"+byteBuffer.limit());
        System.out.println("get完之后-->position--->"+byteBuffer.position());
        System.out.println("get完之后-->capacity--->"+byteBuffer.capacity());
        System.out.println("get完之后-->mark--->" + byteBuffer.mark());

        // 添加一些数据到缓冲区中
        byteBuffer.put(s.getBytes());
        byteBuffer.clear();

        // 看一下初始时4个核心变量的值
        System.out.println("put完之后-->limit--->"+byteBuffer.limit());
        System.out.println("put完之后-->position--->"+byteBuffer.position());
        System.out.println("put完之后-->capacity--->"+byteBuffer.capacity());
        System.out.println("put完之后-->mark--->" + byteBuffer.mark());

    }
}
