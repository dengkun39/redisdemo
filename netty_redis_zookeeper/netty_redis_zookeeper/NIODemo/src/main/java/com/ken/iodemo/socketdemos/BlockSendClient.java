package com.ken.iodemo.socketdemos;

import com.ken.util.IOUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

import static com.ken.iodemo.socketdemos.ServerConfig.*;

@Slf4j
public class BlockSendClient extends Socket
{

    private Socket client;

    private FileInputStream fis;

    private DataOutputStream outputStream;

    /**
     * 构造函数<br/>
     * 与服务器建立连接
     *
     * @throws Exception
     */
    public BlockSendClient() throws Exception
    {
        super(SOCKET_SERVER_IP
                , SERVER_PORT);
        this.client = this;
    }

    /**
     * 向服务端传输文件
     *
     * @throws Exception
     */
    public void sendFile() throws Exception
    {
        try
        {

//            String srcPath = SOURCE_FILE_PATH;
//            log.info("srcPath=" , srcPath);
//
//            File file = new File(srcPath);
//            if (file.exists())
//            {
//                fis = new FileInputStream(file);
                outputStream = new DataOutputStream(client.getOutputStream());

                // 长度

//                outputStream.writeLong(file.length());
//                outputStream.flush();
//                // 文件名
//                outputStream.writeUTF("copy_" + file.getName());
//                outputStream.flush();

                for (int i = 0;i < 1000 ;i++)
                {
                    outputStream.writeUTF(String.valueOf(i));
                    outputStream.flush();
                    Thread.sleep(1000);
                }
                // 开始传输文件
                /*
                log.debug("======== start transfer file ========");
                byte[] bytes = new byte[1024];
                int length = 0;
                long progress = 0;
                while ((length = fis.read(bytes, 0, bytes.length)) != -1)
                {
                    outputStream.write(bytes, 0, length);
                    outputStream.flush();
                    progress += length;
                    log.debug("| " + (100 * progress / file.length()) + "% |");
                }
                 */
                log.debug("======== file transfer success ========");
//            }else {
//
//                log.info("======== 文件传输失败, 文件不存在 ========");
//            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {

            IOUtil.closeQuietly(fis);
            IOUtil.closeQuietly(outputStream);
            IOUtil.closeQuietly(client);

        }
    }



    /**
     * 入口
     *
     * @param args
     */
    public static void main(String[] args)
    {
        for(int i =0;i<100;i++){
            new Thread(new Task()).start();
        }
    }

}

class Task  implements Runnable
{

    @Override
    public void run() {
        try
        {
            BlockSendClient client = new BlockSendClient(); // 启动客户端连接
            client.sendFile(); // 传输文件
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}