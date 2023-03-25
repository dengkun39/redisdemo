package com.ken.reactormodel;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static com.ken.iodemo.socketdemos.ServerConfig.SERVER_PORT;
import static com.ken.iodemo.socketdemos.ServerConfig.SOCKET_SERVER_IP;

@Slf4j
public class EchoServerReactor implements Runnable {

    Selector selector;
    ServerSocketChannel serverSocket;

    EchoServerReactor() throws IOException {
        //Reactor初始化
        selector = Selector.open();

        serverSocket = ServerSocketChannel.open();

        InetSocketAddress address =
                new InetSocketAddress(SOCKET_SERVER_IP, SERVER_PORT);

        //非阻塞
        serverSocket.configureBlocking(false);


        //分步处理,第一步,接收accept事件
        SelectionKey sk =
                serverSocket.register(selector,0,new AcceptorHandler());

        // SelectionKey.OP_ACCEPT
        serverSocket.socket().bind(address);
        log.info("start listening: "+address);


        sk.interestOps(SelectionKey.OP_ACCEPT);

        //attach callback object, AcceptorHandler
        //sk.attach(new AcceptorHandler());
    }


    class AcceptorHandler implements Runnable {
        public void run() {
            try {
                SocketChannel channel = serverSocket.accept();
                log.info("receiving a connection");
                if (channel != null)
                    new EchoHandler(selector, channel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {

                //io事件的查询
                // 限时阻塞查询
                selector.select(1000);


                Set<SelectionKey> selected = selector.selectedKeys();
                if (null == selected || selected.size() == 0) {
                    continue;
                }
                Iterator<SelectionKey> it = selected.iterator();
                while (it.hasNext()) {
                    //Reactor负责dispatch收到的事件
                    SelectionKey sk = it.next();

                    it.remove();     //避免下次重复处理

                    dispatch(sk);
                }
//                selected.clear();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void dispatch(SelectionKey sk) {
        Runnable handler = (Runnable) sk.attachment();
        //调用之前attach绑定到选择键的handler处理器对象
        if (handler != null) {
            handler.run();
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(new EchoServerReactor()).start();
    }
}
