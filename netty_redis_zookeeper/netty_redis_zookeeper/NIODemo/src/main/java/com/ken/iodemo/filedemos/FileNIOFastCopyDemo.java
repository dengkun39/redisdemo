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
public class FileNIOFastCopyDemo {
    public static void main(String[] args)
    {
        try {
            for (int i = 0;i<1;i++) {
                nioCopyResourceFile();
            }
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

        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            inChannel = fis.getChannel();
            outChannel = fos.getChannel();
            long size = inChannel.size();
            long pos = 0;
            long count = 0;
            while (pos < size) {
                //每次复制最多1024个字节，没有就复制剩余的
                count = size - pos > 1024 ? 1024 : size - pos;
                //复制内存,偏移量pos + count长度
                pos += outChannel.transferFrom(inChannel, pos, count);
            }

            //强制刷新磁盘
            outChannel.force(true);
        } finally {
            IOUtil.closeQuietly(outChannel);
            IOUtil.closeQuietly(fos);
            IOUtil.closeQuietly(inChannel);
            IOUtil.closeQuietly(fis);
        }
        long endTime = System.currentTimeMillis();

        log.info("base 复制毫秒数：" + (endTime - startTime));
    }
}
