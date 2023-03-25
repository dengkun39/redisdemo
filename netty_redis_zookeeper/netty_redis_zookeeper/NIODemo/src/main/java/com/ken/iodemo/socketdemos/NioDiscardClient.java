package com.ken.iodemo.socketdemos;

import com.ken.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static com.ken.iodemo.socketdemos.ServerConfig.SERVER_PORT;
import static com.ken.iodemo.socketdemos.ServerConfig.SOCKET_SERVER_IP;

@Slf4j
public class NioDiscardClient {
    public  void startClient(int clientNum) throws IOException {
        InetSocketAddress address =
                new InetSocketAddress(SOCKET_SERVER_IP,
                        SERVER_PORT);

        // 1、获取通道（channel）
        SocketChannel socketChannel = SocketChannel.open(address);
        // 2、切换成非阻塞模式
        socketChannel.configureBlocking(false);
        //不断的自旋、等待连接完成，或者做一些其他的事情
        while (!socketChannel.finishConnect()) {

        }

        log.info("client connect success");
        for(int i = 0;i<1000;i++) {
            // 3、分配指定大小的缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put((clientNum + "hello" + i).getBytes());
            byteBuffer.flip();
            socketChannel.write(byteBuffer);

            ThreadUtil.sleepSeconds(1);
        }
        log.info("client write success");

        socketChannel.shutdownOutput();
        socketChannel.close();
    }


    public static void main(String[] args) throws IOException {
        for(int i =0;i<50;i++){
            new Thread(new NoiTask(i)).start();
        }
    }

}

class NoiTask  implements Runnable
{
    private int clientNum;
    public NoiTask(int clientNum)
    {
        this.clientNum = clientNum;
    }

    @Override
    public void run() {
        try
        {
            NioDiscardClient client = new NioDiscardClient(); // 启动客户端连接
            client.startClient(clientNum); // 传输文件
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
