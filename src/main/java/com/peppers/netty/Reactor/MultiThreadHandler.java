package com.peppers.netty.Reactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName MultiThreadHandler
 * @Author peppers
 * @Date 2020/6/29
 * @Description 多线程处理读写业务逻辑
 **/
public class MultiThreadHandler implements Runnable{

    public static final int READING = 0,WRITING = 1;
    int state;
    final SocketChannel socket;
    final SelectionKey sk;

    //多线程处理业务逻辑
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public MultiThreadHandler(SocketChannel socket, Selector sl) throws IOException {
        this.state = READING;
        this.socket = socket;
        this.sk = socket.register(sl,SelectionKey.OP_READ);
        sk.attach(this);
        socket.configureBlocking(false);
    }

    @Override
    public void run() {
        if(state == READING){
            read();
        }else if(state == WRITING){
            write();
        }

    }

    private void read(){
        //任务异步处理
        executorService.submit(()->process());

        //下一步处理写事件
        sk.interestOps(SelectionKey.OP_WRITE);
        this.state = WRITING;
    }

    /**
     * task 业务处理
     */
    public void process() {
        //do IO ,task,queue something
    }
    private void write() {
        //任务异步处理
        executorService.submit(() -> process());
        sk.interestOps(SelectionKey.OP_READ);
        this.state = READING;
    }
}
