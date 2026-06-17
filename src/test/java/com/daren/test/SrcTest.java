package com.daren.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SrcTest {
    static int threadNum = 20;
    final static int taskNum = 200;
    static ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
    static CountDownLatch endGate = new CountDownLatch(taskNum);


    public static void main(String[] args) throws InterruptedException {

        long start = System.currentTimeMillis();

        for (int i = 0; i< taskNum; i++){
            executorService.submit(cpuCal());
        }
        endGate.await();
        long end = System.currentTimeMillis();

        System.out.println("线程池数量："+threadNum+" CPU核数："+Runtime.getRuntime().availableProcessors()+"全部结束:time:"+(end-start));
    }

    public static Thread cpuCal(){
        return new Thread(()->{
            long start = System.currentTimeMillis();
            //随便写个cpu耗时的操作
            for (int i = 0;i<10000000;i++){
                StringBuffer sb = new StringBuffer();
                sb.append(i).append(",");
            }
            long end = System.currentTimeMillis();
            System.out.println("线程ID:"+Thread.currentThread().getId()+"; 消耗时间:"+(end-start));
            endGate.countDown();
        });
    }
}

