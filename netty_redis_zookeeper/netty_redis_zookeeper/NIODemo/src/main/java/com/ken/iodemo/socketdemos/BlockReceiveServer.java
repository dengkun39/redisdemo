package com.ken.iodemo.socketdemos;

import com.ken.util.IOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.config.Task;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static com.ken.iodemo.socketdemos.ServerConfig.RECEIVE_PATH;
import static com.ken.iodemo.socketdemos.ServerConfig.SERVER_PORT;

@Slf4j
public class BlockReceiveServer  extends ServerSocket {
    public BlockReceiveServer() throws Exception
    {
        super(SERVER_PORT);
    }

    public void startServer() throws Exception
    {
        while (true)
        {
            // server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的

            log.debug("server listen at:" + SERVER_PORT);
            Socket socket = this.accept();
            /**
             * 我们的服务端处理客户端的连接请求是同步进行的， 每次接收到来自客户端的连接请求后，
             * 都要先跟当前的客户端通信完之后才能再处理下一个连接请求。 这在并发比较多的情况下会严重影响程序的性能，
             * 为此，我们可以把它改为如下这种异步处理与客户端通信的方式
             */
            // 每接收到一个Socket就建立一个新的线程来处理它
            new Thread(new Task(socket)).start();
        }
    }

    /**
     * 处理客户端传输过来的文件线程类
     */
    class Task implements Runnable
    {

        private Socket socket;

        private DataInputStream dis;

        private FileOutputStream fos;

        public Task(Socket socket)
        {
            this.socket = socket;
        }

        @Override
        public void run()
        {
            try
            {
                dis = new DataInputStream(socket.getInputStream());
                /*
                // 长度
                long fileLength = dis.readLong();
                // 文件名
                String fileName = dis.readUTF();
                 */
                log.debug("start receive");
                for (int i = 0;i < 1000 ;i++)
                {
                    dis.readUTF();
                }

                log.debug("finish receive" );

                /*

                File directory = new File(RECEIVE_PATH);
                if (!directory.exists())
                {
                    directory.mkdir();
                }
                File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
                fos = new FileOutputStream(file);
                long startTime = System.currentTimeMillis();
                log.debug("block IO start transfer:");

                // 开始接收文件
                byte[] bytes = new byte[1024];
                int length = 0;
                while ((length = dis.read(bytes, 0, bytes.length)) != -1)
                {
                    fos.write(bytes, 0, length);
                    fos.flush();
                }

                log.debug("file receive success,File Name: " + fileName);
                log.debug(" Size:" + IOUtil.getFormatFileSize(fileLength));
                long endTime = System.currentTimeMillis();
                log.debug("block IO cost time:" + (endTime - startTime));

                */

            } catch (Exception e)
            {
                e.printStackTrace();
            } finally
            {

                IOUtil.closeQuietly(fos);
                IOUtil.closeQuietly(dis);
                IOUtil.closeQuietly(socket);

            }
        }
    }


    /**
     * 入口
     *
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            // 启动服务端
            BlockReceiveServer server = new BlockReceiveServer();
            server.startServer();

            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
