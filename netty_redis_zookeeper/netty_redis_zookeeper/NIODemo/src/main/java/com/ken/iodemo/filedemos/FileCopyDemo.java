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
public class FileCopyDemo {
    public static void main(String[] args)
    {
        try {
            for (int i = 0;i<100;i++)
            {
                copyResourceFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void copyResourceFile() throws IOException {
        String srcPath = "C:\\work\\FundBond\\funddataqatool.zip";
        String destPath = "C:\\softs\\temp\\funddataqatool.zip";

        File srcFile = new File(srcPath);
        File destFile = new File(destPath);


        if (!destFile.exists()){
            destFile.createNewFile();
        }


        long startTime = System.currentTimeMillis();

        FileInputStream input = new FileInputStream(srcFile);
        FileOutputStream output = new FileOutputStream(destFile);
        byte[] buf = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buf)) != -1) {
            output.write(buf, 0, bytesRead);
            //System.out.println("写入的字节数" + bytesRead);
        }
        output.flush();
        long endTime = System.currentTimeMillis();

        log.info("base 复制毫秒数：" + (endTime - startTime));
    }
}
