package com.daren.test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

 class AnyOfFutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<Void> a = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("我执行完了");
        });
        CompletableFuture<Void> b = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("我也执行完了");
        });
        CompletableFuture<Object> anyOfFuture = CompletableFuture.anyOf(a, b).whenComplete((m, k) -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println("finish");

            throw new RuntimeException();
//            return "捡田螺的小男孩";
        });
//        anyOfFuture.join();
    }
}
