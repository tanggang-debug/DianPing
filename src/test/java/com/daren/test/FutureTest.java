package com.daren.test;

import java.util.concurrent.*;

public class FutureTest {

    public static void main(String[] args) throws InterruptedException {
        System.out.println(System.nanoTime());
        ExecutorService executor = Executors.newCachedThreadPool();
        Future submit = executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
            }finally {
            }
            System.out.println("submit success");
        });
        TimeUnit.SECONDS.sleep(1);
        submit.cancel(true);

        try {
            System.out.println(submit.get());
        } catch (InterruptedException e) {
            throw e;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
//        Task task = new Task();
//        Future<Integer> result = executor.submit(task);

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e1) {
//            e1.printStackTrace();
//        }
//
//        System.out.println("主线程在执行任务");

//        try {
//            System.out.println("task运行结果" + result.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("所有任务执行完毕");
    }
}

class Task implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("子线程在进行计算");
        Thread.sleep(3000);
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            sum += i;
        }
        return sum;
    }
}
