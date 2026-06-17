package com.hmdp.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestVolatile {
    // 1. 将 volatile int 改为 AtomicInteger，确保并发自增的原子性（最终结果才能稳稳的是 100）
    static volatile int number = 0;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(500);

        // 2. 创建一个容量为 1 的发令枪门栓
        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < 500; i++) {
            executorService.execute(() -> {
                try {
                    // 3. 所有线程一进来，立刻在这里卡住，等待发令枪响
                    latch.await();

                    // 4. 枪响后，100个线程同时爆发执行自增
                    number++;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // 给线程池一点点时间，让 100 个线程全部初始化完毕并卡在 await() 处准备好
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("各就各位，预备---跑！");
        // 5. 倒计时减为 0，相当于鸣枪发令！100个线程同时被唤醒并冲刺
        latch.countDown();

        // 等待 1 秒确保大家都跑完
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("最终结果: " + number);
        executorService.shutdown();
    }
}