package com.ken.coccurent;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class GuavaFutureDemo {
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
            MainJob mainJob = new MainJob();
            Thread mainThread = new Thread(mainJob);
            mainThread.setName("喝茶线程");
            mainThread.start();

            //烧水的业务逻辑
            Callable<Boolean> hotJob = new HotWaterJob();
            //清洗的业务逻辑
            Callable<Boolean> washJob = new WashJob();

            //创建java 线程池
            ExecutorService jPool =
                    Executors.newFixedThreadPool(10);

            //包装java线程池，构造guava 线程池
            ListeningExecutorService gPool =
                    MoreExecutors.listeningDecorator(jPool);

            //提交烧水的业务逻辑，取到异步任务
            ListenableFuture<Boolean> hotFuture = gPool.submit(hotJob);

            Futures.addCallback(hotFuture, new FutureCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    log.info("烧水成功，尝试喝茶");
                    if(aBoolean) {
                        mainJob.waterOk = aBoolean;
                        mainJob.drinkTea();
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    log.info("烧水失败，没有茶喝了");
                }
            }, MoreExecutors.directExecutor());


            ListenableFuture<Boolean> washFuture = gPool.submit(washJob);

            Futures.addCallback(washFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                log.info("清洗成功，尝试喝茶");
                if(aBoolean) {
                    mainJob.cupOk = aBoolean;
                    mainJob.drinkTea();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                log.info("清洗失败，没有茶喝了");
            }
        }, MoreExecutors.directExecutor());
    }


}
}
