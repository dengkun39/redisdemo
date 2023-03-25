package com.ken.iodemo.filedemos;

import com.ken.util.IOUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


@Slf4j
public class FileNIOCopyDemo {
    public static void main(String[] args)
    {
        try {
            for (int i = 0;i<3;i++) {
                nioCopyResourceFile();
            }

            System.out.println("helloWorld");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void nioCopyResourceFile() throws IOException {
        String srcPath = "C:\\work\\FundBond\\funddataqatool.zip";
        String destPath = "C:\\softs\\temp\\funddataqatool.zip";

        File srcFile = new File(srcPath);
        File destFile = new File(destPath);


        if (!destFile.exists()){
            destFile.createNewFile();
        }

        long startTime = System.currentTimeMillis();

        FileInputStream fis  = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();

        int length = -1;
        ByteBuffer buf = ByteBuffer.allocate(1024);
        while((length = inChannel.read(buf)) != -1){
            buf.flip();
            int outlength = 0;
            while((outlength = outChannel.write(buf)) !=0){
                //System.out.println("写入的字节数" + outlength);
            }

            buf.clear();
        }

        outChannel.force(true);
        IOUtil.closeQuietly(outChannel);
        IOUtil.closeQuietly(fos);
        IOUtil.closeQuietly(inChannel);
        IOUtil.closeQuietly(fis);

        long endTime = System.currentTimeMillis();

        log.info("base 复制毫秒数：" + (endTime - startTime));
    }
}
