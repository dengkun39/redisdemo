package com.ken.coccurent;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class NettyFutureDemo {
    public static final int SLEEP_GAP = 5000;

    public static String getCurThreadName() {
        return Thread.currentThread().getName();
    }

    static class HotWaterJob implements Callable<Boolean> //①
    {

        @Override
        public Boolean call() throws Exception //②
        {

            try {
                log.info("洗好水壶");
                log.info("灌上凉水");
                log.info("放在火上");

                //线程睡眠一段时间，代表烧水中
                Thread.sleep(SLEEP_GAP);
                log.info("水开了");

            } catch (InterruptedException e) {
                log.info(" 发生异常被中断.");
                return false;
            }
            log.info(" 运行结束.");
            return true;
        }
    }

    static class WashJob implements Callable<Boolean> {
        @Override
        public Boolean call() throws Exception {
            try {
                log.info("洗茶壶");
                log.info("洗茶杯");
                log.info("拿茶叶");
                //线程睡眠一段时间，代表清洗中
                Thread.sleep(SLEEP_GAP);
                log.info("洗完了");

            } catch (InterruptedException e) {
                log.info(" 清洗工作 发生异常被中断.");
                return false;
            }
            log.info(" 清洗工作  运行结束.");
            return true;
        }
    }

    static class MainJob implements Runnable{
        volatile boolean waterOk = false;
        volatile boolean cupOk = false;
        @Override
        public void run() {
            while (true) {
                try {
                    log.info("读书中......");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.info(getCurThreadName() + "发生异常被中断.");
                }


            }
        }

        public void drinkTea() {
            if (waterOk && cupOk) {

                log.info("泡茶喝，茶喝完");

                this.waterOk = false;


            } else if (!waterOk) {
                log.info("烧水 没有完成，没有茶喝了");
            } else if (!cupOk ) {
                log.info("洗杯子  没有完成，没有茶喝了");
            }

        }

        public static void main(String args[]) {

            //新起一个线程，作为泡茶主线程
            NettyFutureDemo.MainJob mainJob = new NettyFutureDemo.MainJob();
            Thread mainThread = new Thread(mainJob);
            mainThread.setName("喝茶线程");
            mainThread.start();

            //烧水的业务逻辑
            Callable<Boolean> hotJob = new HotWaterJob();
            //清洗的业务逻辑
            Callable<Boolean> washJob = new WashJob();

            //创建 netty  线程池
            DefaultEventExecutorGroup npool = new DefaultEventExecutorGroup(2);

            //提交烧水的业务逻辑，取到异步任务
            io.netty.util.concurrent.Future<Boolean> hotFuture = npool.submit(hotJob);
            //绑定任务执行完成后的回调，到异步任务
            hotFuture.addListener(new GenericFutureListener() {
                @Override
                public void operationComplete(io.netty.util.concurrent.Future future) throws Exception {
                    if (future.isSuccess()) {
                        log.info("烧水成功，尝试喝茶");
                        mainJob.waterOk = true;
                        mainJob.drinkTea();
                    }
                    else {
                        log.info("烧水失败，没有茶喝了");
                    }
                }
            });


            io.netty.util.concurrent.Future<Boolean> washFuture = npool.submit(washJob);
            //绑定任务执行完成后的回调，到异步任务
            washFuture.addListener(new GenericFutureListener() {
                @Override
                public void operationComplete(io.netty.util.concurrent.Future future) throws Exception {
                    if (future.isSuccess()) {
                        log.info("清洗成功，尝试喝茶");
                        mainJob.cupOk = true;
                        mainJob.drinkTea();
                    }
                    else {
                        log.info("清洗失败，没有茶喝了");
                    }
                }
            });
        }
    }
}
