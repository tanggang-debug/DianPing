package com.daren.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class RedisIdWorkerTest {
    @Autowired
    private RedisIdWorker redisIdWorker;

    @Test
    void test() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(300);
        CountDownLatch startLatch = new CountDownLatch(1);
        ExecutorService es = Executors.newFixedThreadPool(100);
        Set<Long> set = ConcurrentHashMap.newKeySet();
        Runnable task = () -> {
            try {
                startLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0; i < 100; i++) {
                long id = redisIdWorker.nextId();
                set.add(id);
                System.out.println("id = " + id);
            }
            latch.countDown();
        };
        for (int i = 0; i < 300; i++) {
            es.submit(task);
        }
        startLatch.countDown();
        long begin = System.currentTimeMillis();
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("count = " + set.size());
        System.out.println("time = " + (end - begin));
    }
}