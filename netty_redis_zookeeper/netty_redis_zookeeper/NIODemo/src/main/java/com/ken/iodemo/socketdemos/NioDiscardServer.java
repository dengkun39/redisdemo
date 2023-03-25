package com.ken.iodemo.socketdemos;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import static com.ken.iodemo.socketdemos.ServerConfig.SERVER_PORT;

@Slf4j
public class NioDiscardServer {

    public static void startServer() throws IOException {

        // 1、创建一个 Selector选择器
        Selector selector = Selector.open();

        // 2、获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        // 3.设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 4、绑定监听端口
        serverSocket.bind(new InetSocketAddress(SERVER_PORT));
        log.info("server start success");

        // 5、将通道注册到选择器上,并注册的IO事件为：“接收新连接”
        SelectionKey sk = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //  sk.interestOps(SelectionKey.OP_ACCEPT) ;

        // 6、轮询感兴趣的I/O就绪事件（选择键集合）
        while (selector.select() > 0) {
            // 7、获取选择键集合
            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();

            while (selectedKeys.hasNext()) {
                // 8、获取单个的选择键，并处理
                SelectionKey selectedKey = selectedKeys.next();

                // 9、判断key是具体的什么事件
                if (selectedKey.isAcceptable()) {
                    log.info("new connection coming" + selectedKey.channel());
                    ServerSocketChannel server = (ServerSocketChannel) selectedKey.channel();
                    SocketChannel socketChannel = server.accept();
                    // 10、若选择键的IO事件是“连接就绪”事件,就获取客户端连接

                    // 11、切换为非阻塞模式
                    socketChannel.configureBlocking(false);
                    socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);

                    // 12、将该通道注册到selector选择器上
                    SelectionKey channleSk=        socketChannel.register(selector,
                            SelectionKey.OP_READ | SelectionKey.OP_WRITE  | SelectionKey.OP_CONNECT);


                }
                if (selectedKey.isWritable()) {
                    //log.info("write ready:" + selectedKey.channel());
                }
                if (selectedKey.isConnectable()) {
                    log.info("client connect success:" + selectedKey.channel());

                }
                if (selectedKey.isReadable()) {
                    log.info("read ready:" + selectedKey.channel());

                    // 13、若选择键的IO事件是“可读”事件,读取数据
                    SocketChannel socketChannel = (SocketChannel) selectedKey.channel();

                    // 14、读取数据
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int length = 0;
                    while ((length = socketChannel.read(byteBuffer)) > 0) {

                        byteBuffer.flip();

                        log.info(new String(byteBuffer.array(), 0, length));

                        byteBuffer.clear();

                    }

                    if(length == -1)
                    {
                        socketChannel.close();
                    }
                }

                // 15、移除选择键
                selectedKeys.remove();
            }
        }

        // 7、关闭连接
        serverSocketChannel.close();
    }

    public static void main(String[] args) throws IOException {
        startServer();
    }

}
