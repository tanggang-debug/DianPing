package com.hmdp.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class VoucherOrderServiceImplTest {
    @Autowired
    private VoucherOrderServiceImpl voucherOrderService;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;
    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }
    @Test
    void testSeckillLuaPerformance() throws InterruptedException {
        int threadNum = 200; // 并发线程数
        int loopCount = 200;  // 每个线程循环次数
        int totalRequests = threadNum * loopCount;

        // 1. 使用线程池代替手动创建线程
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(totalRequests);

        String stockKey = "seckill:voucher:13";
        String orderKey = "seckill:order:13";

        for (int i = 0; i < threadNum; i++) {
            long userId = 319; // 稍微变动下ID，模拟不同用户
            executorService.submit(() -> {
                try {
                    latch.await(); // 等待统一开跑
                    for (int j = 0; j < loopCount; j++) {
                        long start = System.currentTimeMillis();

                        // 执行 Lua 脚本
                        Long ans = stringRedisTemplate.execute(
                                SECKILL_SCRIPT,
                                Arrays.asList(stockKey, orderKey),
                                String.valueOf(userId)
                        );

                        long end = System.currentTimeMillis();
                        // 生产环境建议删除这行打印，频繁的控制台IO会严重拖慢TPS
                         System.out.println(Thread.currentThread().getName() + " 耗时: " + (end - start) + "ms");
                        done.countDown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        long totalStart = System.nanoTime();
        latch.countDown(); // 鸣枪开跑
        done.await();      // 等待所有 5000 个请求完成
        long totalEnd = System.nanoTime();

        executorService.shutdown(); // 测试结束关闭线程池

        long durationMs = (totalEnd - totalStart) / 1_000_000;
        double durationSec = (totalEnd - totalStart) / 1_000_000_000.0;

        System.out.println("========================================");
        System.out.println("总计请求数: " + totalRequests);
        System.out.println("总计耗时: " + durationMs + "ms");
        System.out.println("平均吞吐量 (TPS): " + (totalRequests / durationSec));
        System.out.println("========================================");
    }
    @Test
    void testSeckillApiPerformance() throws InterruptedException {
        int threadNum = 200; // 并发线程数
        int loopCount = 200;  // 每个线程循环 10 次
        int totalRequests = threadNum * loopCount;

        // 1. 准备 HTTP 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "03jaq7t0nt");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 2. 线程池与计数器
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(totalRequests);

        String url = "http://localhost:8081/voucher-order/seckill/13"; // 假设券ID为13

        for (int i = 0; i < threadNum; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await(); // 统一冲锋信号
                    for (int j = 0; j < loopCount; j++) {
                        // 3. 真正发起 HTTP POST 请求
                        ResponseEntity<String> response = restTemplate.exchange(
                                url,
                                HttpMethod.POST,
                                entity,
                                String.class
                        );
                        doneLatch.countDown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        long totalStart = System.currentTimeMillis();
        startLatch.countDown(); // 鸣枪开跑
        doneLatch.await();      // 等待 5000 个请求全部结束
        long totalEnd = System.currentTimeMillis();

        executorService.shutdown();

        // 4. 计算性能指标
        long duration = totalEnd - totalStart;
        double tps = (totalRequests * 1000.0) / duration;

        System.out.println("================ HTTP 接口压测结果 ================");
        System.out.println("总请求数: " + totalRequests);
        System.out.println("总耗时: " + duration + " ms");
        System.out.println("接口平均 TPS: " + String.format("%.2f", tps));
        System.out.println("==================================================");
    }
}