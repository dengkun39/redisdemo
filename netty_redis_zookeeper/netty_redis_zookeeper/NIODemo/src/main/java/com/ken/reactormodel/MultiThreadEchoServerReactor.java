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
import java.util.concurrent.atomic.AtomicInteger;

import static com.ken.iodemo.socketdemos.ServerConfig.SERVER_PORT;
import static com.ken.iodemo.socketdemos.ServerConfig.SOCKET_SERVER_IP;

@Slf4j
public class MultiThreadEchoServerReactor {
    ServerSocketChannel serverSocket;
    AtomicInteger next = new AtomicInteger(0);
    Selector bossSelector = null;
    Reactor bossReactor = null;
    //selectors集合,引入多个selector选择器
    Selector[] workSelectors = new Selector[2];
    //引入多个子反应器
    Reactor[] workReactors = null;


    MultiThreadEchoServerReactor() throws IOException {

        //初始化多个selector选择器
        bossSelector = Selector.open();// 用于监听新连接事件
        workSelectors[0] = Selector.open(); // 用于监听read、write事件
        workSelectors[1] = Selector.open(); // 用于监听read、write事件
        serverSocket = ServerSocketChannel.open();

        InetSocketAddress address =
                new InetSocketAddress(SOCKET_SERVER_IP,
                        SERVER_PORT);
        serverSocket.socket().bind(address);

        serverSocket.configureBlocking(false);//非阻塞

        //bossSelector,负责监控新连接事件, 将 serverSocket注册到bossSelector
        SelectionKey sk =
                serverSocket.register(bossSelector, SelectionKey.OP_ACCEPT);



        //绑定Handler：新连接监控handler绑定到SelectionKey（选择键）
        sk.attach(new AcceptorHandler());

        //bossReactor反应器，处理新连接的bossSelector
        bossReactor = new Reactor(bossSelector);

        //第一个子反应器，一子反应器负责一个worker选择器
        Reactor workReactor1 = new Reactor(workSelectors[0]);
        //第二个子反应器，一子反应器负责一个worker选择器
        Reactor workReactor2 = new Reactor(workSelectors[1]);
        workReactors = new Reactor[]{workReactor1, workReactor2};
    }

    private void startService() {
        // 一子反应器对应一条线程
        new Thread(bossReactor).start();
        new Thread(workReactors[0]).start();
        new Thread(workReactors[1]).start();
    }

    //反应器
    class Reactor implements Runnable {
        //每条线程负责一个选择器的查询
        final Selector selector;

        public Reactor(Selector selector) {
            this.selector = selector;
        }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    //单位为毫秒
                    selector.select(1000);
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    if (null == selectedKeys || selectedKeys.size() == 0) {
                        continue;
                    }
                    Iterator<SelectionKey> it = selectedKeys.iterator();
                    while (it.hasNext()) {
                        //Reactor负责dispatch收到的事件
                        SelectionKey sk = it.next();
                        dispatch(sk);
                    }
                    selectedKeys.clear();
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
    }


    // Handler:新连接处理器
    class AcceptorHandler implements Runnable {
        public void run() {
            try {
                SocketChannel channel = serverSocket.accept();
                log.info("receive a new connection");

                if (channel != null) {
                    int index = next.get();
                    log.info("select index is: " + index);
                    Selector selector = workSelectors[index];
                    new MultiThreadEchoHandler(selector, channel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (next.incrementAndGet() == workSelectors.length) {
                next.set(0);
            }
        }
    }


    public static void main(String[] args) throws IOException {
        MultiThreadEchoServerReactor server =
                new MultiThreadEchoServerReactor();
        server.startService();
    }

}
